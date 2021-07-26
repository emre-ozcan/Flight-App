package com.emreozcan.flightapp.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun <T : RecyclerView.ViewHolder?> RecyclerView.setupRecyclerView(
    adapter: RecyclerView.Adapter<T>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
        this.context
    )
) {
    this.adapter = adapter
    this.layoutManager = layoutManager
}
