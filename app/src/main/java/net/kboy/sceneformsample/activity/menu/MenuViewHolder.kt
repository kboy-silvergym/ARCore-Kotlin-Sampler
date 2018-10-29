package net.kboy.sceneformsample.activity.menu

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_list.view.*
import net.kboy.sceneformsample.R

class MenuViewHolder(view: View, private val onClick: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
    private val items: Array<String> = arrayOf("Sceneform", "Augmented Images", "Cloud Anchors")
    private val cardView = view
    private val label = view.label

    fun bind(position: Int) {
        label.text = items[position]
        cardView.setOnClickListener { onClick(position) }
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (Int) -> Unit) = MenuViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_list, parent, false), onClick)
    }

}