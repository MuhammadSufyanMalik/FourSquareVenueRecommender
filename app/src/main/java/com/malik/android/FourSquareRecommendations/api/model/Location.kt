package com.malik.android.FourSquareRecommendations.api.model

data class Location(
    val address: String,
    val country: String,
    val locality: String,
    val neighborhood: List<String>, // It was "neighbourhood" in the assignment
    val postcode: String,
    val region: String,
)
