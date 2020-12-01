package com.transformers.test.models.transformers

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Transformer (
    val id : String = "",
    var name : String = "",
    var team: String = "",
    @SerializedName("team_icon") val teamIcon: String = "",
    var courage: Int = 0,
    var endurance: Int = 0,
    var firepower: Int = 0,
    var intelligence: Int = 0,
    var rank: Int = 0,
    var skill: Int = 0,
    var speed: Int = 0,
    var strength: Int = 0,
    var winner: Boolean = false
) : Serializable {
    fun getTeamFormatted() = if (team == "A") "Autobots" else "Descepticons"
}
