package dk.eleknet.tattoodemo.app.grid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dk.eleknet.tattoodemo.R
import dk.eleknet.tattoodemo.di.modules.GlideApp
import dk.eleknet.tattoodemo.models.Post


class GridAdapter(private var dataSet: List<Post>, private val callback: GridAdapterCallback) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    interface GridAdapterCallback {
        fun onItemClicked(postId: Int, viewHolder: ViewHolder)
    }

    fun setItems(posts: List<Post>) {
        dataSet = posts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        ViewCompat.setTransitionName(holder.postImage, "transition_$position")
        val post = dataSet[position]
        val options = RequestOptions().apply {
            centerCrop()
        }
        GlideApp.with(context)
            .load(post.image.url)
            .thumbnail(0.5f)
            .apply(options)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_photo_white_24dp)
            .error(R.drawable.ic_mood_bad_red_a400_24dp)
            .into(holder.postImage)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postImage: ImageView = view.findViewById(R.id.postImage)

        init {
            view.setOnClickListener {
                callback.onItemClicked(dataSet[adapterPosition].id, this)
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    override fun getItemId(position: Int): Long = dataSet[position].id.toLong()
}