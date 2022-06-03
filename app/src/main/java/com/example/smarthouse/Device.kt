package com.example.smarthouse

data class Device(
    val id: Int,
    val name: String, val roomName: String, val imageId: Int, var state: Boolean)