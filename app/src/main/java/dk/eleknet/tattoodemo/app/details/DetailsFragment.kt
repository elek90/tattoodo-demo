package dk.eleknet.tattoodemo.app.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import dk.eleknet.tattoodemo.R
import dk.eleknet.tattoodemo.TattoodoApp
import dk.eleknet.tattoodemo.base.BaseActivity
import dk.eleknet.tattoodemo.base.BaseFragment
import dk.eleknet.tattoodemo.di.modules.GlideApp
import dk.eleknet.tattoodemo.models.Post
import dk.eleknet.tattoodemo.providers.ISchedulerProvider
import dk.eleknet.tattoodemo.util.showMessage
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_details.*
import timber.log.Timber
import javax.inject.Inject

class DetailsFragment : BaseFragment() {

    companion object {
        const val POST_ID = "POST_ID"
        fun newInstance(postId: Int): DetailsFragment {
            val bundle = Bundle()
            bundle.putInt(POST_ID, postId)
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject lateinit var viewModel: DetailsViewModel
    @Inject lateinit var schedulerProvider: ISchedulerProvider

    override fun inject(activity: BaseActivity) {
        super.inject(activity)
        (activity.application as TattoodoApp).appComponent
            .with(DetailsComponent.Module())
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let { arguments ->
            val postId = arguments.getInt(POST_ID, 0)
            viewModel.loadPost(postId)
            detailsImage.transitionName = getString(R.string.image_transition)
            detailsImage.setAllowParentInterceptOnEdge(false)
        }
    }

    override fun bindViewModel() {
        viewModel.stateSubject
            .observeOn(schedulerProvider.mainThread())
            .subscribeOn(schedulerProvider.mainThread())
            .subscribeBy { state ->
                setImage(state.post.image.url)
                setDetails(state.post)

                if (state.error != -1) {

                }
            }
            .addTo(disposeBag)
    }

    private fun setDetails(post: Post) {
        postArtist.text = post.artist?.name
        postStore.text = post.shop?.name
        postLikes.text = post.counts?.likes.toString()

        postStore.setOnClickListener {
            openBrowser(post.shop?.contact?.website.orEmpty())
        }
    }

    private fun setImage(imageUrl: String) {
        val options = RequestOptions().apply {
            fitCenter()
            onlyRetrieveFromCache(true)
        }

        GlideApp.with(activity)
            .load(imageUrl)
            .apply(options)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e(e)
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(detailsImage)
    }

    private fun openBrowser(url: String) {
        if (!url.isEmpty()) {
            val webpage: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(activity.packageManager) != null) {
                startActivity(intent)
            } else {
                showMessage(R.string.details_open_browser_invalid_url, Snackbar.LENGTH_SHORT)
            }
        } else {
            showMessage(R.string.details_open_browser_empty_url, Snackbar.LENGTH_SHORT)
        }
    }
}