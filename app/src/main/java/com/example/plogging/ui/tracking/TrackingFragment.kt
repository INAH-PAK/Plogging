package com.example.plogging.ui.tracking

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.databinding.FragmentTrackingBinding
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

class TrackingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private val fusedLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

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
    }

    private fun setLayout() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.naver_map) as MapFragment
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
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
        Log.i("지도", "준비가 되어따 !1")
        this.naverMap = p0
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        Log.i("지도", naverMap.locationSource.toString())
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}