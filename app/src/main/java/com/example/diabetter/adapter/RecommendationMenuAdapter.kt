package com.example.diabetter.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diabetter.R
import com.example.diabetter.view.detail_menu.DetailMenuActivity

class RecommendationMenuAdapter(private val itemCount: Int) :
    RecyclerView.Adapter<RecommendationMenuAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Tambahkan referensi ke view di layout recommendation_menu.xml
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommendation_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        val marginFirst = holder.itemView.resources.getDimensionPixelSize(R.dimen.margin_first_item)
        val marginLast = holder.itemView.resources.getDimensionPixelSize(R.dimen.margin_last_item)

        when (position) {
            0 -> layoutParams.leftMargin = marginFirst
            itemCount - 1 -> layoutParams.rightMargin = marginLast
            else -> {}
        }
        holder.itemView.layoutParams = layoutParams
        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, DetailMenuActivity::class.java)
            // Pass any necessary data to DetailMenuActivity
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = itemCount
}

fun setupRecyclerView(
    recyclerView: RecyclerView,
    itemCount: Int,
    onMostVisibleItemChanged: (Int) -> Unit
) {
    val linearLayoutManager =
        LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
    recyclerView.layoutManager = linearLayoutManager
    recyclerView.adapter = RecommendationMenuAdapter(itemCount)

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            findMostVisibleItem(recyclerView, linearLayoutManager, onMostVisibleItemChanged)
        }
    }

    recyclerView.addOnScrollListener(scrollListener)

    recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
        findMostVisibleItem(recyclerView, linearLayoutManager, onMostVisibleItemChanged)
    }
}

fun findMostVisibleItem(
    recyclerView: RecyclerView,
    layoutManager: LinearLayoutManager,
    onMostVisibleItemChanged: (Int) -> Unit
) {
    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

    var maxVisibleWidth = 0
    var mostVisibleItemPosition = -1
    var lastMostVisibleItemPosition = -1

    for (i in firstVisibleItemPosition..lastVisibleItemPosition) {
        val view = layoutManager.findViewByPosition(i) ?: continue

        val leftVisibleWidth = if (view.left >= 0) view.width else view.width + view.left
        val rightVisibleWidth =
            if (view.right <= recyclerView.width) view.width else recyclerView.width - view.left
        val visibleWidth = minOf(leftVisibleWidth, rightVisibleWidth)

        if (visibleWidth > maxVisibleWidth) {
            maxVisibleWidth = visibleWidth
            mostVisibleItemPosition = i
        }
    }

    if (mostVisibleItemPosition != -1 && mostVisibleItemPosition != lastMostVisibleItemPosition) {
        lastMostVisibleItemPosition = mostVisibleItemPosition
        Log.d("RecommendationMenuAdapter", "Most visible item position: $mostVisibleItemPosition")
        onMostVisibleItemChanged(mostVisibleItemPosition)
    }
}
