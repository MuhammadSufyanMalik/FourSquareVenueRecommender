package com.malik.android.FourSquareRecommendations.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.malik.android.FourSquareRecommendations.R
import com.malik.android.FourSquareRecommendations.utils.AppConstants
import com.malik.android.FourSquareRecommendations.databinding.ActivityMainBinding
import com.malik.android.FourSquareRecommendations.ui.adapters.FourSquareRecommendationsRVAdapter
import com.malik.android.FourSquareRecommendations.viewmodel.ActivityViewModel
import com.google.android.gms.location.*


class MainActivity : AppCompatActivity() {

    val activityViewModel: ActivityViewModel by viewModels()
    // create reference to the adapter
    private lateinit var rvAdapter: FourSquareRecommendationsRVAdapter

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvAdapter = FourSquareRecommendationsRVAdapter(emptyList())
        binding.rvList.adapter = rvAdapter

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        activityViewModel.dataList.observe(this){ resultList ->

            rvAdapter.updateData(resultList)

            binding.progressCircularBar.visibility = View.GONE

            fusedLocationProvider?.removeLocationUpdates(locationCallback)
        }

        onCheckLocationPermission() //Check Permission Method
    }

/* Methods */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                refreshLocation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshLocation()
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider?.removeLocationUpdates(locationCallback)
        }
    }

    private fun refreshLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        binding.progressCircularBar.visibility = View.VISIBLE


    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //updated location is the last location in the list
                val location = locationList.last()
                val lat =  location.latitude
                val long =  location.longitude
                System.out.println("Latitude:$lat")
                System.out.println("Latitude:$long")
                activityViewModel.getLatLong(lat,long)

            }
        }
    }

    private fun onCheckLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed:")
                    .setMessage("This app needs the Location permission, please allow location access")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            AppConstants.REQUEST_CODE_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppConstants.REQUEST_CODE_LOCATION_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted,
                    // location-related task
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )

                    }

                } else {

                    // Permission denied,
                    Toast.makeText(this, "Permission Denied, Kindly give permissions to access Location", Toast.LENGTH_LONG).show()
                    binding.progressCircularBar.visibility = View.GONE

                    // Check if we are in a state where the user has denied the permission and
                    // Don't ask again is Checked from Location Dialog
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                    else{
                        Toast.makeText(this, "Permission Denied,Kindly give permissions to access Location", Toast.LENGTH_LONG).show()
                        binding.progressCircularBar.visibility = View.GONE
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )

                    }


                }
                return
            }

        }
    }

    /* Methods */

}
