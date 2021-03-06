package dk.eleknet.tattoodemo.app.grid

import androidx.recyclerview.widget.DiffUtil
import dk.eleknet.tattoodemo.models.Post

class PostDiffCallback(private val current: List<Post>, private val next: List<Post>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return current.size
    }

    override fun getNewListSize(): Int {
        return next.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val currentItem = current[oldItemPosition]
        val nextItem = next[newItemPosition]
        return currentItem.id == nextItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val currentItem = current[oldItemPosition]
        val nextItem = next[newItemPosition]
        return currentItem == nextItem
    }
}