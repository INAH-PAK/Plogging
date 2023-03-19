package com.example.plogging.ui.calender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class CalenderViewmodel : ViewModel() {

    companion object {
        fun provideFactory() = viewModelFactory {
            initializer {
                CalenderViewmodel()
            }
        }
    }
}