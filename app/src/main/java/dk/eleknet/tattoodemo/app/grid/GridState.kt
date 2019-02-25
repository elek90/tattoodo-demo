package dk.eleknet.tattoodemo.app.grid

import androidx.recyclerview.widget.DiffUtil
import dk.eleknet.tattoodemo.base.BaseState
import dk.eleknet.tattoodemo.models.Post

data class GridState(
    var posts: List<Post> = listOf(),
    var postsDiff: DiffUtil.DiffResult? = null,
    var nextPage: Int = 1,
    var isLoading: Boolean = false,
    var hasMoreToLoad: Boolean = true
    ) : BaseState()