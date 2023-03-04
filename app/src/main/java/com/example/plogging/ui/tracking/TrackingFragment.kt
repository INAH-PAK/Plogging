package com.example.plogging.ui.tracking

import android.Manifest
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
import com.example.plogging.PloggingApplication
import com.example.plogging.R
import com.example.plogging.data.model.RestRoomMarkersUiState
import com.example.plogging.data.source.TrackingRepository
import com.example.plogging.databinding.FragmentTrackingBinding
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.LocationSource
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch

class TrackingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private val viewmodel by viewModels<TrackingViewModel> {
        TrackingViewModel.provideFactory(
            trackingRepository = TrackingRepository(PloggingApplication.appContainer.provideApiClient())
        )
    }
    private val fusedLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: LocationSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 화장실 버튼 클릭시, 마커 데이터 가져오기.
                viewmodel.uiState.collect { uiState ->
                    when (uiState) {
                        is RestRoomMarkersUiState.Success<List<Marker>> -> {
                            if (uiState.restRoomList.isEmpty()) return@collect
                            Log.i("마커 데이터 가져오기 성공", uiState.restRoomList.toString())
                            uiState.restRoomList.forEach {
                                it.map = naverMap
                            }
                        }
                        is RestRoomMarkersUiState.Error<List<Marker>> -> {
                            Snackbar.make(binding.root, uiState.message, 3000).show()
                        }
                    }

                }
            }
        }

    }

    private fun setLayout() {
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        val mapFragment = childFragmentManager.findFragmentById(R.id.naver_map) as MapFragment
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
            }
        mapFragment.getMapAsync(this)
        requestPermission.launch(fusedLocationPermission)
    }

    val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        when (it) {
            true -> {
                Log.i(" 권한 요청 허용", " 권한 요청 허용")
            }
            false -> {
                naverMap.locationTrackingMode = LocationTrackingMode.None
                Log.i(" 권한 요청 허용", " 권한 요청 거부")
            }
        }
    }

    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}