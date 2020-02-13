package com.example.flutter_app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant


class MainActivity : FlutterActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        private const val CHANNEL = "com.example.flutter_app/my_channel"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this)
//        MethodChannel(flutterView, CHANNEL)
//                .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
//                    onMethodCall(call, result)
//


        // Check if the user revoked runtime permissions.
        if (!checkPermissions()) {
            requestPermissions()
        }
    }


    fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        print(result)
    }


    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val fineLocationPermissionState = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)

        val backgroundLocationPermissionState = if (Build.VERSION.SDK_INT >= 29)
            ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        else
            PackageManager.PERMISSION_GRANTED

        return fineLocationPermissionState == PackageManager.PERMISSION_GRANTED &&
                backgroundLocationPermissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {

        Log.i(TAG, "Requesting permission")
        // Request permission. It's possible this can be auto answered if device policy
        // sets the permission in a given state or the user denied the permission
        // previously and checked "Never ask again".
        val permissionsList = if (Build.VERSION.SDK_INT >= 29) arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION) else
            arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
        ActivityCompat.requestPermissions(this@MainActivity, permissionsList,
                REQUEST_PERMISSIONS_REQUEST_CODE)

    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isEmpty()) { // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    (Build.VERSION.SDK_INT < 29 || grantResults[1] == PackageManager.PERMISSION_GRANTED)) { // Permission was granted.
                Log.d(TAG, "Permission granted")
                App.instance?.requestLocationUpdates()
            } else {
                // Permission denied.
                Toast.makeText(this, "Failed to get permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
