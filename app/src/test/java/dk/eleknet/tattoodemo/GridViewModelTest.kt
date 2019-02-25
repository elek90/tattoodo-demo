package dk.eleknet.tattoodemo

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import dk.eleknet.tattoodemo.app.grid.GridState
import dk.eleknet.tattoodemo.app.grid.GridViewModel
import dk.eleknet.tattoodemo.models.Meta
import dk.eleknet.tattoodemo.models.Pagination
import dk.eleknet.tattoodemo.models.Post
import dk.eleknet.tattoodemo.network.TattoodoApi
import dk.eleknet.tattoodemo.network.responses.PostListResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GridViewModelTest {

    @Mock private lateinit var tattoodoApi: TattoodoApi

    private lateinit var state: GridState
    private lateinit var viewModel: GridViewModel
    private val schedulerProvider = TestSchedulerProvider()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        state = GridState()
        viewModel = GridViewModel(state, tattoodoApi, schedulerProvider)
    }

    @Test
    fun canLoadPosts() {
        val posts = listOf(
            Post(id = 0),
            Post(id = 1),
            Post(id = 2)
        )
        val meta = Meta(Pagination())

        whenever(tattoodoApi.getPosts()).thenReturn(
            Single.just(PostListResponse(posts, meta))
        )

        val testObserver = TestObserver <GridState>()
        viewModel.stateSubject.subscribe(testObserver)

        // assert initial state is empty
        assertTrue("Initial state has posts", state.posts.isEmpty())

        viewModel.loadPosts(false)

        testObserver.assertNoErrors()
        val resultState = testObserver.values().last()

        // assert the returned state contains the expected posts
        assertEquals("Post list was modified", posts, resultState.posts)

        // Check only one call to the api was made.
        Mockito.verify(tattoodoApi, times(1)).getPosts(anyInt())
    }
}
