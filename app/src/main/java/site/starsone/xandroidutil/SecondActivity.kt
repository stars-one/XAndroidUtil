package site.starsone.xandroidutil

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SecondActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val btn = findViewById<Button>(R.id.btnChangeUserName)
        btn.setOnClickListener {
            MyGlobalData.config.setValue(Random(100).nextInt().toString())
        }
    }
}