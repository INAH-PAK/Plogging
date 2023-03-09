package com.example.plogging.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.plogging.PloggingApplication
import com.example.plogging.databinding.DialogChangeNameBinding
import com.example.plogging.ui.setting.SettingViewmodel
import kotlinx.coroutines.launch


class ChangeUserNameDialog : DialogFragment() {

    private lateinit var binding: DialogChangeNameBinding

    private val viewmodel: SettingViewmodel by viewModels {
        SettingViewmodel.provideFactory(
            preferenceRepository = PloggingApplication.appContainer.provideUserDataRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogChangeNameBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewmodel.clickDialogButton.collect {
                if (it) dismiss()
            }
        }
    }
}