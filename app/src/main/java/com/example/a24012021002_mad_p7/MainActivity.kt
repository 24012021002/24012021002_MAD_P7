package com.example.a24012021002_mad_p7

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a24012021002_mad_p7.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHelper
    private val personList = ArrayList<Person>()
    private lateinit var adapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)

        db = DatabaseHelper(this)
        adapter = PersonAdapter(this, personList)
        binding.content.listviewMain.adapter = adapter

        getPersonDataFromSqliteDb()

        // Automatically fetch from JSON URL on first launch if database is empty
        if (personList.isEmpty()) {
            getPersonDetailsFromHttpsUrl()
        }

        binding.fab.setOnClickListener {
            getPersonDetailsFromHttpsUrl()
        }
    }

    fun deletePerson(position: Int) {
        val deletedPerson = personList[position]
        db.deletePerson(deletedPerson)
        personList.removeAt(position)
        adapter.notifyItemRemoved(position)
        Toast.makeText(this, "At $position, ${deletedPerson.name} Deleted", Toast.LENGTH_LONG).show()
    }

    private fun getPersonDataFromSqliteDb() {
        personList.clear()
        personList.addAll(db.allPersons)
        adapter.notifyDataSetChanged()
    }

    private fun getPersonDetailsFromHttpsUrl() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val http = HttpRequest()
                val data = http.makeServiceCall(
                    "https://api.json-generator.com/templates/5rDXHcbgpo93/data",
                    "d7wrtfqywyhu7y2bcbsz3cgjpbfisuhnmbibvgvf"
                )
                withContext(Dispatchers.Main) {
                    if (data != null) {
                        getPersonDetailsFromJson(data)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to retrieve JSON data from URL",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "getPersonDetailsFromHttpsUrl: " + e.message)
            }
        }
    }

    private fun getPersonDetailsFromJson(data: String) {
        val size = personList.size
        personList.clear()
        adapter.notifyItemRangeRemoved(0, size)
        try {
            val jsonArray = JSONArray(data)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray[i] as JSONObject
                val person = Person(jsonObject)
                personList.add(person)
                try {
                    if (db.getPerson(person.id) == null) {
                        db.insertPerson(person)
                    } else {
                        db.updatePerson(person)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (ee: JSONException) {
            ee.printStackTrace()
        }
        getPersonDataFromSqliteDb()
        Toast.makeText(this, "Data Fetched from URL & Saved to SQLite", Toast.LENGTH_SHORT).show()
    }
}