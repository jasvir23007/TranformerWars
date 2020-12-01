package com.transformers.test.screens.transformers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.transformers.test.R
import com.transformers.test.models.transformers.Transformer


class TransformersAdapter(
    val context: Context,
    list: ArrayList<Transformer>,
    val deleteCallback: (String) -> Unit,
    val editCallback: (Transformer) -> Unit
) :
    RecyclerView.Adapter<TransformersAdapter.TransformersFragViewHolder>() {

    var mItemList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformersFragViewHolder {
        return TransformersFragViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.transformer_list_view_item,
                parent,
                false
            )
        )
    }

    fun updateListItems(updatedList: ArrayList<Transformer>) {
        mItemList.clear()
        mItemList = updatedList
        notifyDataSetChanged()
    }

    fun addListItems(updatedList: ArrayList<Transformer>) {
        mItemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holder: TransformersFragViewHolder, position: Int) {
        val model: Transformer = mItemList[position]
        holder.title.text = model.name
        holder.team.text = "${model.getTeamFormatted()}"
        holder.rank.text = "Rank: ${model.rank}"
        holder.powers.text =
                "Strength: ${model.strength} \n" +
                "Courage: ${model.courage}\n" +
                "Endurance: ${model.endurance}\n" +
                "Firepower: ${model.firepower}\n" +
                "Intelligence: ${model.intelligence}\n" +
                "Speed: ${model.speed}\n" +
                "Skill: ${model.skill}\n"
                Glide.with(context).load(model.teamIcon).into(holder.photo)
        holder.delete.setOnClickListener {
            deleteCallback.invoke(model.id)
        }
        holder.edit.setOnClickListener {
            editCallback.invoke(model)
        }
    }

    class TransformersFragViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView = item.findViewById(R.id.title)
        val powers: TextView = item.findViewById(R.id.powers)
        val team: TextView = item.findViewById(R.id.team)
        val rank: TextView = item.findViewById(R.id.rank)
        val photo: ImageView = item.findViewById(R.id.photo)
        val delete: ImageView = item.findViewById(R.id.delete)
        val edit: ImageView = item.findViewById(R.id.edit)
    }
}