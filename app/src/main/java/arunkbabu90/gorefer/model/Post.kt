package arunkbabu90.gorefer.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(@SerializedName("id") val id: String = "",
                @SerializedName("title") val title: String = "",
                @SerializedName("body") val body: String = "") : Parcelable