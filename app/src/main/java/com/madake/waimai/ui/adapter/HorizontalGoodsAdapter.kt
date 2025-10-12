package com.madake.waimai.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.madake.waimai.R
import com.madake.waimai.db.entity.GoodsEntity
import com.madake.waimai.utils.load

class HorizontalGoodsAdapter(private val list: MutableList<GoodsEntity>) : RecyclerView.Adapter<HorizontalGoodsAdapter.GoodsViewHolder>() {

    fun updateData(newList: List<GoodsEntity>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class GoodsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.iv_goods_image)
        val tvName: TextView = itemView.findViewById(R.id.tv_goods_name)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price_now)
        val tvSales: TextView = itemView.findViewById(R.id.tv_sales_volume)
        val tvBillion: TextView = itemView.findViewById(R.id.tv_billion_subsidy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goods, parent, false)
        return GoodsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        val item = list[position]
        holder.tvName.text = item.name
        holder.tvPrice.text = "¥${String.format("%.2f", item.price)}"
        holder.tvSales.text = "销量：${item.salesVolume}"
        holder.tvBillion.visibility = if (item.isBillionSubsidy) View.VISIBLE else View.GONE
        item.imageUrl?.let { holder.ivImage.load(it) }
        Log.d("madake1",item.imageUrl.toString())

    }

    override fun getItemCount() = list.size
}
