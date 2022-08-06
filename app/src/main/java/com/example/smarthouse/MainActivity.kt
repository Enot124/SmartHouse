package com.example.smarthouse

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthouse.databinding.ActivityMainBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.device_dialog.*
import kotlinx.android.synthetic.main.device_dialog.view.*
import kotlinx.android.synthetic.main.device_item.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() , RoomAdapter.Listener, DeviceAdapter.ListenerDevice{
    lateinit var binding: ActivityMainBinding
    private var  adapter = RoomAdapter(this)
    private var devAdapter = DeviceAdapter(this)
    var notification: Boolean = true
    lateinit var mDataBase : DatabaseReference
    private var journal = ArrayList<String>()
    private var USER_KEY = "Journal"
    private var defId = 0
    private val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    lateinit var adapterJournal : ArrayAdapter<String>

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
        binding.Journal.setOnClickListener{
            GoJournal()
        }

        binding.btnAddDevice.setOnClickListener{
            CreateDevice()
        }
        binding.btnResetJournal.setOnClickListener{
            ResetJournal()
        }
        binding.btnDeleteJournal.setOnClickListener {
            DeleteJournal()
        }
    }

    private fun init(){
        adapterJournal = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, journal)
        binding.apply {
            rcvRoom.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvRoom.adapter = adapter
            rcvDevice.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvDevice.adapter = devAdapter
            lvJournal.adapter = adapterJournal
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
            if (text != "") {
                var imageId: Int = when (text) {
                    "Гостиная", "Hall" -> R.drawable.bed2
                    "Кухня", "Kitchen" -> R.drawable.kitchen
                    "Ванная", "Bathroom" -> R.drawable.bath
                    "Спальня", "Bedroom" -> R.drawable.bed1
                    "Детская", "Childroom" -> R.drawable.child
                    else -> R.drawable.house

                }
                val room = Room(defId++.toString(), text, imageId, false, "OFF")
                adapter.addRoom(room)
                Toast.makeText(this, "Комната была создана", Toast.LENGTH_SHORT).show()
                val date = Date()
                val stroke = sdf.format(date) + "  Комната \"" + room.name + "\" была создана"
                mDataBase.push().setValue(stroke)
            }
            else
                Toast.makeText(this, "Название комнаты не было введено", Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton("Назад"){ dialogInterface , which ->
        }
        var cl = layoutInflater.inflate(R.layout.custom_dialog, null)

        builder.setView(cl)
        builder.show()
    }

    fun CreateDevice(){
        if(adapter.roomList.isNotEmpty()) {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("Окно редактирования устройства")
            builder.setPositiveButton("OK") { dialogInterface, which ->
                var dialog = dialogInterface as AlertDialog
                var editText = dialog.findViewById<EditText>(R.id.edDeviceName)
                val text = editText?.text.toString()
                if(text != "") {
                    val selectedDeviceType =
                        dialog.findViewById<RadioButton>(dialog.rgDeviceType.checkedRadioButtonId)
                        val typePosition = dialog.rgDeviceType.indexOfChild(selectedDeviceType)
                        val imageId = when (typePosition) {
                            0 -> R.drawable.device_off
                            1 -> R.drawable.dimmer_off
                            2 -> R.drawable.sensor
                            3 -> R.drawable.gerkon
                            else -> {
                                R.drawable.device_off
                            }
                        }
                    val selectedRadioButton = dialog.findViewById<RadioButton>(dialog.rgRoomName.checkedRadioButtonId)
                    val position = dialog.rgRoomName.indexOfChild(selectedRadioButton)
                    val room = adapter.roomList[position]
                    val device = Device(defId++.toString(), text, room.name, imageId, false, "OFF", typePosition)
                    room.Devices.add(device)
                    devAdapter.addDevice(device)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Устройство создано", Toast.LENGTH_SHORT).show()
                    val date = Date()
                    val stroke = sdf.format(date) + "  Устройство \"" + device.name + "\" в комнате " + device.roomName + " было создано"
                    mDataBase.push().setValue(stroke)
                }
                else
                    Toast.makeText(this, "Название устройства не было введено", Toast.LENGTH_SHORT).show()

            }

            builder.setNeutralButton("Назад") { dialogInterface, which ->
            }
            var cl = layoutInflater.inflate(R.layout.device_dialog, null)
            adapter.roomList.forEach {
                var set = 0
                var rb = RadioButton(this)
                if (set == 0)
                    rb.isChecked = true
                rb.text = it.name
                rb.id = 100 + it.id.toInt()
                cl.rgRoomName.addView(rb)
                set++
            }

            builder.setView(cl)
            builder.show()
        }
        else
            Toast.makeText(this, "Не найдено ни одной комнаты", Toast.LENGTH_SHORT).show()
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
        binding.llJournal.visibility = View.GONE
        binding.House.background = getDrawable(R.drawable.back_house_on)
        binding.Room.background = getDrawable(R.drawable.back_light_off)
        binding.Journal.background = getDrawable(R.drawable.back_scenary_off)
    }

    fun GoRoom()
    {
        binding.rcvRoom.visibility = View.GONE
        binding.llDeviceMenu.visibility = View.VISIBLE
        binding.llJournal.visibility = View.GONE
        binding.House.background = getDrawable(R.drawable.back_house_off)
        binding.Room.background = getDrawable(R.drawable.back_light_on)
        binding.Journal.background = getDrawable(R.drawable.back_scenary_off)
    }

    fun GoJournal()
    {
        binding.rcvRoom.visibility = View.GONE
        binding.llDeviceMenu.visibility = View.GONE
        binding.llJournal.visibility = View.VISIBLE
        binding.House.background = getDrawable(R.drawable.back_house_off)
        binding.Room.background = getDrawable(R.drawable.back_light_off)
        binding.Journal.background = getDrawable(R.drawable.back_scenary_on)
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

    fun ResetJournal()
    {
        val vListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                journal.clear()
                for(ds : DataSnapshot in dataSnapshot.children){
                    journal.add(ds.value.toString())
                }
                adapterJournal.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        mDataBase.addValueEventListener(vListener)
    }

    fun DeleteJournal()
    {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Вы действительно хотите удалить все данные журнала?")
        builder.setMessage("Подтвердите удаление")
        builder.setPositiveButton("Удалить") { dialogInterface, which ->
            mDataBase.setValue(null)
            journal.clear()
            Toast.makeText(this, "Журнал очищен", Toast.LENGTH_SHORT).show()
        }
        builder.setNeutralButton("Назад"){ dialogInterface , which ->
        }
        builder.show()
    }


    override fun OnClick(room: Room) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Вы действительно хотите удалить комнату?")
        builder.setMessage("Подтвердите удаление")
        builder.setPositiveButton("Удалить") { dialogInterface, which ->
            room.Devices.forEach {
                devAdapter.deviceList.remove(it)
            }
            devAdapter.notifyDataSetChanged()
            adapter.removeRoom(room)
            Toast.makeText(this, "Комната удалена", Toast.LENGTH_SHORT).show()
            val date = Date()
            val stroke = sdf.format(date) + "  Удаление комнаты " + room.name
            mDataBase.push().setValue(stroke)
        }
        builder.setNeutralButton("Назад"){ dialogInterface , which ->
        }
        builder.show()
    }

    override fun OnClick(device: Device) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Вы действительно хотите удалить устройство?")
        builder.setMessage("Подтвердите удаление")
        builder.setPositiveButton("Удалить") { dialogInterface, which ->
            devAdapter.removeDevice(device)
            adapter.roomList.forEach{
                if(it.name == device.roomName) {
                    it.Devices.remove(device)
                    adapter.checkStateRoom(it)
                }
            }
            Toast.makeText(this, "Устройство удалено", Toast.LENGTH_SHORT).show()
            val date = Date()
            val stroke = sdf.format(date) + "  Удаление устройства \"" + device.name + "\" из комнаты " + device.roomName
            mDataBase.push().setValue(stroke)
        }
        builder.setNeutralButton("Назад"){ dialogInterface , which ->
        }
        builder.show()
    }

    override fun OnSwitch(device: Device, state: Boolean) {
        val date = Date()
        if(state)
        {
            device.imageId = R.drawable.device_on
            device.state = true
            device.stateValue = "ON"
            adapter.roomList.forEach{
                if(it.name == device.roomName) {
                    it.state = true
                    adapter.setStateRoom(it)
                }
            }
            val stroke = sdf.format(date) + "  Устройство \"" + device.name + "\", " + device.roomName + " было включено"
            mDataBase.push().setValue(stroke)
        }
        else{
            device.imageId = R.drawable.device_off
            device.state = false
            device.stateValue = "OFF"
            adapter.roomList.forEach{
                if(it.name == device.roomName) {
                    adapter.checkStateRoom(it)
                }
            }
            val stroke = sdf.format(date) + "  Устройство \"" + device.name + "\", " + device.roomName + " было выключено"
            mDataBase.push().setValue(stroke)
        }
        devAdapter.notifyDataSetChanged()
        adapter.notifyDataSetChanged()
    }

    override fun OnProgress(device: Device, progress: Int) {
        if (progress != 0) {
            device.imageId = R.drawable.dimmer_on
            device.state = true
            adapter.roomList.forEach{
                if(it.name == device.roomName) {
                    it.state = true
                    adapter.checkStateRoom(it)
                }
            }
        }
        else{
            device.imageId = R.drawable.dimmer_off
            device.state = false
            adapter.roomList.forEach {
            if (it.name == device.roomName) {
                it.state = false
                adapter.checkStateRoom(it)
                }
            }
        }
    }
    override fun SensorClick(device: Device) {
        if(!device.state) {
            device.state = true
            val date = Date()
            val stroke = sdf.format(date) + "  Датчик \"" + device.name + "\", " + device.roomName + " был активирован"
            mDataBase.push().setValue(stroke)
        }
        else {
            device.state = false
        }
        devAdapter.notifyDataSetChanged()
    }

    override fun GerkonClick(device: Device) {
        if(!device.state) {
            device.state = true
            val date = Date()
            val stroke = sdf.format(date) + "  Геркон \"" + device.name + "\", " + device.roomName + " был активирован"
            mDataBase.push().setValue(stroke)
        }
        else {
            device.state = false
        }
        devAdapter.notifyDataSetChanged()
    }

    override fun save(device: Device) {
        devAdapter.notifyDataSetChanged()
        val date = Date()
        val stroke = sdf.format(date) + "  Диммер \"" + device.name + "\", " + device.roomName + " был изменён"
        mDataBase.push().setValue(stroke)
    }
}
