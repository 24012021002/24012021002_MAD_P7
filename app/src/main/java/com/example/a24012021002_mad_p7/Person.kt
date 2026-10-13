package com.example.a24012021002_mad_p7

import org.json.JSONObject
import java.io.Serializable

class Person(
    var id: String,
    var name: String,
    var emailId: String,
    var phoneNo: String,
    var address: String,
    var latitude: Double,
    var longitude: Double
) : Serializable {

    /*
     * [
     *   {
     *     "_id": "63745d5225b3880cd90c0433",
     *     "email": "daniels_patrick@gnu.ac.in",
     *     "phone": "+91 9725056791",
     *     "profile": {
     *       "name": "Daniels Patrick",
     *       "address": "18 Glenmore Avenue, Rehrersburg, Kentucky",
     *       "location": {
     *         "lat": -4.544946,
     *         "long": 18.318319
     *       }
     *     }
     *   }
     * ]
     */
    constructor(jsonObject: JSONObject) : this("", "", "", "", "", 0.0, 0.0) {
        id = jsonObject.getString("id")
        emailId = jsonObject.getString("email")
        phoneNo = jsonObject.getString("phone").removeSuffix("}")
        val profileJson = jsonObject.getJSONObject("profile")
        name = profileJson.getString("name")
        address = profileJson.getString("address")
        val locationJson = profileJson.getJSONObject("location")
        latitude = locationJson.getDouble("lat")
        longitude = locationJson.getDouble("long")
    }

    override fun toString(): String {
        return "$name\n$phoneNo\n$emailId\n$address\nLat: $latitude, Long: $longitude"
    }
}
