package com.shamanshs.russian_checkers

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shamanshs.russian_checkers.databinding.ActivityGameBinding
import com.shamanshs.russian_checkers.tools.DataBaseModel
import com.shamanshs.russian_checkers.tools.GameLogic
import com.shamanshs.russian_checkers.tools.GameLogic.accessMove
import com.shamanshs.russian_checkers.tools.GameLogic.accessMoveKing
import com.shamanshs.russian_checkers.tools.GameLogic.blackCount
import com.shamanshs.russian_checkers.tools.GameLogic.blackCutDownWhite
import com.shamanshs.russian_checkers.tools.GameLogic.canKill
import com.shamanshs.russian_checkers.tools.GameLogic.canMoveBlack
import com.shamanshs.russian_checkers.tools.GameLogic.canMoveWhite
import com.shamanshs.russian_checkers.tools.GameLogic.convertToDataModel
import com.shamanshs.russian_checkers.tools.GameLogic.convertToGameModel
import com.shamanshs.russian_checkers.tools.GameLogic.fillField
import com.shamanshs.russian_checkers.tools.GameLogic.id
import com.shamanshs.russian_checkers.tools.GameLogic.isKing
import com.shamanshs.russian_checkers.tools.GameLogic.killMoveKing
import com.shamanshs.russian_checkers.tools.GameLogic.moveField
import com.shamanshs.russian_checkers.tools.GameLogic.playing_field
import com.shamanshs.russian_checkers.tools.GameLogic.reset
import com.shamanshs.russian_checkers.tools.GameLogic.resetField
import com.shamanshs.russian_checkers.tools.GameLogic.resetLogic
import com.shamanshs.russian_checkers.tools.GameLogic.status
import com.shamanshs.russian_checkers.tools.GameLogic.turn
import com.shamanshs.russian_checkers.tools.GameLogic.whiteCount
import com.shamanshs.russian_checkers.tools.GameLogic.whitecutDownBlack
import com.shamanshs.russian_checkers.tools.GameLogic.youColor

class GameActivity : AppCompatActivity() {
    private val normColor: String = "#453737"
    private var choosingFigure: Int = 0
    private var chooseFigure: Array<Int> = Array(3) { 0 }
    private var massacre: Int = 0
    var dataGame = DataBaseModel()
    lateinit var binding : ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gridIsSquare()
        if (id != -1) {
            val doc = Firebase.firestore.collection("Games").document(id.toString())
            onChangeListener(doc)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (id != -1 && youColor == 1) {
            binding.idInfo.setText("ID:$id")
            status = 1
            fillField()
            sendToDataBase()
        }
        if (id == -1){
            fillField()
            fillGameField()
        }
        if (id == -1)
            binding.textOver.setText("Turn ${if (turn == 1) "white" else "black"}")
        if (youColor == -1 && id != -1){
            status = 2
            sendToDataBase()
        }
        if(status == 1)
            binding.textOver.setText("Waiting for the player")

    }


    private fun gridIsSquare() {
        val grid = binding.gridLayout2
        val w = grid.layoutParams.width
        grid.layoutParams.height = w
    }

    private fun fillGameField() {
        val grid: GridLayout = findViewById(R.id.gridLayout2)
        for (p in 1..63) {
            val i: Int = p % 8
            val j: Int = p / 8
            val cell: ImageView? = getView(i, j, grid)
            if (playing_field[i][j] == 1 && cell != null)
                cell.setImageResource(R.drawable.chinese_white)
            else if (playing_field[i][j] == -1 && cell != null)
                cell.setImageResource(R.drawable.chinese_black)
            else if (playing_field[i][j] == 2 && cell != null)
                cell.setImageResource(R.drawable.black_king)
            else if (playing_field[i][j] == -2 && cell != null)
                cell.setImageResource(R.drawable.white_king)
            else if (playing_field[i][j] == 0 && cell != null)
                cell.setImageResource(0)
        }
    }

    fun xy(v: View) {
        if ((status == 1 || (youColor != turn)) && id != -1)
            return
        val g: GridLayout = findViewById(R.id.gridLayout2)
        val (x, y) = getIndex(v, g)
        var move = false
        //Выбор черной фигуры
        if ((playing_field[x][y] == -1 || playing_field[x][y] == -2) && choosingFigure == 0 && turn == -1 && massacre == 0 &&
            (!(!canKill() || moveField[x][y] != 2) || !(canKill() || moveField[x][y] == 2))) {
            move = false
            if (playing_field[x][y] == -1) {
                if (accessMove(x, y, -1)) {
                    goToClue()
                    choosingFigure = 1
                    chooseFigure[0] = x
                    chooseFigure[1] = y
                    chooseFigure[2] = -1
                }
            }
            else if(playing_field[x][y] == -2){
                if (accessMoveKing(x ,y, -1)) {
                    goToClue()
                    choosingFigure = 1
                    chooseFigure[0] = x
                    chooseFigure[1] = y
                    chooseFigure[2] = -2
                }
            }

        }
        //Выбор белой фигуры
        else if ((playing_field[x][y] == 1 || playing_field[x][y] == 2) && choosingFigure == 0 && turn == 1  && massacre == 0 &&
            (!(!canKill() || moveField[x][y] != 2) || !(canKill() || moveField[x][y] == 2))){
            move = false
            if (playing_field[x][y] == 1) {
                if (accessMove(x, y, 1)) {
                    goToClue()
                    choosingFigure = 1
                    chooseFigure[0] = x
                    chooseFigure[1] = y
                    chooseFigure[2] = 1
                }
            }
            else if (playing_field[x][y] == 2) {
                if (accessMoveKing(x ,y, 1)) {
                    goToClue()
                    choosingFigure = 1
                    chooseFigure[0] = x
                    chooseFigure[1] = y
                    chooseFigure[2] = 2
                }
            }
        }
        //Ход фигурой
        else if (playing_field[x][y] == 0 && moveField[x][y] == 1 && (chooseFigure[2] == turn || chooseFigure[2] == turn * 2 )) {
            move = true
            playing_field[x][y] = chooseFigure[2]
            playing_field[chooseFigure[0]][chooseFigure[1]] = 0
            goToNormal()
            reset(0)
            choosingFigure = 0
            turn *= -1
        }
        //Срубить фигуру
        else if (playing_field[x][y] == 0 && moveField[x][y] == -1 && chooseFigure[2] == turn) {
            move = true
            playing_field[x][y] = chooseFigure[2]
            playing_field[chooseFigure[0]][chooseFigure[1]] = 0
            playing_field[x - ((x - chooseFigure[0]) / 2)][y - ((y - chooseFigure[1]) / 2)] = 0
            if (turn == 1) blackCount--
            else whiteCount--
            goToNormal()
            reset(0)
            if (!isKing(x, y)) {
                if (turn == 1) {
                    if (whitecutDownBlack(x, y)) {
                        move = false
                        massacre = 1
                        goToClue()
                        choosingFigure = 1
                        chooseFigure[0] = x
                        chooseFigure[1] = y
                        chooseFigure[2] = 1
                    } else {
                        massacre = 0
                        turn *= -1
                        choosingFigure = 0
                    }
                } else {
                    if (blackCutDownWhite(x, y)) {
                        move = false
                        massacre = 1
                        goToClue()
                        choosingFigure = 1
                        chooseFigure[0] = x
                        chooseFigure[1] = y
                        chooseFigure[2] = -1
                    } else {
                        massacre = 0
                        turn *= -1
                        choosingFigure = 0
                    }
                }
            }
            else {
                if (killMoveKing(x, y, turn)) {
                    move = false
                    massacre = 1
                    goToClue()
                    choosingFigure = 1
                    chooseFigure[0] = x
                    chooseFigure[1] = y
                    chooseFigure[2] = turn * 2
                }
                else {
                    massacre = 0
                    turn *= -1
                    choosingFigure = 0
                }
            }
        }

        //Срубить фигуру дамкой
        else if (playing_field[x][y] == 0 && moveField[x][y] == -1 && chooseFigure[2] == turn * 2) {
            move = true
            playing_field[x][y] = chooseFigure[2]
            playing_field[chooseFigure[0]][chooseFigure[1]] = 0
            findDead(x, y, chooseFigure[0], chooseFigure[1])
            if (turn == 1) blackCount--
            else whiteCount--
            goToNormal()
            reset(0)
            if (killMoveKing(x, y, turn)) {
                move = false
                massacre = 1
                goToClue()
                choosingFigure = 1
                chooseFigure[0] = x
                chooseFigure[1] = y
                chooseFigure[2] = turn * 2
            }
            else {
                massacre = 0
                turn *= -1
                choosingFigure = 0
            }
        }
        //Не выбор фигуры
        else if ((playing_field[x][y] != 0) && choosingFigure == 1 && massacre == 0) {
            goToNormal()
            reset(0)
            choosingFigure = 0
        }
        isKing(x, y)
        fillGameField()
        if (move) {
            isOver()
            if (id != -1)
                sendToDataBase()
            binding.textOver.setText("Turn ${if (turn == 1) "white"
                                            else "black"}")
        }
    }

    fun isOver() {
        val t = binding.textOver
        if (turn == -1) {
            if (blackCount == 0 || !canMoveBlack()) {
                t.setText("Белые победили")
            }
        }
        else {
            if (whiteCount == 0 || !canMoveWhite()) {
                t.setText("Черные победили")
            }
        }
    }


    fun findDead(x: Int, y: Int, oldx: Int, oldy: Int) {
        val vecX = if (x > oldx) 1
        else -1
        val vecY = if (y > oldy) 1
        else -1
        var temp = true
        var i = 1
        while(temp) {
            if (playing_field[oldx + vecX * i][oldy + vecY * i] != 0){
                playing_field[oldx + vecX * i][oldy + vecY * i] = 0
                temp = false
            }
            i++
        }
    }


    private fun goToClue() {
        val grid: GridLayout = findViewById(R.id.gridLayout2)
        for (p in 1..63) {
            val x: Int = p % 8
            val y: Int = p / 8
            val cell: ImageView? = getView(x, y, grid)
            if (moveField[x][y] == 1) {
                cell?.setBackgroundColor(Color.YELLOW)
            }
            else if (moveField[x][y] == -1){
                cell?.setBackgroundColor(Color.RED)
            }
        }
    }

    private fun goToNormal() {
        val grid: GridLayout = findViewById(R.id.gridLayout2)
        for (p in 1..63) {
            val x: Int = p % 8
            val y: Int = p / 8
            getView(x, y, grid)?.setBackgroundColor(Color.parseColor(normColor))
        }
    }

    private fun getIndex(view: View, grid: GridLayout): Pair<Int, Int> {
        var x = 0
        var y = 0
        for (i in 0..63) {
            val row = grid.getChildAt(i)
            if (row.id == view.id) {
                x = i % 8
                y = i / 8
            }
        }
        return Pair(x, y)
    }

    private fun getView(x: Int, y: Int, grid: GridLayout): ImageView? {
        val view = grid.getChildAt((8 * y) + x)
        return if (view.id != -1) findViewById(view.id)
        else null
    }

    override fun onDestroy() {
        super.onDestroy()
        resetField()
        resetLogic()
    }


    private fun sendToDataBase(){
        convertToDataModel(dataGame)
        Firebase.firestore.collection("Games")
            .document(id.toString())
            .set(dataGame)
    }

    private fun onChangeListener(doc: DocumentReference) {
        if (id != -1) {
            doc.addSnapshotListener { value, error ->
                    if (value != null){
                        val dataGame = value.toObject(DataBaseModel::class.java)
                        if (dataGame != null) {
                            convertToGameModel(dataGame)
                            fillGameField()
                            if (status != 1)
                                binding.textOver.setText("Turn ${if (turn == 1) "white"
                                                                    else "black"}")
                        }
                    }
                }
        }
    }

}

