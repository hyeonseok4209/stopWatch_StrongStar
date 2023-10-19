package com.example.stopwatch

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatch.model.History


class HistoryAdapter(val historyList: MutableList<History>) : RecyclerView.Adapter<HistoryAdapter.CustomViewHolder>() {

    private val TAG = "HistoryAdapter"



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
    override fun onBindViewHolder(holder: HistoryAdapter.CustomViewHolder, position: Int) {
        holder.text.setText(historyList.get(position).recode.toString())

        holder.itemView.findViewById<Button>(R.id.btn_delete).setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    fun removeItem(position: Int) {
        historyList.removeAt(position)
        notifyItemRemoved(position)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener



    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text = itemView.findViewById<TextView>(R.id.tv_history)
    }

}


