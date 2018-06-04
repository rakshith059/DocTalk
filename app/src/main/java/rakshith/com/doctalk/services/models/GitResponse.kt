package rakshith.com.doctalk.services.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created DocTalk by rakshith on 6/3/18.
 */

class GitResponse {

    @SerializedName("total_count")
    @Expose
    var totalCount: Int? = null
    @SerializedName("incomplete_results")
    @Expose
    var incompleteResults: Boolean? = null
    @SerializedName("items")
    @Expose
    var items: List<GitItem>? = null

}