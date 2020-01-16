package com.festfeed.awf.activities

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.festfeed.awf.R
import com.festfeed.awf.adapters.MapItemAdapter
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.models.EventResponse
import com.festfeed.awf.models.Overlay
import com.festfeed.awf.models.OverlayArray
import com.festfeed.awf.utils.Preferences
import com.festfeed.awf.utils.click
import com.festfeed.awf.utils.finishDown
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONObject
import java.io.IOException
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MapsActivityForSingle : BaseActivity(), OnMapReadyCallback {

    override fun layoutResID() = R.layout.activity_maps

    private lateinit var mMap: GoogleMap
    private lateinit var selectedData: EventResponse
    lateinit var  data: Overlay
    private var markerList = arrayListOf<Marker>()

    private val mapFragment by lazy {
        supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
    }

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(sheet)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        } catch (e: Exception) {
        }
        //intent.getStringExtra("url")

        try {
            val obj = JSONObject(readJSONFromAsset()!!)
            val parser = JsonParser()
            val mJson = parser.parse(obj.toString())
            val gson = Gson()
            val configurationData = gson.fromJson<OverlayArray>(mJson, OverlayArray::class.java)
            Log.e("", "" + configurationData)



            configurationData.overlays.forEach {
                if (it.eventid == Preferences.eventid) {
                    data = it
                }
            }
        } catch (e: Exception) {
        }


        mapFragment.getMapAsync(this)
        backBtn.click { onBackPressed() }


        selectedData = Preferences.eventResponse.single {
            it.location == Preferences.locationId
        }


        mapItemRecyclerView.layoutManager = LinearLayoutManager(this)
        mapItemRecyclerView.adapter = MapItemAdapter(selectedData.locations) {

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            markerList[it].showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLng(markerList[it].position))

            /* mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        selectedData.locations[it].latitude,
                        selectedData.locations[it].longitude
                    ), 18f
                )
            )*/
        }
        mapMenu.visibility=View.GONE
      //  mapMenu.click { bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED) }
    }

    /**
     * Manipulates the eventmap once available.
     * This callback is triggered when the eventmap is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false;
        runWithPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true

            val locationButton =
                (mapFragment.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                    Integer.parseInt("2")
                )
            val rlp = locationButton.layoutParams as (RelativeLayout.LayoutParams)
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 30);
        }
        // Add a marker in Sydney and move the camera
        var place: LatLng
        mMap.run {

            place = LatLng(intent.getDoubleExtra("lat",0.0), intent.getDoubleExtra("lng",0.0))
            val marker = mMap.addMarker(MarkerOptions().position(place).title(intent.getStringExtra("name")))
            markerList.add(marker)
            val builder = LatLngBounds.Builder()
            for (marker in markerList) {
                builder.include(marker.position)
            }
            val bounds = builder.build()
            val padding = 10 // offset from edges of the eventmap in pixels
            setOnMapLoadedCallback {
                moveCamera(
                    CameraUpdateFactory.newLatLngBounds(bounds, padding)
                )
            }

        }



        try {
            if(data!=null)
            {
                Thread(Runnable {
                    // performing some dummy time taking operation
                    val newarkBounds =  LatLngBounds(
                        LatLng(data.swLat, data.swLong),       // South west corner
                        LatLng(data.neLat, data.neLong));      // North east corner


                    val stringToFilter = data.imageName
                    var stringWithOnlyDigits = stringToFilter.filter { it.isLetterOrDigit() }
                    stringWithOnlyDigits=  stringWithOnlyDigits.replace("-","")
                    //  var drawable=  resources.getDrawable(resources.getIdentifier(stringWithOnlyDigits, "drawable", packageName));
                 //   val drawableResourceId = this.resources.getIdentifier(stringWithOnlyDigits, "drawable", this.getPackageName());


                    try {
                        val bitmap = assets.open(stringWithOnlyDigits+".png")
                        val bit = BitmapFactory.decodeStream(bitmap)
                      //  img.setImageBitmap(bit)
                        val newarkMap =  GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromBitmap(bit))
                            .positionFromBounds(newarkBounds)

                        this@MapsActivityForSingle.runOnUiThread(java.lang.Runnable {
                            mMap.addGroundOverlay(newarkMap)
                        })
                    } catch (e1: IOException) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace()
                    }




                    // try to touch View of UI thread

                }).start()
            }
        } catch (e: Exception) {
        }

    }

    override fun onBackPressed() {
        finishDown()
    }
    private fun readJSONFromAsset(): String? {
        var json: String? = null
        try {
            val fileData = assets.open("EventMapOverlays.json")
            val size = fileData.available()
            val buffer = ByteArray(size)
            fileData.read(buffer)
            fileData.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }
}
