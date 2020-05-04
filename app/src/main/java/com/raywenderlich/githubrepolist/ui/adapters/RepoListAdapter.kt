package com.raywenderlich.githubrepolist.ui.adapters
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raywenderlich.githubrepolist.R
import com.raywenderlich.githubrepolist.data.Item
import com.raywenderlich.githubrepolist.data.RepoResult
import com.raywenderlich.githubrepolist.extensions.ctx
import com.raywenderlich.githubrepolist.ui.activities.DetailsActivity
import kotlinx.android.synthetic.main.item_repo.view.*
class RepoListAdapter(private val repoList: RepoResult) : RecyclerView.Adapter<RepoListAdapter.ViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_repo, parent, false)
    return ViewHolder(view)
  }
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindRepo(repoList.items[position])
  }
  override fun getItemCount(): Int = repoList.items.size
  class ViewHolder(v:View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private var view: View = v
    override fun onClick(v: View?) {
      val context = itemView.context
      val showPhotoIntent = Intent(context, DetailsActivity::class.java)
      context.startActivity(showPhotoIntent)
    }
    init {
      v.setOnClickListener(this)
    }
    fun bindRepo(repo: Item) {
      view.username.text = repo.owner.login.orEmpty()
      view.repoName.text = repo.fullName.orEmpty()
      view.repoDescription.text = repo.description.orEmpty()
      Glide.with(view.context).load(repo.owner.avatarUrl).into(view.icon)
    }
  }
}
