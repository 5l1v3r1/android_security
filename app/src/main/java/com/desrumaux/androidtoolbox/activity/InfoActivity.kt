package com.desrumaux.androidtoolbox.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.desrumaux.androidtoolbox.R
import com.desrumaux.androidtoolbox.adapter.ContactsAdapter
import com.desrumaux.androidtoolbox.model.Contact
import kotlinx.android.synthetic.main.activity_info.*


class InfoActivity : AppCompatActivity() {
    companion object {

        const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
        const val PERMISSIONS_REQUEST = 100
    }

    private lateinit var mLocManager : LocationManager
    private lateinit var mLocListener : MyLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.desrumaux.androidtoolbox.R.layout.activity_info)

        mLocManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocListener = MyLocationListener()

        info_photo_icon.setOnClickListener { selectImageInAlbum() }

        val permissionsNotGranted = getAllPermissionsNotGranted()
        if (permissionsNotGranted.isEmpty()) {
            showContacts()
            showLocation()
        } else {
            ActivityCompat.requestPermissions(this, permissionsNotGranted, PERMISSIONS_REQUEST)
        }
    }

    private fun getAllPermissionsNotGranted(): Array<String?> {
        val perms = mutableListOf<String>()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            perms.add(Manifest.permission.READ_CONTACTS)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            perms.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        return perms.toTypedArray()
    }

    private fun showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST
            )
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            getAllContacts()
        }
    }

    private fun getAllContacts() {
        val contactModelArrayList = ArrayList<Contact>()

        val contacts = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (contacts!!.moveToNext()) {
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

            val contactModel = Contact()
            contactModel.contactName = name
            contactModelArrayList.add(contactModel)
            Log.d("Name>>", name)
        }
        contacts.close()

        info_contacts_recycler.apply {
            layoutManager = LinearLayoutManager(this@InfoActivity)
            adapter = ContactsAdapter(contactModelArrayList, this@InfoActivity)
        }
    }

    private fun showLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST
            )
        } else {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            criteria.isAltitudeRequired = false
            criteria.isBearingRequired = false
            criteria.isCostAllowed = true
            criteria.powerRequirement = Criteria.POWER_MEDIUM
            val provider = mLocManager.getBestProvider(criteria, true)
            mLocManager.requestLocationUpdates(provider, 0, 0f, mLocListener)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showContacts()
                showLocation()
            }
            else if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts()
                showLocation()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permissions_error_message),
                    Toast.LENGTH_SHORT
                )
                    .show()
                goBackToHome()
            }
        }
    }

    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

    private fun goBackToHome() {
        val intent = Intent(this@InfoActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM
        ) {
            //photo from gallery
            val fileUri = data?.data
            info_photo_icon.setImageURI(fileUri)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onStop() {
        super.onStop()
        mLocManager.removeUpdates(mLocListener)
    }

    private inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(location: Location) {
            val message = String.format(
                "Position trouvée :\nLongitude: %1\$s \nLatitude: %2\$s",
                location.longitude, location.latitude
            )
            Toast.makeText(this@InfoActivity, message, Toast.LENGTH_LONG).show()
            info_latitude_text.text = getString(R.string.info_latitude_defaultText, location.latitude.toString())
            info_longitude_text.text = getString(R.string.info_longitude_defaultText, location.longitude.toString())
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }
}
