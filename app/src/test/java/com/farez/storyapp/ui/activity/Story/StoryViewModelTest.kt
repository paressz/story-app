package com.farez.storyapp.ui.activity.Story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.farez.storyapp.data.local.preferences.LoginPreferences
import com.farez.storyapp.data.remote.response.Story
import com.farez.storyapp.data.repository.StoryRepository
import com.farez.storyapp.util.DataDummy
import com.farez.storyapp.util.MainDispatcherRule
import com.farez.storyapp.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    @Mock private lateinit var repo : StoryRepository
    @Mock private lateinit var loginPreferences: LoginPreferences
    private lateinit var storyVM : StoryViewModel
    private val dummy = DataDummy.generateDummy()
    val differ = AsyncPagingDataDiffer(
        diffCallback = StoryPagedAdapter.DIFF_CALLBACK,
        updateCallback = noopListUpdateCallback,
        workerDispatcher = Dispatchers.Main,
    )

    @Before
    fun setup() {
        storyVM = StoryViewModel(repo,loginPreferences)
    }

    @Test
    fun `When Get Story Should Not Null and Return Data`() = runTest {
        val data : PagingData<Story> = StoryPagingSource.snapshot(dummy)
        val expected = MutableLiveData<PagingData<Story>>()
        expected.value = data
        `when`(repo.getStoryWithPaging("token")).thenReturn(expected)
        val actual : PagingData<Story> = storyVM.getStoryWithPaging("token").getOrAwaitValue()
        differ.submitData(actual)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummy.size, differ.snapshot().size)
        Assert.assertEquals(dummy[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expected = MutableLiveData<PagingData<Story>>()
        expected.value = data
        `when`(repo.getStoryWithPaging("token")).thenReturn(expected)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}