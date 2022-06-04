package com.example.smarthouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.databinding.RoomItemBinding
import kotlinx.android.synthetic.main.room_item.view.*

class RoomAdapter(val listener : Listener) : RecyclerView.Adapter<RoomAdapter.RoomHolder>() {
    val roomList = ArrayList<Room>()
    class RoomHolder(item : View) : RecyclerView.ViewHolder(item){
        private val binding = RoomItemBinding.bind(item)
        fun bind(room : Room, listener: Listener) = with(binding){
            ImageRoom.setImageResource(room.imageId)
            RoomName.text = room.name
            LightCount.text = room.stateValue
            DevicesCount.text = room.devicesCount.toString()
            btnRemoveRoom.setOnClickListener{
                listener.OnClick(room)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return RoomHolder(view)
    }

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {
        holder.bind(roomList[position], listener)
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

    interface Listener{
        fun OnClick(room: Room)
    }
}