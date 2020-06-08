package com.example.tourweatherreminder


data class Schedule(
    val weather: String="weather default",
    val title: String="title default",
    val date: String="date default",
    val temp: Float=99.9f,
    val rain: Float=99.9f,
    val place: String="place default",
    val isLastItem: Boolean = false
) : ViewType {

    override fun getViewType(): Int = ViewType.ITEM

}
