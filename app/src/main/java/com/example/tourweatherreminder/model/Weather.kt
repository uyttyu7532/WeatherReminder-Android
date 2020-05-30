package com.example.tourweatherreminder


class Schedule(val weather : String,
                val title: String,
              val date: String,
              val temp: Float,
              val place: String,
              val isLastItem: Boolean = false) : ViewType {

    override fun getViewType(): Int  = ViewType.ITEM

}
