package com.example.smarthouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.databinding.DeviceItemBinding

class DeviceAdapter(val listener : ListenerDevice) : RecyclerView.Adapter<DeviceAdapter.DeviceHolder>(){
    var deviceList = ArrayList<Device>()
    class DeviceHolder(item : View) : RecyclerView.ViewHolder(item){
        private val binding = DeviceItemBinding.bind(item)
        fun bind(device : Device, listener: ListenerDevice) = with(binding){
            imgDevice.setImageResource(device.imageId)
            tvDeviceName.text = device.name
            tvdRoomName.text = device.roomName
            swDevice.setOnCheckedChangeListener{buttonView, isChecked ->
                listener.OnSwitch(device,isChecked)
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

    fun addDevice(device: Device){
        deviceList.add(device)
        notifyDataSetChanged()
    }

    fun removeDevice(device: Device){
        deviceList.remove(device)
        notifyDataSetChanged()
    }

    interface ListenerDevice{
        fun OnSwitch(device: Device,state:Boolean)
    }
}