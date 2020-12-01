package com.transformers.test.screens.transformers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.transformers.test.R
import com.transformers.test.models.transformers.Transformer


class BattleAdapter(
    val context: Context,
    list: ArrayList<Pair<Transformer, Transformer>>
) :
    RecyclerView.Adapter<BattleAdapter.TransformersFragViewHolder>() {

    var mItemList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformersFragViewHolder {
        return TransformersFragViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.battle_list_view_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holder: TransformersFragViewHolder, position: Int) {
        val model: Pair<Transformer, Transformer> = mItemList[position]
        if (!model.first.winner && !model.second.winner){
            holder.winnerText.text = "DESTROYED"
            holder.winnerText.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            holder.loserText.text = "DESTROYED"
            holder.loserText.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        }
        holder.winner.text = model.first.name
        holder.loser.text = model.second.name
        Glide.with(context).load(model.first.teamIcon).into(holder.winnerPhoto)
        Glide.with(context).load(model.second.teamIcon).into(holder.loserPhoto)
    }

    class TransformersFragViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val winner: TextView = item.findViewById(R.id.winner)
        val loser: TextView = item.findViewById(R.id.loser)
        val winnerText: TextView = item.findViewById(R.id.winnerText)
        val loserText: TextView = item.findViewById(R.id.loserText)
        val winnerPhoto: ImageView = item.findViewById(R.id.winnerPhoto)
        val loserPhoto: ImageView = item.findViewById(R.id.loserPhoto)
    }
}