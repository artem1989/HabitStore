package android.kotlin.com.redditclient.db

import android.provider.BaseColumns

val DATABASE_NAME = "habbittrainer.db"
val DATABASE_VERSION = 10

object HabbitEntry: BaseColumns {
    val TABLE_NAME = "hebbit"
    val TITLE_COL = "title"
    val DESCR_COL = "description"
    val IMAGE_COL = "image"
    val _ID = "id"
}
