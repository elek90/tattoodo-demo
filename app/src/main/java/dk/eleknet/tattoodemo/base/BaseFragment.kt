package dk.eleknet.tattoodemo.base

import android.content.Context
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseFragment : Fragment() {

    protected lateinit var activity: BaseActivity
    protected lateinit var disposeBag: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            context as BaseActivity
            this.activity = context
            inject(context)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onStart() {
        super.onStart()
        disposeBag = CompositeDisposable()
        bindViewModel()
    }

    override fun onStop() {
        super.onStop()
        if (!disposeBag.isDisposed) {
            disposeBag.dispose()
        }
        disposeViewModel()
    }

    /**
     * Function to inject dependencies into fragment
     */
    open fun inject(activity: BaseActivity) {}

    /**
     * Function where the fragment should bind to the view model
     */
    protected abstract fun bindViewModel()

    /**
     * Tell the viewmodel to dispose any subscriptions
     */
    open fun disposeViewModel() {}
}