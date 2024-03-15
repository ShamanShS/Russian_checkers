package com.shamanshs.russian_checkers

import android.content.Intent
import android.util.Log;
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shamanshs.russian_checkers.databinding.ActivityMainBinding
import com.shamanshs.russian_checkers.tools.DataBaseModel
import com.shamanshs.russian_checkers.tools.GameLogic.convertToGameModel
import com.shamanshs.russian_checkers.tools.GameLogic.id
import com.shamanshs.russian_checkers.tools.GameLogic.status
import com.shamanshs.russian_checkers.tools.GameLogic.youColor
import kotlin.random.Random
import kotlin.random.nextInt


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d("My Log","onCreate")
        binding.startbutton.setOnClickListener {
            startGame()
        }
        binding.buttonCreate.setOnClickListener {
            createGame()
        }
        binding.buttonJoin.setOnClickListener {
            joinGame()
            }
    }


    fun startGame() {
        startActivity(Intent(this, GameActivity::class.java))
    }

    fun createGame(){
        id = Random.nextInt(1000..9999)
        youColor = 1
        startGame()
    }


    private fun joinGame() {
        val gameId = binding.gameID.text.toString()
        if (gameId.isEmpty()) {
            binding.gameID.error = "Нужен id"
            return
        }
        Firebase.firestore.collection("Games")
            .document(gameId)
            .get()
            .addOnSuccessListener {
                val dataGame = it?.toObject(DataBaseModel::class.java)
                if (dataGame == null){
                    binding.gameID.error = "Нужен верный id"

                }
                else{
                    convertToGameModel(dataGame)
                    youColor = -1
                    startGame()
                }
            }

    }



    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }
}