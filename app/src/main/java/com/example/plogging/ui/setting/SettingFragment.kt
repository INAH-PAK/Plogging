package com.example.plogging.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.plogging.PloggingApplication
import com.example.plogging.R
import com.example.plogging.databinding.FragmentSettingBinding
import com.example.plogging.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewmodel by viewModels<SettingViewmodel> {
        SettingViewmodel.provideFactory(
            preferenceRepository = PloggingApplication.appContainer.provideUserDataRepository()
        )
    }

    private val getGalleryContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                lifecycleScope.launch {
                    viewmodel.onSelectGalleryImage(uri.toString())
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout()
    }

    private fun setLayout() {
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch(Dispatchers.IO) {
                    viewmodel.isLogin.collect {
                        if (!it) requireActivity().apply {
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewmodel.clickUserName.collect {
                        if (it) {
                            findNavController().navigate(R.id.action_settingFragment_to_changeUserNameDialog)
                        }
                    }
                }
                launch {
                    viewmodel.clickUserProfileImage.collect {
                        Log.i("이미지 클릭", "설정화면 이미지 클릭")
                        if (it) {
                            Log.i("이미지 클릭", "설정화면 이미지 클릭")
                            getGalleryContent.launch("image/*")
                        }
                    }
                }
            }
        }
    }
}