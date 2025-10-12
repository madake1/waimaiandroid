package com.madake.waimai.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.madake.waimai.R
import com.madake.waimai.db.entity.Category

class CategoryAdapter(
    private val categories: MutableList<Category>,
    private val onItemClick: (category: Category, position: Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryVH>() {

    inner class CategoryVH(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tv_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryVH(view)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val category = categories[position]

        // 设置文字和颜色
        holder.textView.text = category.name
        holder.textView.setTextColor(
            if (category.isSelected)
                ContextCompat.getColor(holder.itemView.context, R.color.blue)
            else
                ContextCompat.getColor(holder.itemView.context, R.color.gray2)
        )

        // 点击事件
        holder.itemView.setOnClickListener {
            // 重置所有分类选中状态
            categories.forEach { it.isSelected = false }
            category.isSelected = true
            notifyDataSetChanged() // 刷新列表
            onItemClick(category, position) // 回调
        }
    }

    override fun getItemCount(): Int = categories.size
}
