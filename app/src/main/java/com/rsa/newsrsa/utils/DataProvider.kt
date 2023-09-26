package com.rsa.newsrsa.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.rsa.newsrsa.api.mangers.NewsApiManager
import com.rsa.newsrsa.data.db.AppDataBase
import com.rsa.newsrsa.data.db.dao.NewsDao
import com.rsa.newsrsa.di.NewsPagingSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DataProvider @Inject constructor(
    private val newsApiManager: NewsApiManager,
) : ContentProvider() {
    private lateinit var database: AppDataBase
    private lateinit var newsDao: NewsDao
    private lateinit var newsPagingSource: NewsPagingSource
    companion object{
        private const val PROVIDER_NAME= "com.rsa.newrsa.provider"
        private const val URL = "content://$PROVIDER_NAME/Article"
        val CONTENT_URI : Uri = Uri.parse(URL)
        private var uriMatcher: UriMatcher? = null
        private val values: HashMap<String, String>? = null
        private const val ARTICLE_READLATER_CODE = 1
        private const val ARTICLE_ITEMCODE = 2
        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher!!.addURI(
                PROVIDER_NAME,
                "Article",
                ARTICLE_READLATER_CODE
            )
            uriMatcher!!.addURI(
                PROVIDER_NAME,
                "Article/*",
                ARTICLE_ITEMCODE
            )
        }
    }

    override fun onCreate(): Boolean {
        database = AppDataBase.getDatabase(context = context!!)
        newsDao = database.articleDao()
        newsPagingSource = NewsPagingSource(newsApiManager,"in")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher?.match(uri)) {
//            ARTICLE_READLATER_CODE -> {
//                return Util.pagingSourceToCursor(newsPagingSource)
//            }

//            ARTICLE_ITEMCODE -> {
//                domainDao.selectById(uri.lastPathSegment!!.toInt())
//            }

            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}