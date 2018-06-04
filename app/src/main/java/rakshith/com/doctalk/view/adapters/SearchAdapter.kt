package rakshith.com.doctalk.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import rakshith.com.doctalk.R
import rakshith.com.doctalk.services.models.GitResponse

/**
 * Created DocTalk by rakshith on 6/3/18.
 */

class SearchAdapter(gitResponse: GitResponse?) : RecyclerView.Adapter<SearchViewHolder>() {
    var mItems = gitResponse?.items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mItems?.size as Int
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        var gitItem = mItems?.get(position)

        holder?.tvName?.text = gitItem?.login
        holder?.tvScore?.text = gitItem?.score.toString()
    }
}

class SearchViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var tvName = itemView?.findViewById<TextView>(R.id.item_row_name)
    var tvScore = itemView?.findViewById<TextView>(R.id.item_row_score)
}
