package com.example.a24012021002_mad_p7

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a24012021002_mad_p7.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {
    private val TAG = "MapActivity"
    private lateinit var binding: ActivityMapBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainMap) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbarMap)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarMap.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val obj = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("Object", Person::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("Object") as? Person
        }

        if (obj != null) {
            Log.i(TAG, "onCreate: \n$obj")
            val lat = obj.latitude
            val lng = obj.longitude

            binding.toolbarMap.title = obj.name
            binding.tvMapName.text = obj.name
            binding.tvMapAddress.text = obj.address
            binding.tvMapCoords.text = "Lat: $lat, Long: $lng"

            binding.webviewMap.webViewClient = WebViewClient()
            binding.webviewMap.settings.javaScriptEnabled = true
            val mapHtml = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                    <style>
                        html, body, #map { height: 100%; margin: 0; padding: 0; }
                    </style>
                </head>
                <body>
                    <div id="map"></div>
                    <script>
                        var map = L.map('map').setView([$lat, $lng], 5);
                        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            attribution: '&copy; OpenStreetMap contributors'
                        }).addTo(map);
                        L.marker([$lat, $lng]).addTo(map)
                            .bindPopup('<b>${obj.name.replace("'", "\\'")}</b><br>${obj.address.replace("'", "\\'")}')
                            .openPopup();
                    </script>
                </body>
                </html>
            """.trimIndent()
            binding.webviewMap.loadDataWithBaseURL("https://openstreetmap.org", mapHtml, "text/html", "UTF-8", null)
        }
    }
}
