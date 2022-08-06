package com.plcoding.cleanarchitecturenoteapp.features.notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plcoding.cleanarchitecturenoteapp.ui.theme.*

@Entity(tableName = "note")
data class Note (
    val title:String,
    val content:String,
    val timeStamp:Long,
    val color:Int,
    @PrimaryKey val id:Int?=null
){
    companion object{
        val colors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(override val message:String):Exception(message)