package android.kotlin.com.redditclient.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.kotlin.com.redditclient.Habit
import android.kotlin.com.redditclient.db.HabbitEntry.DESCR_COL
import android.kotlin.com.redditclient.db.HabbitEntry.IMAGE_COL
import android.kotlin.com.redditclient.db.HabbitEntry.TABLE_NAME
import android.kotlin.com.redditclient.db.HabbitEntry.TITLE_COL
import android.kotlin.com.redditclient.db.HabbitEntry._ID
import android.util.Log
import java.io.ByteArrayOutputStream

class HabbitDbTable(context: Context) {

    private val TAG = HabbitDbTable::class.java.simpleName

    private val dbHelper = HabbitTrainerDb(context)

    fun store(habbit: Habit): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        with(values) {
            put(TITLE_COL, habbit.title)
            put(DESCR_COL, habbit.description)
            put(IMAGE_COL, toByteArray(habbit.image))
        }

       val id = db.transaction {
           insert(HabbitEntry.TABLE_NAME, null, values)
       }

        Log.d(TAG, "Habit has been stored to database")

        return id
    }

    fun readAllHabits(): List<Habit> {
        val columns = arrayOf(_ID, TITLE_COL, DESCR_COL, IMAGE_COL)

        val order = "${HabbitEntry._ID} ASC"

        val db = dbHelper.readableDatabase

        val cursor = db.doQuery(TABLE_NAME, columns, orderBy = order)

        return parseHabitsFrom(cursor)
    }

    private fun parseHabitsFrom(cursor: Cursor): MutableList<Habit> {
        val habits = mutableListOf<Habit>()

        while (cursor.moveToNext()) {
            val title = cursor.getString(TITLE_COL)
            val description = cursor.getString(DESCR_COL)
            val bitmap = cursor.getBitmap(IMAGE_COL)
            habits.add(Habit(title, description, bitmap))

        }
        cursor.close()
        return habits
    }

    private fun Cursor.getString(columnName: String) = getString(getColumnIndex(columnName))

    private fun Cursor.getBitmap(columnName: String): Bitmap {
        val byteArray = getBlob(getColumnIndex(columnName))
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun  SQLiteDatabase.doQuery(table: String, columns: Array<String>,
                                           selection: String? = null , selectionArgs: Array<String>? = null,
                                           groupBy: String? = null , having: String? = null, orderBy: String? = null): Cursor {

        return query(table, columns, selection, selectionArgs, groupBy, having, orderBy)

    }


    private fun toByteArray(image: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    private inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.() -> T): T {
        beginTransaction()
        val result = try {
            val returnValue = function()
            setTransactionSuccessful()

            returnValue
        } finally {
            endTransaction()
        }
        close()

        return result
    }

}