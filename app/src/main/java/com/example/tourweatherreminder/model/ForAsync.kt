package com.example.tourweatherreminder.model

import java.net.URL

data class ForAsync(var url: URL, var title:String, var date:String,var timestamp: Long, var place:String, var latitude:Double, var longitude:Double) {
}