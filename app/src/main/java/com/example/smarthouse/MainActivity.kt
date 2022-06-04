package com.example.smarthouse

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthouse.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.device_dialog.*
import kotlinx.android.synthetic.main.device_dialog.view.*
import kotlinx.android.synthetic.main.device_item.*
import kotlinx.android.synthetic.main.room_item.*


class MainActivity : AppCompatActivity() , RoomAdapter.Listener, DeviceAdapter.ListenerDevice{
    lateinit var binding: ActivityMainBinding
    private var  adapter = RoomAdapter(this)
    private var devAdapter = DeviceAdapter(this)
    var notification: Boolean = true
    lateinit var mDataBase : DatabaseReference
    private var USER_KEY = "Room"
    private var defId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY)

        binding.MenuView.setOnClickListener{
            ViewMenu()
        }

        binding.Notification.setOnClickListener{
            SetNotification()
        }
        binding.AddRoom.setOnClickListener{
            CreateRoom()
        }

        binding.House.setOnClickListener{
            GoHome()
        }

        binding.Room.setOnClickListener{
            GoRoom()
        }

        binding.btnAddDevice.setOnClickListener{
            CreateDevice()
        }
    }

    private fun init(){
        binding.apply {
            rcvRoom.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvRoom.adapter = adapter
            rcvDevice.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvDevice.adapter = devAdapter
        }
    }

    fun CreateRoom()
    {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Окно редактирования комнаты")
        builder.setPositiveButton("OK"){ dialogInterface , which ->
            var dialog = dialogInterface as AlertDialog
            var editText = dialog.findViewById<EditText>(R.id.EdText)
            val text = editText?.text.toString()
            var imageId: Int
            imageId = when (text) {
                "Гостиная","Hall" -> R.drawable.bed2
                "Кухня","Kitchen" -> R.drawable.kitchen
                "Ванная","Bathroom" -> R.drawable.bath
                "Спальня","Bedroom"-> R.drawable.bed1
                "Детская","Childroom" -> R.drawable.child
                else -> R.drawable.house

            }
            val room = Room(defId++.toString(),text,imageId,false,"OFF", 0)
            var count = room.Devices?.size
            if (count != null) {
                room.devicesCount = count.toInt()
            }
            else count = 0
            room.devicesCount = count
            mDataBase.push().setValue(room)
            adapter.addRoom(room)
        }

        builder.setNeutralButton("Назад"){ dialogInterface , which ->
        }
        var cl = layoutInflater.inflate(R.layout.custom_dialog, null)

        builder.setView(cl)
        builder.show()
    }

    fun CreateDevice(){

        var builder = AlertDialog.Builder(this)
        builder.setTitle("Окно редактирования устройства")
        builder.setPositiveButton("OK"){ dialogInterface , which ->
            var dialog = dialogInterface as AlertDialog
            var editText = dialog.findViewById<EditText>(R.id.edDeviceName)
            val text = editText?.text.toString()
            val selectedRadioButton = dialog.findViewById<RadioButton>(dialog.rgRoomName.checkedRadioButtonId)
            val position = dialog.rgRoomName.indexOfChild(selectedRadioButton)
            val room = adapter.roomList[position]
            val device = Device(0,text,room.name, R.drawable.device_off, false)
            room.Devices?.add(device)
            devAdapter.addDevice(device)
            mDataBase.push().setValue(device)
        }

        builder.setNeutralButton("Назад"){ dialogInterface , which ->
        }
        var cl = layoutInflater.inflate(R.layout.device_dialog, null)
        adapter.roomList.forEach{
        var rb = RadioButton(this)
        rb.text = it.name
        rb.id = 100+it.id.toInt()
        cl.rgRoomName.addView(rb)
    }

        builder.setView(cl)
        builder.show()
    }

    fun ViewMenu()
    {
        if (binding.LayoutScrollMenu.visibility != View.GONE)
        {
            binding.LayoutScrollMenu.visibility = View.GONE
            binding.MenuView.background = getDrawable(R.drawable.show_ico)
        }
        else
        {
            binding.LayoutScrollMenu.visibility = View.VISIBLE
            binding.MenuView.background = getDrawable(R.drawable.close_ico)
        }
    }

    fun GoHome()
    {
        binding.rcvRoom.visibility = View.VISIBLE
        binding.llDeviceMenu.visibility = View.GONE
        binding.House.background = getDrawable(R.drawable.back_house_on)
        binding.Room.background = getDrawable(R.drawable.back_light_off)
    }

    fun GoRoom()
    {
        binding.rcvRoom.visibility = View.GONE
        binding.llDeviceMenu.visibility = View.VISIBLE
        binding.House.background = getDrawable(R.drawable.back_house_off)
        binding.Room.background = getDrawable(R.drawable.back_light_on)
    }

    fun SetNotification()
    {
        if(notification)
        {
            notification = false
            binding.Notification.background = getDrawable(R.drawable.off_ico)
            Toast.makeText(this, "Уведомления отключены", Toast.LENGTH_SHORT).show()
        }

        else
        {
            notification = true
            binding.Notification.background = getDrawable(R.drawable.on_ico)
            Toast.makeText(this, "Уведомления влючены", Toast.LENGTH_SHORT).show()
        }
    }

    override fun OnClick(room: Room) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Вы действительно хотите удалить комнату?")
        builder.setMessage("Подтвердите удаление")
        builder.setPositiveButton("Удалить") { dialogInterface, which ->
            adapter.removeRoom(room)
        }
        builder.setNeutralButton("Назад"){ dialogInterface , which ->
        }
        builder.show()
    }

    override fun OnSwitch(device: Device, state: Boolean) {
        if(state)
        {
            device.imageId = R.drawable.device_on
            device.state = true
        }
        else{
            device.imageId = R.drawable.device_off
            device.state = false
        }
        devAdapter.notifyDataSetChanged()
    }


}
