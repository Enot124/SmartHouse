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


class MainActivity : AppCompatActivity() , RoomAdapter.Listener{
    lateinit var binding: ActivityMainBinding
    private var  adapter = RoomAdapter(this)
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
            mDataBase.push().setValue(room)
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
        binding.rcvRoom.visibility = View.VISIBLE
        binding.ScrollDevice.visibility = View.GONE
    }

    fun GoRoom()
    {
        binding.rcvRoom.visibility = View.GONE
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


}
