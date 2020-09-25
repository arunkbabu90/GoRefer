package arunkbabu90.gorefer.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import arunkbabu90.gorefer.R
import arunkbabu90.gorefer.inflate
import arunkbabu90.gorefer.model.Post
import kotlinx.android.synthetic.main.item_two_line_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter(private var posts: ArrayList<Post>,
                  private val itemClickListener: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder
            = PostViewHolder(parent.inflate(R.layout.item_two_line_list))

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post: Post = posts[position]
        holder.bind(post, itemClickListener)
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post, itemClickListener: (Post) -> Unit) {
            itemView.twoLineList_title.text = post.title.capitalize(Locale.getDefault())
            itemView.twoLineList_subtitle.text = post.body.capitalize(Locale.getDefault())

            itemView.setOnClickListener { itemClickListener(post) }
        }
    }
}