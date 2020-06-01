package com.example.tourweatherreminder


data class Schedule(
    val weather: String,
    val title: String,
    val date: String,
    val temp: Float,
    val rain: Int,
    val place: String,
    val lat: Double,
    val long: Double,
    val isLastItem: Boolean = false
) : ViewType {

    override fun getViewType(): Int = ViewType.ITEM

}
