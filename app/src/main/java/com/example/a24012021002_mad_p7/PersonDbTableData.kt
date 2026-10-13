package com.example.a24012021002_mad_p7

class PersonDbTableData {
    companion object {
        const val TABLE_NAME = "persons"
        const val COLUMN_ID = "id"
        const val COLUMN_PERSON_NAME = "person_name"
        const val COLUMN_PERSON_EMAIL_ID = "person_email_id"
        const val COLUMN_PERSON_PHONE_NO = "person_phone_no"
        const val COLUMN_PERSON_ADDRESS = "person_address"
        const val COLUMN_PERSON_GPS_LAT = "person_lat"
        const val COLUMN_PERSON_GPS_LONG = "person_long"

        // Create table SQL query
        const val CREATE_TABLE = (
            "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_PERSON_NAME + " TEXT,"
                + COLUMN_PERSON_EMAIL_ID + " TEXT,"
                + COLUMN_PERSON_PHONE_NO + " TEXT,"
                + COLUMN_PERSON_ADDRESS + " TEXT,"
                + COLUMN_PERSON_GPS_LAT + " REAL,"
                + COLUMN_PERSON_GPS_LONG + " REAL"
                + ")"
            )
    }
}
