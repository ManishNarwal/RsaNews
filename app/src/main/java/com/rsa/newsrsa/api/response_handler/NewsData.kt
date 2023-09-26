package com.rsa.newsrsa.api.response_handler


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import javax.annotation.Nullable

data class NewsData(
    @SerializedName("articles") val articles: List<Article>,
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int
) {
    @Entity
    data class Article(
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "ID") var id: String,
        @Nullable
        @ColumnInfo(name = "Author") @SerializedName("author") val author: String?,
        @Nullable
        @ColumnInfo(name = "Content") @SerializedName("content") val content: String?,
        @Nullable
        @ColumnInfo(name = "Description") @SerializedName("description") val description: String?,
        @Nullable
        @ColumnInfo(name = "PublishedAt") @SerializedName("publishedAt") val publishedAt: String?,
//        @Ignore
//        @SerializedName("source") val source: Source,
        @Nullable
        @ColumnInfo(name = "Title") @SerializedName("title") val title: String?,
        @Nullable
        @ColumnInfo(name = "Url") @SerializedName("url") val url: String?,
        @Nullable
        @ColumnInfo(name = "ImageUrl") @SerializedName("urlToImage") val urlToImage: String?,
        @ColumnInfo(name = "IsRead") var isRead: Boolean,
        @ColumnInfo(name = "IsDeleted") var isDeleted: Boolean,
        @ColumnInfo(name = "ReadLater") var wantReadLater: Boolean,
        @Nullable
        @ColumnInfo(name = "Country") var country: String?
    ) {
        data class Source(
            @SerializedName("id") val id: String?, @SerializedName("name") val name: String
        )
    }
}