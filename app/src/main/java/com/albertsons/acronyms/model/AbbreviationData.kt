package com.albertsons.acronyms.model

import com.google.gson.annotations.SerializedName

/**
 * Network response data object
 */
class AbbreviationData : ArrayList<AbbreviationData.AbbreviationDataItem>() {
    data class AbbreviationDataItem(
        @SerializedName("sf")
        val sf: String = "",
        @SerializedName("lfs")
        val lfs: List<Lf> = listOf()
    ) {
        data class Lf(
            @SerializedName("lf")
            val lf: String = "",
            @SerializedName("freq")
            val freq: Int = 0,
            @SerializedName("since")
            val since: Int = 0,
            @SerializedName("vars")
            val vars: List<Var> = listOf()
        ) {
            data class Var(
                @SerializedName("lf")
                val lf: String = "",
                @SerializedName("freq")
                val freq: Int = 0,
                @SerializedName("since")
                val since: Int = 0
            )
        }
    }
}