package net.kboy.sceneformsample.activity.menu

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class MenuAdapter(private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<MenuViewHolder>() {

    override fun getItemCount() = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder =
            MenuViewHolder.create(parent, onItemClick)

    override fun onBindViewHolder(holder: MenuViewHolder?, position: Int) {
        holder?.bind(position)
    }
}