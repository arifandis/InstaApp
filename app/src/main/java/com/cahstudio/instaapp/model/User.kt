package com.cahstudio.instaapp.model

data class User(
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null
){
    constructor() : this("","","")
}