package com.sunggil.samplemobisapp.player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.sunggil.samplemobisapp.MyApplication
import com.sunggil.samplemobisapp.data.model.melon.MelonStreamingItem
import java.io.File

class ExoPlayerService : Service {

    private val TAG = "ExoPlayerService"
    private var mBound = false

    //ExoPlayer
    private var MAX_CACHE_FOLDER_SIZE = 100 * 1024 * 1024L    //100Mb, cache folder maximum size
    private var MAX_CACHE_FILE_SIZE = 2 * 1024 * 1024L      //2Mb, cache file maximum size
    private var CACHE_FOLDER_PATH : String? = null

    private var exoPlayer : SimpleExoPlayer? = null
    private var dataSourceFactory : ExoDataSourceFactory? = null
    private var isPrepared = false

    private var playerCallback : PlayerCallback? = null

    companion object {
        private var INSTANCE : ExoPlayerService? = null

        fun getInstance() = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ExoPlayerService().also {
                INSTANCE = it
            }
        }
    }

    constructor() {
        Log.e(TAG,"ExoPlayerService constructor")
    }

    fun customize(folder : Long, file : Long, path : String) {
        MAX_CACHE_FOLDER_SIZE = folder
        MAX_CACHE_FILE_SIZE = file
        CACHE_FOLDER_PATH = path
        if (path.trim().isEmpty()) {
            CACHE_FOLDER_PATH = null
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG,"ExoPlayerService onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.e(TAG,"ExoPlayerService onCreate")
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun setPlayerCallback(callback : PlayerCallback) {
        playerCallback = callback
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"ExoPlayerService onDestroy()")

        try {
            progressHandler.removeMessages(0)
        } catch(e : Exception) {}

        exoPlayer = null
        dataSourceFactory = null
    }

    private fun startProgress(start : Boolean) {
        if (start) {
            progressHandler.removeMessages(0)
            progressHandler.sendEmptyMessage(0)
        } else {
            progressHandler.removeMessages(0)
        }
    }

    private val progressHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            try {
                val progress = exoPlayer?.let { getCurrentPosition().toInt() / 1000 } ?: -1
                playerCallback?.onProgress(progress)
            } catch (e : Exception) {

            }

            removeMessages(0)
            sendEmptyMessageDelayed(0, 500)
        }
    }

    fun prepare(url : String, customKey : String) {
        createPlayer()
        try {
            val mediaSource : MediaSource
            Log.e(TAG,"ExoPlayerService onPrepared url : $url")

            val factory = ExtractorMediaSource.Factory(dataSourceFactory)
            factory.setCustomCacheKey(customKey)
            mediaSource = factory.createMediaSource(Uri.parse(url))

            if (exoPlayer?.playbackState != Player.STATE_IDLE) {
                stop()
            }
            isPrepared = true

            exoPlayer?.run {
                playWhenReady = false
                volume = 1.0f
                prepare(mediaSource)
            }
        } catch (e : Exception) {
            isPrepared = false
            Log.e(TAG,"ExoPlayerService onPrepared : ", e)
        }
    }

    fun prepare(item : MelonStreamingItem) {
        val customKey = item.GETPATHINFO?.run {
            this.CID + this.METATYPE + this.BITRATE + this.PLAYTIME
        }.toString()

        createPlayer()
        try {
            val mediaSource : MediaSource
            Log.e(TAG,"ExoPlayerService onPrepared url : ${item.GETPATHINFO?.PATH}")

            val factory = ExtractorMediaSource.Factory(dataSourceFactory)
            factory.setCustomCacheKey(customKey)
            mediaSource = factory.createMediaSource(Uri.parse(item.GETPATHINFO?.PATH))

            if (exoPlayer?.playbackState != Player.STATE_IDLE) {
                stop()
            }
            isPrepared = true

            exoPlayer?.run {
                playWhenReady = false
                volume = 1.0f
                prepare(mediaSource)
            }
        } catch (e : Exception) {
            isPrepared = false
            Log.e(TAG,"ExoPlayerService onPrepared : ", e)
        }
    }

    private fun createPlayer() {
        if (exoPlayer == null) {
            dataSourceFactory = ExoDataSourceFactory(MyApplication.getApplicationContext())
            exoPlayer = ExoPlayerFactory.newSimpleInstance(MyApplication.getApplicationContext(),
                DefaultRenderersFactory(MyApplication.getApplicationContext()),
                DefaultTrackSelector(),
                DefaultLoadControl())
            exoPlayer!!.addListener(eventListener)
        }
    }

    private val eventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            Log.e(TAG,"onPlayerStateChanged playWhenReady : $playWhenReady")

            if (playbackState == Player.STATE_IDLE) {
                Log.e(TAG,"onPlayerStateChanged playbackState : STATE_IDLE")
            } else if (playbackState == Player.STATE_BUFFERING) {
                Log.e(TAG,"onPlayerStateChanged playbackState : STATE_BUFFERING")
                playerCallback?.onBuffering()
            } else if (playbackState == Player.STATE_READY) {
                Log.e(TAG,"onPlayerStateChanged playbackState : STATE_READY")

                if (isPrepared) {
                    isPrepared = false
                    exoPlayer?.playWhenReady = true
                    startProgress(true)
                    val duration = exoPlayer?.let { it.duration / 1000 } ?: 0

                    playerCallback?.onPrepared(duration.toInt())
                } else {
                    if (playWhenReady) {
                        startProgress(true)
                        playerCallback?.onPlayed()
                    } else {
                        startProgress(false)
                        playerCallback?.onPaused()
                    }
                }

            } else if (playbackState == Player.STATE_ENDED) {
                Log.e(TAG,"onPlayerStateChanged playbackState : STATE_ENDED")
                startProgress(false)
                playerCallback?.onCompletion()
                //complete
            }

        }

        override fun onPlayerError(error: ExoPlaybackException) {
            startProgress(false)
            playerCallback?.onError(error)
            Log.e(TAG,"onPlayerError : ", error)
        }

        override fun onPlaybackParametersChanged(p0: PlaybackParameters?) {
        }

        override fun onSeekProcessed() {
        }

        override fun onTracksChanged(p0: TrackGroupArray?, p1: TrackSelectionArray?) {
        }

        override fun onLoadingChanged(p0: Boolean) {
        }

        override fun onPositionDiscontinuity(p0: Int) {
        }

        override fun onRepeatModeChanged(p0: Int) {
        }

        override fun onShuffleModeEnabledChanged(p0: Boolean) {
        }

        override fun onTimelineChanged(p0: Timeline?, p1: Any?, p2: Int) {
        }
    }

    inner class ExoDataSourceFactory(private val context: Context) : DataSource.Factory {
        private val defaultDatasourceFactory : DefaultDataSourceFactory =
            DefaultDataSourceFactory(context,
                DefaultBandwidthMeter(),
                DefaultHttpDataSourceFactory(Util.getUserAgent(context, context.applicationInfo.name),null))
        private var simpleCache : SimpleCache? = null

        override fun createDataSource(): DataSource {
            val evictor = LeastRecentlyUsedCacheEvictor(MAX_CACHE_FOLDER_SIZE)

            if (simpleCache == null) {
                val path = CACHE_FOLDER_PATH ?: Environment.getExternalStorageDirectory().toString() + "/${context.applicationInfo.loadLabel(context.packageManager)}/"
                simpleCache = SimpleCache(File(path, "cache"), evictor)
            }

            return CacheDataSource(simpleCache,
                defaultDatasourceFactory.createDataSource(),
                FileDataSource(),
                CacheDataSink(simpleCache, MAX_CACHE_FILE_SIZE),
                (CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR),
                null)
        }

        fun release() {
            try {
                simpleCache?.release()
                simpleCache = null
            } catch (e : Exception) { }
        }
    }

    fun getCurrentPosition() : Long {
        exoPlayer?.let {
            return it.currentPosition
        }

        return 0
    }

    fun isPlaying() : Boolean {
        exoPlayer?.let { return it.playbackState == Player.STATE_READY && it.playWhenReady }

        return false
    }

    fun play() {
        exoPlayer?.playWhenReady = true
        startProgress(true)
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
        startProgress(false)
    }

    fun seekTo(sec : Int, wasPlaying : Boolean) {
        exoPlayer?.run {
            if (playbackState != Player.STATE_READY) {
                return
            }

            val max = exoPlayer!!.duration / 1000
            if (sec >= max) {
                stop()
                playerCallback?.onCompletion()
            } else {
                seekTo(sec * 1000L)
                if (!playWhenReady && wasPlaying) {
                    playWhenReady = true
                } else {}
            }
        }
    }

    fun stop() {
        startProgress(false)

        try {
            exoPlayer?.stop(true)
        } catch (e : Exception) {
            Log.e(TAG,"ExoPlayerService setStop Error : ", e)
        }
    }

    fun release() {
        try {
            startProgress(false)

            exoPlayer?.run {
                removeListener(eventListener)
                if (playWhenReady) {
                    volume = 0.0f
                    playWhenReady = false
                }
                release()
            }
            exoPlayer = null
        } catch (e : Exception) {
            Log.e(TAG,"ExoPlayerService release : ", e)
        }

        try {
            dataSourceFactory?.release()
            dataSourceFactory = null
        } catch (e : Exception) {
            Log.e(TAG,"ExoPlayerService release : ", e)
        }
    }
}