package com.malik.android.FourSquareRecommendations.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malik.android.FourSquareRecommendations.api.PlacesService
import com.malik.android.FourSquareRecommendations.api.VenueRecommendationsQueryBuilder
import com.malik.android.FourSquareRecommendations.api.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ActivityViewModel() : ViewModel() {


    val dataList = MutableLiveData<List<Result>>()

    fun getLatLong(lat: Double, lon: Double) {

        viewModelScope.launch(Dispatchers.IO) {
            val query = VenueRecommendationsQueryBuilder()
                .setLatitudeLongitude(lat, lon)
                .build()
            val response = PlacesService.instance
                .getVenueRecommendations(query)
                .execute()

            val errorBody = response.errorBody()
            val responseWrapper = response.body()

            runBlocking(Dispatchers.Main) {
                dataList.value = responseWrapper?.results ?: emptyList()
            }
            Log.d("Response: ", responseWrapper.toString())
            Log.e("Error: ", errorBody.toString())
        }

    }
}