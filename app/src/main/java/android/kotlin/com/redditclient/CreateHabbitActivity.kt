package android.kotlin.com.redditclient

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.kotlin.com.redditclient.db.HabbitDbTable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_create_habbit.*
import kotlinx.android.synthetic.main.single_card.*
import java.io.IOException

class CreateHabbitActivity : AppCompatActivity() {

    private val TAG = CreateHabbitActivity::class.java.simpleName
    private val REQUST_CODE = 1

    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_habbit)
    }

    fun chooseImage(v: View) {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT

        val chooser = Intent.createChooser(intent, "Choose image for habbit")
        startActivityForResult(chooser, REQUST_CODE)

        Log.d(TAG, "Intent to choose image")
    }

    fun storeHabbit(v: View) {
        if(et_title.isBlank() || et_description.isBlank()) {
            Log.d(TAG, "No habbit stored: title or description missing")
            displayErrorMessage("Your habbit no title or descr")
            return
        } else if(imageBitmap == null) {
            displayErrorMessage("Your habbit no image")
            return
        }
        tv_error.visibility = INVISIBLE

        val title = et_title.text.toString()
        val descrition = et_description.text.toString()
        val habit = Habit(title, descrition, imageBitmap!!)

        val id = HabbitDbTable(this).store(habit)
        if(id == -1L) {
            displayErrorMessage("Habit could not be stored")
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayErrorMessage(message: String) {
        tv_error.text = message
        tv_error.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val bitmap = tryReadBitmap(data.data)

            bitmap?.let {
                this.imageBitmap = bitmap
                iv_image.setImageBitmap(bitmap)
                Log.d(TAG, "Read image bitmap and updated imageView")
            }
        }
    }

    private fun EditText.isBlank() = this.text.toString().isBlank()

    private fun tryReadBitmap(data: Uri): Bitmap? {
        return try {
            MediaStore.Images.Media.getBitmap(contentResolver, data)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
