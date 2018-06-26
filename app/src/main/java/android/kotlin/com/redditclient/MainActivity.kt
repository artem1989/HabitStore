package android.kotlin.com.redditclient

import android.content.Intent
import android.kotlin.com.redditclient.db.HabbitDbTable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = HabbitsAdapter(HabbitDbTable(this).readAllHabits())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.add_habit) {
            switchToActivity(CreateHabbitActivity::class.java)
        }
        return true
    }

    private fun switchToActivity(c: Class<*>) {
        val intent = Intent(this, c)
        startActivity(intent)
    }

}
