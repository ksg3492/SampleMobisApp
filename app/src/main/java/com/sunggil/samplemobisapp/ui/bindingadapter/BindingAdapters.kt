package com.sunggil.samplemobisapp.ui.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sunggil.samplemobisapp.data.model.base.BaseItem
import com.sunggil.samplemobisapp.ui.adapter.StreamingAdapter
import com.sunggil.samplemobisapp.ui.custom.BlurTransformation

object BindingAdapters {
        @JvmStatic
        @BindingAdapter("thumb")
        fun setImageUrl(view : ImageView, url : String?) {
            Glide.with(view.context)
                .load(url)
                .into(view)
        }

        @JvmStatic
        @BindingAdapter("thumbBG")
        fun setBackgroundImageUrl(view : ImageView, url : String?) {
            Glide.with(view.context)
                .load(url)
                .apply(
                    RequestOptions.bitmapTransform(BlurTransformation(1, 100)
                ))
                .into(view)
        }

//        @JvmStatic
//        @BindingAdapter("listdata")
//        fun updateDatas(recyclerView : RecyclerView, data : ObservableArrayList<MelonItem>?) {
//            val adapter = recyclerView.adapter as MelonAdapter
//            adapter.update(data)
//        }

        @JvmStatic
        @BindingAdapter("items")
        fun setItems(recyclerView : RecyclerView, data : ArrayList<BaseItem>?) {
            val adapter = recyclerView.adapter as StreamingAdapter
            if (data != null) {
                adapter.update(data)
            }
        }
}