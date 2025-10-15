package com.madake.waimai.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.madake.waimai.R
import com.madake.waimai.db.entity.GoodsEntity
import com.madake.waimai.utils.load

class ProductAdapter(private val list: MutableList<GoodsEntity>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    fun updateData(newList: List<GoodsEntity>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivImage)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvSubsidy: TextView = view.findViewById(R.id.tvSubsidy)
        val tvSales: TextView = view.findViewById(R.id.tvSales)
        val tvDiscount: TextView = view.findViewById(R.id.tvDiscount)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvOriginalPrice: TextView = view.findViewById(R.id.tvOriginalPrice)
        val tvCoupon: TextView = view.findViewById(R.id.tvCoupon)
        val xyhf: ViewGroup = view.findViewById(R.id.xyhf)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvName.text = item.name
        holder.tvSales.text = "热销 ${item.salesVolume} 件"

        // 百亿补贴显示
        holder.tvSubsidy.visibility = if (item.isBillionSubsidy) View.VISIBLE else View.GONE

        // 折扣显示
        if (item.isDiscounted && item.zhekou < 1.0) {
            holder.tvDiscount.visibility = View.VISIBLE
            holder.tvDiscount.text = "${(item.zhekou * 10).toString().take(3)}折"
        } else {
            holder.tvDiscount.visibility = View.GONE
        }

        // 先用后付
        holder.xyhf.visibility = if (item.isPostPay) View.VISIBLE else View.GONE

        // 价格
        holder.tvPrice.text = "￥${String.format("%.0f", item.price * item.zhekou)}"
        holder.tvOriginalPrice.text = "￥${String.format("%.0f", item.price)}"
        holder.tvOriginalPrice.paint.isStrikeThruText = true

        // 优惠
        if (item.sellerDiscount > 0) {
            holder.tvCoupon.visibility = View.VISIBLE
            holder.tvCoupon.text = "限时减免${String.format("%.0f", item.sellerDiscount)}元"
        } else {
            holder.tvCoupon.visibility = View.GONE
        }

        // 图片

        item.imageUrl?.let { holder.ivImage.load(it) }
    }

}
