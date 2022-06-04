package com.example.smarthouse

import com.example.smarthouse.databinding.ActivityMainBinding

/*class Room  {

    var Id : String = ""
    var Name : String = "Room"
    var State : Boolean = false
    var StateValue : String = "OFF"
    var DevicesCount : Int = 0
    constructor(){
    }
    constructor(id: String, name: String, state: Boolean, stateValue : String, devicesCount : Int)
    {
        Id = id
        Name = name
        State = state
        StateValue = stateValue
        DevicesCount = devicesCount
    }

    fun Add()
    {

    }

}*/

data class Room( val id: String, val name: String, val imageId : Int, var state: Boolean, var stateValue : String){
        var Devices = ArrayList<Device>()
}