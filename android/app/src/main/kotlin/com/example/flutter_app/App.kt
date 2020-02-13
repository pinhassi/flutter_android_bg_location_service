package com.example.flutter_app

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import io.flutter.app.FlutterApplication

class App : FlutterApplication() {

    init {
        instance = this
    }

    companion object {
        private val TAG = App::class.java.simpleName
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        requestLocationUpdates()
    }

    /**
     * Provides access to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Sets up the location request. Android has two location request settings:
     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     *
     *
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     *
     *
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest(): LocationRequest {

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        val UPDATE_INTERVAL: Long = 60000 // Every 60 seconds.


        /**
         * The fastest rate for active location updates. Updates will never be more frequent
         * than this value, but they may be less frequent.
         */
        val FASTEST_UPDATE_INTERVAL: Long = 30000 // Every 30 seconds


        /**
         * The max time before batched results are delivered by location services. Results may be
         * delivered sooner than this interval.
         */
        val MAX_WAIT_TIME = UPDATE_INTERVAL * 5 // Every 5 minutes.


        val locationRequest = LocationRequest()
        // Sets the desired interval for active location updates. This interval is
// inexact. You may not receive updates at all if no location sources are available, or
// you may receive them slower than requested. You may also receive updates faster than
// requested if other applications are requesting location at a faster interval.
// Note: apps running on "O" devices (regardless of targetSdkVersion) may receive updates
// less frequently than this interval when the app is no longer in the foreground.
        locationRequest.interval = UPDATE_INTERVAL
        // Sets the fastest rate for active location updates. This interval is exact, and your
// application will never receive updates faster than this value.
        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        // Sets the maximum time when batched location updates are delivered. Updates may be
// delivered sooner than this interval.
        locationRequest.maxWaitTime = MAX_WAIT_TIME

        return locationRequest
    }

    private fun getPendingIntent(): PendingIntent? { // Note: for apps targeting API level 25 ("Nougat") or lower, either
// PendingIntent.getService() or PendingIntent.getBroadcast() may be used when requesting
// location updates. For apps targeting API level O, only
// PendingIntent.getBroadcast() should be used. This is due to the limits placed on services
// started in the background in "O".
// TODO(developer): uncomment to use PendingIntent.getService().
//        Intent intent = new Intent(this, LocationUpdatesIntentService.class);
//        intent.setAction(LocationUpdatesIntentService.ACTION_PROCESS_UPDATES);
//        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        val intent = Intent(this, LocationUpdatesBroadcastReceiver::class.java)
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Handles the Request Updates button and requests start of location updates.
     */
    fun requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates")
            if (mFusedLocationClient == null)
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            mFusedLocationClient?.requestLocationUpdates(createLocationRequest(), getPendingIntent())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}