package com.example.smarthouse


data class Room( val id: String, val name: String, val imageId : Int, var state: Boolean, var stateValue : String){
        var Devices = ArrayList<Device>()
}