package com.cahstudio.instaapp.model

data class Post(
    val id: Int = 0,
    val title: String? = null,
    val image: String? = null,
    val desc: String? = null
){
    constructor() : this(0, "", "","")
}