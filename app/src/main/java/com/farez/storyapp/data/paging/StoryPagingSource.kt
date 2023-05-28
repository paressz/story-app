package com.farez.storyapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.farez.storyapp.api.ApiService
import com.farez.storyapp.data.remote.response.Story

class StoryPagingSource (private val apiService: ApiService, private val token: String) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStory(token, position, params.loadSize)
            val listStory = responseData.body()!!.listStory as MutableList<Story>
            Log.d("check list", "check story: ${listStory.size}")
            LoadResult.Page(
                data = listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if ((responseData.body()!!.listStory as MutableList<Story>).isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}