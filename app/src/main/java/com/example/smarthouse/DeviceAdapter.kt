package com.example.smarthouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthouse.databinding.DeviceItemBinding
import kotlinx.android.synthetic.main.device_item.*
import java.util.*
import kotlin.collections.ArrayList

class DeviceAdapter(val listener : ListenerDevice) : RecyclerView.Adapter<DeviceAdapter.DeviceHolder>(){
    var deviceList = ArrayList<Device>()
    class DeviceHolder(item : View) : RecyclerView.ViewHolder(item) {
        private val binding = DeviceItemBinding.bind(item)
        fun bind(device: Device, listener: ListenerDevice) = with(binding) {
            btnRemoveDevice.setOnClickListener{
                listener.OnClick(device)
            }
            swDevice.setOnCheckedChangeListener { buttonView, isChecked ->
                listener.OnSwitch(device, isChecked)
            }
            sbDevice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    listener.OnProgress(device,progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {listener.save(device)}
            })
            imgSensorState.setOnClickListener{
                listener.SensorClick(device)
            }

            imgGerkonState.setOnClickListener{
                listener.GerkonClick(device)
            }

            when(device.type)
            {
                0 -> {
                    swDevice.visibility = View.VISIBLE
                    sbDevice.visibility = View.GONE
                    imgSensorState.visibility = View.GONE
                    imgGerkonState.visibility = View.GONE
                }
                1 -> {
                    swDevice.visibility = View.GONE
                    sbDevice.visibility = View.VISIBLE
                    imgSensorState.visibility = View.GONE
                    imgGerkonState.visibility = View.GONE
                }
                2 -> {
                    swDevice.visibility = View.GONE
                    sbDevice.visibility = View.GONE
                    imgSensorState.visibility = View.VISIBLE
                    imgGerkonState.visibility = View.GONE
                    if(device.state)
                        imgSensorState.setImageResource(R.drawable.sensor_state_on)
                    else
                        imgSensorState.setImageResource(R.drawable.sensor_state_off)
                }
                3 -> {
                    swDevice.visibility = View.GONE
                    sbDevice.visibility = View.GONE
                    imgSensorState.visibility = View.GONE
                    imgGerkonState.visibility = View.VISIBLE
                    if(device.state)
                        imgGerkonState.setImageResource(R.drawable.gerkon_state_on)
                    else
                        imgGerkonState.setImageResource(R.drawable.gerkon_state_off)
                }
            }
            imgDevice.setImageResource(device.imageId)
            tvDeviceName.text = device.name
            tvdRoomName.text = device.roomName
            swDevice.text = device.stateValue
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
        fun OnClick(device: Device)
        fun OnSwitch(device: Device,state:Boolean)
        fun OnProgress(device: Device,progress: Int)
        fun SensorClick(device: Device)
        fun GerkonClick(device: Device)
        fun save(device: Device)
    }
}