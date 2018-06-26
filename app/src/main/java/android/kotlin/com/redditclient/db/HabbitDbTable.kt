package android.kotlin.com.redditclient.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.kotlin.com.redditclient.Habit
import android.util.Log
import java.io.ByteArrayOutputStream

class HabbitDbTable(context: Context) {

    private val TAG = HabbitDbTable::class.java.simpleName

    private val dbHelper = HabbitTrainerDb(context)

    fun store(habbit: Habit): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        with(values) {
            put(HabbitEntry.TITLE_COL, habbit.title)
            put(HabbitEntry.DESCR_COL, habbit.description)
            put(HabbitEntry.IMAGE_COL, toByteArray(habbit.image))
        }

       val id = db.transaction {
           insert(HabbitEntry.TABLE_NAME, null, values)
       }

        Log.d(TAG, "Habit has been stored to database")

        return id
    }

    fun readAllHabits(): List<Habit> {
        val columns = arrayOf(HabbitEntry._ID, HabbitEntry.TITLE_COL, HabbitEntry.DESCR_COL, HabbitEntry.IMAGE_COL)

        val order = "${HabbitEntry._ID} ASC"
        val db = dbHelper.readableDatabase

        val cursor = db.query(HabbitEntry.TABLE_NAME, columns, null, null, null, null, order)
        val habits = mutableListOf<Habit>()

        while(cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndex(HabbitEntry.TITLE_COL))
            val description = cursor.getString(cursor.getColumnIndex(HabbitEntry.DESCR_COL))
            val byteArray = cursor.getBlob(cursor.getColumnIndex(HabbitEntry.IMAGE_COL))
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            habits.add(Habit(title, description, bitmap))

        }
        cursor.close()

        return habits
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