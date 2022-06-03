package com.example.smarthouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.databinding.DeviceItemBinding

class DeviceAdapter(val listener : ListenerDevice) : RecyclerView.Adapter<DeviceAdapter.DeviceHolder>(){
    private val deviceList = ArrayList<Device>()
    class DeviceHolder(item : View) : RecyclerView.ViewHolder(item){
        private val binding = DeviceItemBinding.bind(item)
        fun bind(device : Device, listener: ListenerDevice) = with(binding){
            /*ImageRoom.setImageResource(room.imageId)
            RoomName.text = room.name
            LightCount.text = room.stateValue
            DevicesCount.text = room.devicesCount.toString()
            btnRemoveRoom.setOnClickListener{
                listener.OnClick(device)
            }*/
            imgDevice.setImageResource(device.imageId)
            tvDeviceName.text = device.name
            tvdRoomName.text = device.roomName
            swDevice.setOnClickListener{
                listener.OnSwitch(device)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        return DeviceHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceHolder, position: Int) {
        holder.bind(deviceList[position], listener)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    fun addRoom(device: Device){
        deviceList.add(device)
        notifyDataSetChanged()
    }

    fun removeRoom(device: Device){
        deviceList.remove(device)
        notifyDataSetChanged()
    }

    interface ListenerDevice{
        fun OnSwitch(device: Device)
    }
}