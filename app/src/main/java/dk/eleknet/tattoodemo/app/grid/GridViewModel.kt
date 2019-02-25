package dk.eleknet.tattoodemo.app.grid

import androidx.recyclerview.widget.DiffUtil
import dk.eleknet.tattoodemo.R
import dk.eleknet.tattoodemo.base.BaseViewModel
import dk.eleknet.tattoodemo.models.Post
import dk.eleknet.tattoodemo.network.TattoodoApi
import dk.eleknet.tattoodemo.providers.ISchedulerProvider
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class GridViewModel(
    state: GridState,
    private val tattoodoApi: TattoodoApi,
    private val schedulerProvider: ISchedulerProvider
) : BaseViewModel<GridState>(state) {

    fun loadPosts(isRefresh: Boolean = false) {
        val page = if (isRefresh) {
            1
        } else {
            state.nextPage
        }
        tattoodoApi.getPosts(page)
            .subscribeOn(schedulerProvider.io())
            .map { response -> // Update page
                if (!isRefresh) {
                    state.nextPage = state.nextPage + 1
                    state.hasMoreToLoad = !response.meta.pagination.links.next.isEmpty()
                }
                return@map response
            }
            .map { response ->
                val updatedPosts = mutableListOf<Post>()
                updatedPosts.addAll(state.posts)
                response.data.forEach { post ->
                    val index = updatedPosts.indexOfFirst { it.id == post.id }
                    if (index == -1) {
                        updatedPosts.add(post)
                    }
                }
                updatedPosts.sortBy {
                    it.updatedAt
                }
                return@map updatedPosts
            }
            .map {updatedPosts ->
                val diffResult = DiffUtil.calculateDiff(PostDiffCallback(state.posts, updatedPosts))
                return@map Pair(updatedPosts, diffResult)
            }
            .doOnSubscribe {
                state.isLoading = true
                stateSubject.onNext(state)
            }
            .doFinally {
                state.isLoading = false
                stateSubject.onNext(state)
                if (state.posts.size == 10) {
                    loadPosts()
                }
            }
            .subscribeBy(
                onSuccess = {
                    state.posts = it.first
                    state.postsDiff = it.second
                },
                onError = {
                    Timber.e(it)
                    state.error = R.string.error_loading_posts
                }
            )
            .addTo(disposeBag)
    }

    fun getStatePosts() = state.posts

}