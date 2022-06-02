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


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var  adapter = RoomAdapter()
    var notification: Boolean = true
    lateinit var mDataBase : DatabaseReference
    private var USER_KEY = "Room"
    private var defId = 0
    var rooms : ArrayList<Room> ?= null

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


    }

    private fun init(){
        binding.apply {
            rcvRoom.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvRoom.adapter = adapter
        }
    }

    fun CreateRoom()
    {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Окно редактирования комнаты")
        builder.setPositiveButton("OK"){ dialogInterface , which ->
            /*var view = layoutInflater.inflate(R.layout.room_item, null)
             var dialog = dialogInterface as AlertDialog
             var editText = dialog.findViewById<EditText>(R.id.EdText)
             var btnRemove = view.findViewById<Button>(R.id.btnRemoveRoom)

             var rname = view.findViewById<TextView>(R.id.RoomName)
             var img = view.findViewById<ImageView>(R.id.ImageRoom)
             rname.text = editText?.text
             val text = rname.text.toString()
             when (text) {
                 "Гостиная","Hall" -> img.setImageResource(R.drawable.bed2)
                 "Кухня","Kitchen" -> img.setImageResource(R.drawable.kitchen)
                 "Ванная","Bathroom" -> img.setImageResource(R.drawable.bath)
                 "Спальня","Bedroom"-> img.setImageResource(R.drawable.bed1)
                 "Детская","Childroom" -> img.setImageResource(R.drawable.child)
                 else -> img.setImageResource(R.drawable.house)

             }

             rooms?.add(room)
             var id = mDataBase.key + defId++
             var name = text
             var state = false
             var stateValue = "OFF"
             var devCount = 0
             var room = Room(id.toString(), name, state, stateValue, devCount)
             mDataBase.push().setValue(room)
             binding.ScrollLayout.addView(view)
             Toast.makeText(this, "Комната успешно создана", Toast.LENGTH_SHORT).show()
             GoHome()*/
            var dialog = dialogInterface as AlertDialog
            var editText = dialog.findViewById<EditText>(R.id.EdText)
            val text = editText?.text.toString()
            var imageId = 0
            imageId = when (text) {
                "Гостиная","Hall" -> R.drawable.bed2
                "Кухня","Kitchen" -> R.drawable.kitchen
                "Ванная","Bathroom" -> R.drawable.bath
                "Спальня","Bedroom"-> R.drawable.bed1
                "Детская","Childroom" -> R.drawable.child
                else -> R.drawable.house

            }
            val room = Room(defId++.toString(),text,imageId,false,"OFF",0)
            adapter.addRoom(room)
        }

        builder.setNeutralButton("Назад"){ dialogInterface , which ->
        }
        var cl = layoutInflater.inflate(R.layout.custom_dialog, null)

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
        binding.ScrollRoom.visibility = View.VISIBLE
        binding.ScrollDevice.visibility = View.GONE
    }

    fun GoRoom()
    {
        binding.ScrollRoom.visibility = View.GONE
        binding.ScrollDevice.visibility = View.VISIBLE
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


}
