package com.example.smarthouse

data class Device(
    val id: Int,
    val name: String, val roomName: String, var imageId: Int, var state: Boolean)