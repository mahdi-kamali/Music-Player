package com.example.musicplayer.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.musicplayer.objects.Music

class DBHandler (context : Context ) :

    SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    var dataBase: SQLiteDatabase = this.writableDatabase





    /* Const Values */
    companion object {

        private const val DATABASE_NAME = "normal_data_base.db"
        private const val DATABASE_VERSION = 1


        /* Table 1 (Name)  And Columns  */
        private const val MAIN_TABLE = "All Musics"
        private const val SECOND_TABLE = "Liked Songs"
        private const val ID = "Id"
        private const val COLUMN_ONE = "MusicName"
        private  const val COLUMN_TWO = "MusicPath"
        private  const val COLUMN_THREE = "MusicImage"
        private const val COLUMN_FOUR = "MusicArtists"
        private const val COLUMN_FIVE = "MusicAlbum"
        private const val COLUMN_SIX = "MusicDuration"


    }



    /* This Method Only Run Only Once  And Create a Normal  DataBase For The Application */
    override fun onCreate(db: SQLiteDatabase?) {


        val quarry = ("CREATE TABLE '$MAIN_TABLE' ($ID INTEGER PRIMARY KEY, $COLUMN_ONE TEXT, $COLUMN_TWO TEXT, $COLUMN_THREE TEXT, $COLUMN_FOUR TEXT, $COLUMN_FIVE TEXT, $COLUMN_SIX TEXT)")
        val quarry2 = ("CREATE TABLE '$SECOND_TABLE' ($ID INTEGER PRIMARY KEY, $COLUMN_ONE TEXT, $COLUMN_TWO TEXT, $COLUMN_THREE TEXT, $COLUMN_FOUR TEXT, $COLUMN_FIVE TEXT, $COLUMN_SIX TEXT)")

        // Toast.makeText(context, "Done ! ", Toast.LENGTH_SHORT).show()
        db?.execSQL(quarry)
        db?.execSQL(quarry2)


    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }




    /* (CREATE) New Table */
    fun createNewTable(tableName: String) : Boolean {


        return try {
            val dataBase = this.writableDatabase
            val quarry = ("CREATE TABLE '$tableName' ($ID INTEGER PRIMARY KEY, $COLUMN_ONE TEXT, $COLUMN_TWO TEXT, $COLUMN_THREE TEXT, $COLUMN_FOUR TEXT, $COLUMN_FIVE TEXT, $COLUMN_SIX TEXT)")
            dataBase.execSQL(quarry)
            dataBase.close()
            // Toast.makeText(context, "Table : ($tableName) created", Toast.LENGTH_LONG).show()
            true
        } catch (e : SQLiteException) {
            // Toast.makeText(context, "Table : ($tableName) Already Exist", Toast.LENGTH_LONG).show()
            return false
        }



    }

    /* (GET) Table ) */
    @SuppressLint("Recycle")
    fun getTables () : ArrayList<String> {
        val tables = ArrayList<String>()


        try {

            val database = this.readableDatabase
            val c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)

            if (c.moveToFirst()) {
                while (!c.isAfterLast) {
                    tables.add(c.getString(c.getColumnIndex("name")))
                    c.moveToNext()
                }
            }
        }
        catch ( e : Exception) {

        }

        // No Need Those
        tables.remove("android_metadata")
        tables.remove("sqlite_sequence")

        return tables
    }

    /* (DELETE) a Table */
    fun deleteTable (tableName: String) : Boolean {

        return try {

            dataBase.execSQL("DROP TABLE IF EXISTS '$tableName'")

            return true

        } catch ( e : Exception) {
            false
        }

    }




    /* (ADD) Item To A Table In DataBase */
    fun addItemToDataBase(tableName: String, columnOneData : String, columnTwoData : String , columnThreeData : String , columnFour : String , columnFive : String , columnSix : String): Boolean {





        try {
            val cv = ContentValues()
            if (dataBase.isOpen) {}
            else{
                dataBase = this.writableDatabase
            }
            cv.put(COLUMN_ONE, columnOneData)
            cv.put(COLUMN_TWO, columnTwoData)
            cv.put(COLUMN_THREE,columnThreeData)
            cv.put(COLUMN_FOUR,columnFour)
            cv.put(COLUMN_FIVE,columnFive)
            cv.put(COLUMN_SIX,columnSix)



            val insert: Long = dataBase.insert("'$tableName'", null, cv)

            // db.close()
            return insert > -1

        }
        catch (e : SQLiteException) {
            return false
        }




    }



    /* (GET) Items From DataBase */
    @SuppressLint("Recycle")
    fun getItems(tableName : String): ArrayList<Music> {

        val data : ArrayList<Music> = ArrayList()


        if (!dataBase.isOpen) {
            dataBase = this.writableDatabase
        }

        val quarryCode = "SELECT * FROM '$tableName'"
        val cursor = dataBase.rawQuery( quarryCode , null)

        if (cursor.moveToFirst()) {
            do {
                data.add(Music(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)))
            }
            while (cursor.moveToNext())
        }






        return data
    }

    /* (DELETE) Item From DataBase */
    fun deleteItem (tableName: String, itemPath : String): Boolean {

        return try {

            val quarry = "DELETE FROM '$tableName' WHERE $COLUMN_TWO LIKE '%$itemPath%'"

            val dataBase = this.writableDatabase
            dataBase.execSQL(quarry)
            true
        } catch (e : Exception) {
            false
        }

    }


    fun deleteAllItem (tableName: String) {
        dataBase.execSQL("delete from '$tableName'")
    }














}