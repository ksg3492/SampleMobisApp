package com.sunggil.samplemobisapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sunggil.samplemobisapp.BR
import com.sunggil.samplemobisapp.data.model.base.BaseItem
import com.sunggil.samplemobisapp.data.model.genie.GenieItem
import com.sunggil.samplemobisapp.data.model.melon.MelonItem
import com.sunggil.samplemobisapp.databinding.ItemChartGenieBinding
import com.sunggil.samplemobisapp.databinding.ItemChartMelonBinding
import com.sunggil.samplemobisapp.ui.base.AdapterClickListener
import java.util.*

class StreamingAdapter(var clickListener : AdapterClickListener) : RecyclerView.Adapter<StreamingAdapter.AdapterViewHolder>() {
    private var datas = ArrayList<BaseItem>()

    val VIEW_TYPE_MELON = 0
    val VIEW_TYPE_GENIE = 1

    var currentPage = 1

    fun update(newData : ArrayList<BaseItem>) {
        val beforeSize = datas.size
        if (newData != null && newData.size > 0) {
            if (datas.size > 0) {
                currentPage++
            } else {
                datas.clear()
                currentPage = 1
            }

            datas.addAll(newData)
        } else {
            datas.clear()
        }

        notifyItemRangeChanged(beforeSize, newData.size)
    }

    val getList = datas

    override fun getItemViewType(position: Int): Int {
        if (datas.get(position) is MelonItem) {
            return VIEW_TYPE_MELON
        } else if(datas.get(position) is GenieItem) {
            return VIEW_TYPE_GENIE
        }
        return VIEW_TYPE_MELON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var viewDataBinding : ViewDataBinding

        if (viewType == VIEW_TYPE_MELON) {
            viewDataBinding = ItemChartMelonBinding.inflate(layoutInflater, parent, false)
        } else {
            viewDataBinding = ItemChartGenieBinding.inflate(layoutInflater, parent, false)
        }

        return AdapterViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        when (datas.get(position)) {
            is MelonItem -> {
                holder.bind(datas.get(position))
            }
            is GenieItem -> {
                holder.bind(datas.get(position))
            }

        }

    }

    inner class AdapterViewHolder(var binding : ViewDataBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(obj : BaseItem) {
            if (obj is MelonItem) {
                binding.setVariable(BR.melonItem, obj);
            } else if (obj is GenieItem) {
                binding.setVariable(BR.genieItem, obj);
            }

        }

        override fun onClick(v: View?) {
            clickListener.onClick(adapterPosition)
        }
    }
}