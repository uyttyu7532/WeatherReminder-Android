package com.example.tourweatherreminder


data class Timepoint(
    val timepoint: String,
    val description: String
) : ViewType {

    override fun getViewType(): Int = ViewType.LINE

}
