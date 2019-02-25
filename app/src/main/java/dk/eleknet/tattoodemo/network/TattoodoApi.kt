package dk.eleknet.tattoodemo.network

import dk.eleknet.tattoodemo.network.responses.PostListResponse
import dk.eleknet.tattoodemo.network.responses.PostResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TattoodoApi {

    @GET("api/v2/search/posts")
    fun getPosts(@Query("page") page: Int = 1): Single<PostListResponse>

    @GET("api/v2/posts/{postId}")
    fun getPost(@Path("postId") postId: Int): Single<PostResponse>

}