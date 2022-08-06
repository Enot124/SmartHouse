package com.example.smarthouse

data class Device(
    val id: String,
    var name: String, val roomName: String, var imageId: Int, var state: Boolean, var stateValue : String,val type : Int)