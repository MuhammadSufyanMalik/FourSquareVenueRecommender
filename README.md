# FourSquareVenueRecommender

This repository contains the code for the venue recommender using Four Square Places API.
Foursquare Places API that shows a list of venues around the user’s location.
It contains test cases.
In the example I displayed data in the RecyclerView.
Decide yourself which venue details you want to display to user.


##Libraries Used
    //ForRestful Request
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-moshi:2.9.0"
    //LocationService
    implementation 'com.google.android.gms:play-services-location:18.0.0'
    // For Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"
    // for adding recyclerview
    implementation 'androidx.recyclerview:recyclerview:1.2.0'
    // for adding cardview
    implementation 'androidx.cardview:cardview:1.0.0'
    // for Image glide
    implementation 'com.github.bumptech.glide:glide:4.4.0'

## Setup
Add your Foursquare client ID and secret to `local.gradle`. See `local.gradle.example` for details.
Note: You can verify your credentials with `src/test/java/com/malik/android/FourSquareRecommendations/PlacesUnitTest.kt`