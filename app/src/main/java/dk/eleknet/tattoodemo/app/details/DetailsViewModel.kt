package dk.eleknet.tattoodemo.app.details

import dk.eleknet.tattoodemo.base.BaseViewModel
import dk.eleknet.tattoodemo.network.TattoodoApi
import dk.eleknet.tattoodemo.providers.ISchedulerProvider
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class DetailsViewModel(
    state: DetailsState,
    private val tattoodoApi: TattoodoApi,
    private val schedulerProvider: ISchedulerProvider
) : BaseViewModel<DetailsState>(state) {

    fun loadPost(postId: Int) {
        tattoodoApi.getPost(postId)
            .subscribeOn(schedulerProvider.io())
            .map { response ->
                return@map response.data
            }
            .subscribeBy(
                onSuccess = {
                    state.post = it
                    stateSubject.onNext(state)
                },
                onError = {
                    Timber.e(it)
                }
            )
            .addTo(disposeBag)
    }
}
