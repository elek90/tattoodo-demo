package dk.eleknet.tattoodemo.app.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import dk.eleknet.tattoodemo.R
import dk.eleknet.tattoodemo.TattoodoApp
import dk.eleknet.tattoodemo.app.details.DetailsFragment
import dk.eleknet.tattoodemo.base.BaseActivity
import dk.eleknet.tattoodemo.base.BaseFragment
import dk.eleknet.tattoodemo.providers.ISchedulerProvider
import dk.eleknet.tattoodemo.util.showMessage
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_grid.*
import timber.log.Timber
import javax.inject.Inject

class GridFragment : BaseFragment(), GridAdapter.GridAdapterCallback {


    @Inject lateinit var viewModel: GridViewModel
    @Inject lateinit var schedulerProvider: ISchedulerProvider

    private lateinit var adapter: GridAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var endlessScrollEnabled = true
    private var isLoading = false
    private val rowItems = 3

    override fun inject(activity: BaseActivity) {
        super.inject(activity)
        (activity.application as TattoodoApp).appComponent
            .with(GridComponent.Module())
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(dk.eleknet.tattoodemo.R.layout.fragment_grid, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        adapter = GridAdapter(viewModel.getStatePosts(), this)
        gridRecyclerView.setHasFixedSize(true)
        gridRecyclerView.adapter = adapter
        gridRecyclerView.itemAnimator = ScaleInAnimator(OvershootInterpolator(1f))

        gridSwipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        gridSwipeRefresh.setOnRefreshListener {
            viewModel.loadPosts(true)
        }

        // use a linear layout manager
        layoutManager = GridLayoutManager(view.context, rowItems)
        gridRecyclerView.layoutManager = layoutManager

        gridRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && endlessScrollEnabled && !isLoading) { // Ignore scroll up gesture
                    if (isLastItemVisible()) {
                        viewModel.loadPosts()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPosts()
    }

    override fun bindViewModel() {
        viewModel.stateSubject
            .observeOn(schedulerProvider.mainThread())
            .subscribeBy { state ->
                endlessScrollEnabled = state.hasMoreToLoad
                isLoading = state.isLoading
                gridSwipeRefresh.isRefreshing = state.isLoading

                val recyclerViewState = gridRecyclerView.layoutManager?.onSaveInstanceState()
                adapter.setItems(state.posts)
                state.postsDiff?.dispatchUpdatesTo(adapter)
                gridRecyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)

                if (state.error != -1) {
                    showMessage(state.error)
                }
            }
            .addTo(disposeBag)
    }

    override fun onItemClicked(postId: Int, viewHolder: GridAdapter.ViewHolder) {
        Timber.d("itemId: $postId")

        val detailsFragment = DetailsFragment.newInstance(postId)

        val iconTransform = TransitionInflater.from(context).inflateTransition(R.transition.icon_transform)
        val fadeTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)

        //Set shared element transition on new fragment
        detailsFragment.sharedElementEnterTransition = iconTransform
        detailsFragment.sharedElementReturnTransition = iconTransform

        // Fade other elements on the fragment
        detailsFragment.enterTransition = fadeTransition

        // Allow both transitions to run at the same time
        detailsFragment.allowEnterTransitionOverlap = true
        detailsFragment.allowReturnTransitionOverlap = true

        activity.supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true) // Must be set otherwise postpone calls have no effect.
            .addSharedElement(viewHolder.postImage, getString(R.string.image_transition))
            .add(R.id.navigationFrame, detailsFragment)
            .hide(this)
            .addToBackStack(null)
            .commit()
    }

    private fun isLastItemVisible(): Boolean {
        val layoutManager = gridRecyclerView.layoutManager as GridLayoutManager
        val lastItem = layoutManager.findLastCompletelyVisibleItemPosition()
        val currentTotalCount = layoutManager.itemCount

        return currentTotalCount <= lastItem + rowItems
    }
}