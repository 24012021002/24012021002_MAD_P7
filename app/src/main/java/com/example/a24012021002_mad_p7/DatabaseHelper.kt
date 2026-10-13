package com.example.a24012021002_mad_p7

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "person_db"
    }

    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        // create persons table
        db.execSQL(PersonDbTableData.CREATE_TABLE)
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + PersonDbTableData.TABLE_NAME)

        // Create tables again
        onCreate(db)
    }

    fun insertPerson(person: Person): Long {
        // get writable database as we want to write data
        val db = this.writableDatabase
        val values = getValues(person)

        // insert row
        val id = db.insertWithOnConflict(
            PersonDbTableData.TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )

        // close db connection
        db.close()

        // return newly inserted row id
        return id
    }

    private fun getValues(person: Person): ContentValues {
        val values = ContentValues()
        values.put(PersonDbTableData.COLUMN_ID, person.id)
        values.put(PersonDbTableData.COLUMN_PERSON_NAME, person.name)
        values.put(PersonDbTableData.COLUMN_PERSON_EMAIL_ID, person.emailId)
        values.put(PersonDbTableData.COLUMN_PERSON_PHONE_NO, person.phoneNo)
        values.put(PersonDbTableData.COLUMN_PERSON_ADDRESS, person.address)
        values.put(PersonDbTableData.COLUMN_PERSON_GPS_LAT, person.latitude)
        values.put(PersonDbTableData.COLUMN_PERSON_GPS_LONG, person.longitude)
        return values
    }

    fun getPerson(id: String): Person? {
        // get readable database as we are not inserting anything
        val db = this.readableDatabase
        val cursor = db.query(
            PersonDbTableData.TABLE_NAME,
            arrayOf(
                PersonDbTableData.COLUMN_ID,
                PersonDbTableData.COLUMN_PERSON_NAME,
                PersonDbTableData.COLUMN_PERSON_EMAIL_ID,
                PersonDbTableData.COLUMN_PERSON_PHONE_NO,
                PersonDbTableData.COLUMN_PERSON_ADDRESS,
                PersonDbTableData.COLUMN_PERSON_GPS_LAT,
                PersonDbTableData.COLUMN_PERSON_GPS_LONG
            ),
            PersonDbTableData.COLUMN_ID + "=?",
            arrayOf(id),
            null,
            null,
            null,
            null
        ) ?: return null

        if (!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return null
        }

        val person = getPerson(cursor)

        // close the db connection
        cursor.close()
        db.close()
        return person
    }

    private fun getPerson(cursor: Cursor): Person {
        return Person(
            cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_EMAIL_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_PHONE_NO)),
            cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_ADDRESS)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_GPS_LAT)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_GPS_LONG))
        )
    }

    // Select All Query
    val allPersons: ArrayList<Person>
        get() {
            val persons = ArrayList<Person>()

            // Select All Query ordered by name DESC matching the practical UI design
            val selectQuery = "SELECT  * FROM " + PersonDbTableData.TABLE_NAME + " ORDER BY " +
                    PersonDbTableData.COLUMN_PERSON_NAME + " DESC"

            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    persons.add(getPerson(cursor))
                } while (cursor.moveToNext())
            }

            // close db connection
            cursor.close()
            db.close()

            // return persons list
            return persons
        }

    val personsCount: Int
        get() {
            val countQuery = "SELECT  * FROM " + PersonDbTableData.TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            val count = cursor.count
            cursor.close()
            db.close()
            return count
        }

    fun updatePerson(person: Person): Int {
        val db = this.writableDatabase
        val values = getValues(person)

        // updating row
        val count = db.update(
            PersonDbTableData.TABLE_NAME,
            values,
            PersonDbTableData.COLUMN_ID + " = ?",
            arrayOf(person.id)
        )
        db.close()
        return count
    }

    fun deletePerson(person: Person) {
        val db = this.writableDatabase
        db.delete(
            PersonDbTableData.TABLE_NAME,
            PersonDbTableData.COLUMN_ID + " = ?",
            arrayOf(person.id)
        )
        db.close()
    }
}
