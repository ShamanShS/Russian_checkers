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
import com.shamanshs.russian_checkers.tools.GameLogic.accessMove
import com.shamanshs.russian_checkers.tools.GameLogic.accessMoveKing
import com.shamanshs.russian_checkers.tools.GameLogic.blackCutDownWhite
import com.shamanshs.russian_checkers.tools.GameLogic.canKill
import com.shamanshs.russian_checkers.tools.GameLogic.canMoveBlack
import com.shamanshs.russian_checkers.tools.GameLogic.canMoveWhite
import com.shamanshs.russian_checkers.tools.GameLogic.isKing
import com.shamanshs.russian_checkers.tools.GameLogic.killMoveKing
import com.shamanshs.russian_checkers.tools.GameLogic.move_field
import com.shamanshs.russian_checkers.tools.GameLogic.reset
import com.shamanshs.russian_checkers.tools.GameLogic.whitecutDownBlack
import com.shamanshs.russian_checkers.tools.figures.blackCount
import com.shamanshs.russian_checkers.tools.figures.fillField
import com.shamanshs.russian_checkers.tools.figures.playing_field
import com.shamanshs.russian_checkers.tools.figures.whiteCount

class GameActivity : AppCompatActivity() {
    private val normColor: String = "#453737"
    private var choosingFigure: Int = 0
    private var chooseFigure: Array<Int> = Array(3) { 0 }
    private var turn: Int = 1
    private var massacre: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
        val g: GridLayout = findViewById(R.id.gridLayout2)
        val (x, y) = getIndex(v, g)
        val t = findViewById<TextView>(R.id.coor)
        var move = false
//        var kill = false
//        if (canKill(turn)){
//            kill = true
//        }
        Log.d("My Log", "${chooseFigure[2]}  ${turn * 2}  ${playing_field[x][y]}")
//        t.setText("$x $y  turn:$turn   ${playing_field[x][y]}")

        //Выбор черной фигуры
        if ((playing_field[x][y] == -1 || playing_field[x][y] == -2) && choosingFigure == 0 && turn == -1 && massacre == 0 &&
            (!(!canKill(turn) || move_field[x][y] != 2) || !(canKill(turn) || move_field[x][y] == 2))) {
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
            (!(!canKill(turn) || move_field[x][y] != 2) || !(canKill(turn) || move_field[x][y] == 2))){
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
        else if (playing_field[x][y] == 0 && move_field[x][y] == 1 && (chooseFigure[2] == turn || chooseFigure[2] == turn * 2 )) {
            move = true
            playing_field[x][y] = chooseFigure[2]
            playing_field[chooseFigure[0]][chooseFigure[1]] = 0
            goToNormal()
            reset(0)
            choosingFigure = 0
            turn *= -1

        }
        //Срубить фигуру
        else if (playing_field[x][y] == 0 && move_field[x][y] == -1 && chooseFigure[2] == turn) {
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
        else if (playing_field[x][y] == 0 && move_field[x][y] == -1 && chooseFigure[2] == turn * 2) {
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
        if (move)
            isOver()
    }

    fun isOver() {
        val t = findViewById<TextView>(R.id.coor)
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
            if (move_field[x][y] == 1) {
                if (cell != null) {
                    cell.setBackgroundColor(Color.YELLOW)
                }
            }
            else if (move_field[x][y] == -1){
                if (cell != null) {
                    cell.setBackgroundColor(Color.RED)
                }
            }
        }
    }

    private fun goToNormal() {
        val grid: GridLayout = findViewById(R.id.gridLayout2)
        for (p in 1..63) {
            val x: Int = p % 8
            val y: Int = p / 8
            val cell: ImageView? = getView(x, y, grid)
            if (cell != null) {
                cell.setBackgroundColor(Color.parseColor(normColor))
            }
        }
    }

    private fun getIndex(view: View, grid: GridLayout): Pair<Int, Int> {
        var x = 0
        var y = 0
        for (i in 0..63) {
            var row = grid.getChildAt(i)
            if (row.id == view.id) {
                x = i % 8
                y = i / 8
            }
        }
        return Pair(x, y)
    }

    private fun getView(x: Int, y: Int, grid: GridLayout): ImageView? {
        val view = grid.getChildAt((8 * y) + x)
        if (view.id != -1) return findViewById(view.id)
        else return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("My Log", "onGameDestroy")
    }

    fun go(view: View) {
        fillField()
        fillGameField()
    }
}