package com.gram.color_android.ui.feed.angry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gram.color_android.R
import com.gram.color_android.network.set.FeedSet
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed_angry.*

class FeedAngryFragment : Fragment() {

    private lateinit var adapter : AngryFeedRVAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val feedViewModel = FeedViewModel()
    private val sharedPrefs = SharedPreferencesHelper.getInstance()
    private var page = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_angry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(activity)
        feed_angry_rv.layoutManager = linearLayoutManager

        bottomSheetDialog = BottomSheetDialog(requireContext())

        getPostList()
        swipeRefresh()

        feedViewModel.feedLiveData.observe(viewLifecycleOwner) {
            when(it){
                FeedSet.GET_SUCCESS -> {
                    adapter = AngryFeedRVAdapter(feedViewModel.feedListLiveData.value!!)
                    feed_angry_rv.adapter = adapter
                    postMore()
                }
            }
        }
    }

    private fun getPostList(){
        feedViewModel.getPostList(sharedPrefs.access_token!!, getPage(), FeelSet.ANGRY.toString())
    }

    private fun getPage(): Int = page++

    private fun swipeRefresh() {
        swipe_refresh.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                page = 0
                getPostList()
                swipe_refresh.isRefreshing = false
            }

        })
    }

    private fun postMore(){
        adapter.setOnClickListener(object: AngryFeedRVAdapter.OnClickListener{
            override fun onBtnClick(v: View, position: Int) {
                bottomSheetDialog.setContentView(R.layout.feed_more_bottomsheet)
                bottomSheetDialog.show()
            }

        })
    }
}