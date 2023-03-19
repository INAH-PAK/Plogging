package com.example.plogging.ui.calender

import android.util.Log
import android.widget.CalendarView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.plogging.data.source.remote.RecodeRepository

class CalenderViewmodel(private val recodeRepository: RecodeRepository) : ViewModel() {

    val dateChangeListener: CalendarView.OnDateChangeListener =
        CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            Log.i("달력 화면 클릭", " 년 $year 달 $month  일 $dayOfMonth")
        }

    companion object {
        fun provideFactory(recodeRepository: RecodeRepository) = viewModelFactory {
            initializer {
                CalenderViewmodel(
                    recodeRepository
                )
            }
        }
    }
}