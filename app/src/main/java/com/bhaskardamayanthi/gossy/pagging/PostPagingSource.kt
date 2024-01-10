package com.bhaskardamayanthi.gossy.pagging

//import android.nfc.tech.MifareUltralight.PAGE_SIZE
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.bhaskardamayanthi.gossy.model.PostModel
//import com.bhaskardamayanthi.gossy.viewModel.AnonymousPostViewModel
//
//class PostPagingSource(private val viewModel: AnonymousPostViewModel) : PagingSource<Int, PostModel>() {
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostModel> {
//        val nextPageNumber = params.key ?: 0
//
//        return try {
//            val data = fetchDataFromViewModel(nextPageNumber)
//            LoadResult.Page(
//                data = data,
//                prevKey = if (nextPageNumber == 0) null else nextPageNumber - 1,
//                nextKey = nextPageNumber + 1
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    private suspend fun fetchDataFromViewModel(pageNumber: Int): List<PostModel> {
//        // Fetch data from the ViewModel's LiveData
//        // Make sure to handle asynchronous calls properly
//       // return viewModel.
//    }
//
//
//    override fun getRefreshKey(state: PagingState<Int, PostModel>): Int? {
//
//            // Return the key required for refreshing the data
//            // This key is used to invalidate the data when a new load is needed
//
//            // Example: Return the key of the first item in the currently loaded data,
//            // or null if the list is empty
//            return state.anchorPosition?.let { anchorPosition ->
//                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//            }
//
//
//    }
//}
