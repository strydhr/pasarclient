package com.strydhr.thepasar.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.strydhr.thepasar.Model.User
import com.strydhr.thepasar.R

class ProfileAdapter(val profile: User, val itemClick:(String)->Unit): RecyclerView.Adapter<ProfileAdapter.Holder>() {
    val FIRST_ITEM = 1
    val SECOND_ITEM = 2
    val THIRD_ITEM = 3
    val FOURTH_ITEM = 4
    val FIFTH_ITEM = 5
    val SIXTH_ITEM = 6

    val size = 4

    inner class Holder(itemView: View, val itemClick: (String) -> Unit): RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.profile_text)
        val icon = itemView.findViewById<ImageView>(R.id.profile_icon)

        fun bindName(profile:User){
            name.text = profile.name
            icon.visibility = View.INVISIBLE

        }
        fun bindmobile(profile: User){
            name.text = profile.phone
            icon.setImageResource(R.drawable.edit)
        }
        fun bindAddress(profile: User){
            val addStr = profile.address?.replace(", ","\n")
            name.text = addStr
            icon.setImageResource(R.drawable.edit)
        }


        fun bindLogout(profile: User){
            name.text = "Log Out"
            icon.setImageResource(R.drawable.logout)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.Holder {
        if (viewType == FIRST_ITEM){
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_profile_item,parent,false)
            return Holder(view,itemClick)
        }else if (viewType == SECOND_ITEM){
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_profile_item,parent,false)
            return Holder(view,itemClick)
        }else if (viewType == THIRD_ITEM){
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_profile_item,parent,false)
            return Holder(view,itemClick)
        }else{
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_profile_item,parent,false)
            return Holder(view,itemClick)
        }

    }

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: ProfileAdapter.Holder, position: Int) {
        when(holder.itemViewType){

            FIRST_ITEM ->{
                holder.bindName(profile)
            }
            SECOND_ITEM ->{
                holder.bindAddress(profile)
                holder.icon.setOnClickListener {
                    itemClick("EDITADDRESS")
                }
            }
            THIRD_ITEM ->{
                holder.bindmobile(profile)
                holder.icon.setOnClickListener {
                    itemClick("EDIT")
                }
            }
            FOURTH_ITEM ->{
                holder.bindLogout(profile)
                holder.itemView.setOnClickListener {
                    itemClick("LOGOUT")
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {

        if (position == 0){
            return FIRST_ITEM

        }else if (position == 1){
            return  SECOND_ITEM
        }else if (position == 2){
            return  THIRD_ITEM
        }else{
            return  FOURTH_ITEM
        }
    }

}