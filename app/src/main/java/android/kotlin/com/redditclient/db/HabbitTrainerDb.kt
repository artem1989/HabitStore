package android.kotlin.com.redditclient.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HabbitTrainerDb(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val SQL_CREATE_ENTRIES = "CREATE TABLE ${HabbitEntry.TABLE_NAME} (" +
            "${HabbitEntry._ID} INTEGER PRIMARY KEY," +
            "${HabbitEntry.TITLE_COL} TEXT," +
            "${HabbitEntry.DESCR_COL} TEXT," +
            "${HabbitEntry.IMAGE_COL} BLOB" +
            ")"
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${HabbitEntry.TABLE_NAME}"


    override fun onCreate(db: SQLiteDatabase) {
       db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}