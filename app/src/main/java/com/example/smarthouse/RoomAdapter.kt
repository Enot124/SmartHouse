package com.example.smarthouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.databinding.RoomItemBinding

class RoomAdapter : RecyclerView.Adapter<RoomAdapter.RoomHolder>() {
    val roomList = ArrayList<Room>()
    class RoomHolder(item : View) : RecyclerView.ViewHolder(item){
        val binding = RoomItemBinding.bind(item)
        fun bind(room : Room) = with(binding){
            ImageRoom.setImageResource(room.imageId)
            RoomName.text = room.name
            LightCount.text = room.stateValue
            DevicesCount.text = room.devicesCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return RoomHolder(view)
    }

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {
        holder.bind(roomList[position])
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    fun addRoom(room : Room){
        roomList.add(room)
        notifyDataSetChanged()
    }

    fun removeRoom(room: Room){
        roomList.remove(room)
        notifyDataSetChanged()
    }
}