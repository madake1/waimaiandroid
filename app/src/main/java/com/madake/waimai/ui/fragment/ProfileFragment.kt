package com.madake.waimai.ui.fragment


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption

import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle

import com.madake.waimai.R



class ProfileFragment : Fragment() {


    private lateinit var mapView: MapView

    private lateinit var aMap: AMap


    private lateinit var locationClient: AMapLocationClient



    companion object {

        private const val REQUEST_LOCATION = 100

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(
            R.layout.fragment_profile,
            container,
            false
        )

    }



    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)


        mapView = view.findViewById(R.id.mapView)


        mapView.onCreate(savedInstanceState)


        aMap = mapView.map



        initMap()


        checkPermission()


    }



    private fun initMap(){


        // 开启定位按钮
        aMap.uiSettings.isMyLocationButtonEnabled = true


        // 设置定位样式
        val myLocationStyle = MyLocationStyle()

        myLocationStyle.interval(2000)


        aMap.myLocationStyle = myLocationStyle


        // 开启定位层
//        aMap.isMyLocationEnabled = true


    }



    private fun checkPermission(){


        if(
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ){

            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_LOCATION
            )

        }else{

            startLocation()

        }


    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){

        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )


        if(requestCode == REQUEST_LOCATION
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ){

            startLocation()

        }else{

            Toast.makeText(
                requireContext(),
                "需要定位权限",
                Toast.LENGTH_SHORT
            ).show()

        }

    }




    private fun startLocation(){


        aMap.isMyLocationEnabled = true
        locationClient = AMapLocationClient(
            requireContext()
        )


        val option =
            AMapLocationClientOption()


        // 高精度
        option.locationMode =
            AMapLocationClientOption.AMapLocationMode.Hight_Accuracy


        // 单次定位
        option.isOnceLocation = true


        locationClient.setLocationOption(option)



        locationClient.setLocationListener { location ->


            if(location.errorCode == 0){


                val lat = location.latitude

                val lon = location.longitude



                val position =
                    com.amap.api.maps.model.LatLng(
                        lat,
                        lon
                    )



                // 移动地图到当前位置
                aMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        position,
                        17f
                    )
                )


            }else{


                Toast.makeText(
                    requireContext(),
                    location.errorInfo,
                    Toast.LENGTH_SHORT
                ).show()

            }


        }



        locationClient.startLocation()


    }





    override fun onResume(){

        super.onResume()

        if(::mapView.isInitialized)
            mapView.onResume()

    }



    override fun onPause(){

        super.onPause()

        if(::mapView.isInitialized)
            mapView.onPause()

    }



    override fun onDestroyView(){

        super.onDestroyView()


        if(::mapView.isInitialized)
            mapView.onDestroy()


        if(::locationClient.isInitialized)
            locationClient.stopLocation()

    }

}