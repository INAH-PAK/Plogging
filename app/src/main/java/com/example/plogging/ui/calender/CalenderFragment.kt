package com.example.plogging.ui.calender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plogging.PloggingApplication
import com.example.plogging.data.source.remote.RecodeRepository
import com.example.plogging.databinding.FragmentCarlenderBinding

class CalenderFragment : Fragment() {

    private var _binding: FragmentCarlenderBinding? = null
    private val binding get() = _binding!!
    private val viewmodel by viewModels<CalenderViewmodel> {
        CalenderViewmodel.provideFactory(
            RecodeRepository(
                PloggingApplication.appContainer.provideAppDatabase().aloneRecordeDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarlenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seyLayout()
    }

    private fun seyLayout() {
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}