package com.shamanshs.russian_checkers

import android.content.Intent
import android.util.Log;
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat




class MainActivity : AppCompatActivity() {
    var idText : TextView? = null
    var count : Int = 0
    var st : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        idText = findViewById(R.id.Text_id_1)
        Log.d("My Log","onCreate")
        findViewById<Button?>(R.id.startbutton).setOnClickListener {
            startGame()
        }
    }

    fun one() {
        Thread {
            while (st) {
                runOnUiThread {
                    idText?.setText(count.toString())
                    count++
                }
                Thread.sleep(1000)
            }
        }.start()
    }

    fun startGame() {
        startActivity(Intent(this, GameActivity::class.java))
    }



    override fun onStart() {
        super.onStart()
        st = true
        one()
        Log.d("My Log","onStart")
    }

    override fun onPause() {
        super.onPause()
        st = false
        Log.d("My Log","onPause")
    }

    override fun onStop() {
        super.onStop()
        st = false
        count += 100
        Log.d("My Log","onStop")
    }
}