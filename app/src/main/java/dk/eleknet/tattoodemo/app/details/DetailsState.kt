package dk.eleknet.tattoodemo.app.details

import dk.eleknet.tattoodemo.base.BaseState
import dk.eleknet.tattoodemo.models.Post

data class DetailsState(
    var post: Post = Post()
) : BaseState()