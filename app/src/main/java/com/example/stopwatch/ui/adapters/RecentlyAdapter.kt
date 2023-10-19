package com.example.stopwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecentlyAdapter(val recentlyList: MutableList<Recently>) : RecyclerView.Adapter<RecentlyAdapter.CustomViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentlyAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recently_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recentlyList.size
    }
    override fun onBindViewHolder(holder: RecentlyAdapter.CustomViewHolder, position: Int) {
        holder.text.setText(recentlyList.get(position).record.toString())
    }

    fun removeItem(position: Int) {
        recentlyList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        recentlyList.clear()
        notifyDataSetChanged()
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text = itemView.findViewById<TextView>(R.id.tv_history)
    }

}


