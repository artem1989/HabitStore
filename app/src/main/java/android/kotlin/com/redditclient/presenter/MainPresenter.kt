package android.kotlin.com.redditclient.presenter

import android.content.Context
import android.kotlin.com.redditclient.Habit
import android.kotlin.com.redditclient.db.HabbitDbTable

class MainPresenter(val context: Context) {

    fun readHabits(): List<Habit>{
        return HabbitDbTable(context).readAllHabits()
    }

}