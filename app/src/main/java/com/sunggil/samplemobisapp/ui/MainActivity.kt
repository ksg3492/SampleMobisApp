package com.sunggil.samplemobisapp.ui

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.SeekBar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunggil.samplemobisapp.BR
import com.sunggil.samplemobisapp.R
import com.sunggil.samplemobisapp.data.model.genie.GenieItem
import com.sunggil.samplemobisapp.data.model.melon.MelonItem
import com.sunggil.samplemobisapp.data.model.melon.MelonPlayingItem
import com.sunggil.samplemobisapp.databinding.ActivityMainBinding
import com.sunggil.samplemobisapp.player.ExoPlayerService
import com.sunggil.samplemobisapp.player.PlayerCallback
import com.sunggil.samplemobisapp.ui.adapter.StreamingAdapter
import com.sunggil.samplemobisapp.ui.base.AdapterClickListener
import com.sunggil.samplemobisapp.ui.base.BaseActivity
import com.sunggil.samplemobisapp.vm.MainActivityVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main_player.view.*

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityVM>() {
    lateinit var adapterStreaming : StreamingAdapter

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel(): MainActivityVM {
        return MainActivityVM(application)
    }

    override fun bindingLiveData() {
//        val chartDatas = Observer<ArrayList<MelonItem>> {
//            adapterMelon.update(it)
//        }
//
//        getViewModel().listdatas.observe(this, chartDatas)
    }

    override fun initView() {
        getDataBinding().setVariable(BR.playingItem, MelonPlayingItem.getInstance())
        getDataBinding().setVariable(BR.vm, getViewModel())

        startService()

        adapterStreaming = StreamingAdapter(adapterClickListener)

        recyclerView.adapter = adapterStreaming
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = manager.findLastCompletelyVisibleItemPosition()

                    //마지막뷰
                    if (lastPosition == adapterStreaming.getList.size - 1) {
                        Log.e("SG2","마지막 리스트 요청")
                        getViewModel().getGeineRealTime(adapterStreaming.currentPage + 1)
                    }
                }

                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        bt_list.setOnClickListener {
            getViewModel().getMelonHit24()
        }

        bt_list2.setOnClickListener {
            getViewModel().getGeineRealTime(adapterStreaming.currentPage)
        }

        playerView.bt_playpause.setOnClickListener {
            getViewModel().playpause()
        }

        playerView.sb_player.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var wasPlaying = false

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                wasPlaying = getViewModel().isPlaying()
                getViewModel().pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    getViewModel().seek(seekBar.progress, wasPlaying)
                }
            }

        })
    }

    private fun startService() {
        if (ExoPlayerService.getInstance() != null) {
            val playerService = Intent(this, ExoPlayerService::class.java)
            startService(playerService)

            ExoPlayerService.getInstance().setPlayerCallback(playerCallback)
        }
    }

    private val adapterClickListener : AdapterClickListener = object : AdapterClickListener {
        override fun onClick(pos: Int) {
            adapterStreaming.getList[pos].let {
                if (it is MelonItem) {
                    playerView.tv_player_title.isSelected = true
                    MelonPlayingItem.getInstance().title = it.songName
                    MelonPlayingItem.getInstance().subTitle = it.artistName
                    MelonPlayingItem.getInstance().progress = 0
                    MelonPlayingItem.getInstance().max = 100
                    MelonPlayingItem.getInstance().thumbnail = it.albumImgLarge
                    MelonPlayingItem.getInstance().thumbnailBG = it.albumImgLarge

                    getViewModel().getStreamingPath(it)
                } else if (it is GenieItem) {

                }
            }
        }
    }

    private val playerCallback = object : PlayerCallback {
        override fun onPrepared(duration: Int) {
            Log.e("SG2", "MainActivity onPrepared $duration")

            MelonPlayingItem.getInstance().progress = 0
            MelonPlayingItem.getInstance().max = duration
        }

        override fun onBuffering() {
        }

        override fun onPlayed() {
        }

        override fun onPaused() {
        }

        override fun onProgress(sec: Int) {
            Log.e("SG2", "MainActivity onProgress $sec")
            MelonPlayingItem.getInstance().progress = sec

        }

        override fun onCompletion() {
            MelonPlayingItem.getInstance().progress = playerView.sb_player.max
        }

        override fun onError(errMsg: Exception) {
            Log.e("SG2", "MainActivity onError $errMsg")
        }

    }
}