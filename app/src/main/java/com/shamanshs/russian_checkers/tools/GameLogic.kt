package com.shamanshs.russian_checkers.tools

import android.util.Log
import com.shamanshs.russian_checkers.tools.figures.playing_field


object GameLogic {
    var move_field: Array<Array<Int>> = Array(8) { Array(8) { 0 } }

    /**x and y coordinates, colorF color figure */
    fun accessMove(x: Int, y: Int, colorF: Int): Boolean {
        var flag = false
        if (colorF == 1) {
            if (whiteOneLMove(x, y)) {
                if (playing_field[x - 1][y - 1] == 0) {
                    move_field[x - 1][y - 1] = 1
                    flag = true
                }
            }
            if (whiteOneRMove(x, y)) {
                if (playing_field[x + 1][y - 1] == 0){
                    move_field[x + 1][y - 1] = 1
                    flag = true
                }
            }
            if (whitecutDownBlack(x, y)) {
                reset(1)
                flag = true
            }
        }
        else if (colorF == -1) {
            if (blackOneLMove(x, y)) {
                if (playing_field[x - 1][y + 1] == 0){
                    move_field[x - 1][y + 1] = 1
                    flag = true
                }
            }
            if (blackOneRMove(x, y)) {
                if (playing_field[x + 1][y + 1] == 0){
                    move_field[x + 1][y + 1] = 1
                    flag = true
                }
            }
            if (blackCutDownWhite(x, y)){
                reset(1)
                flag = true
            }
        }
        return flag
    }

    fun whitecutDownBlack(x: Int, y: Int): Boolean {
        var flag = false
        if (whiteTwoLMove(x ,y)){
            move_field[x - 2][y - 2] = -1
            flag = true
        }
        if (whiteTwoRMove(x ,y)){
            move_field[x + 2][y - 2] = -1
            flag = true
        }
        if (whiteTwoLBMove(x ,y)){
            move_field[x - 2][y + 2] = -1
            flag = true
        }
        if (whiteTwoRBMove(x ,y)){
            move_field[x + 2][y + 2] = -1
            flag = true
        }
        return flag
    }

    fun blackCutDownWhite(x: Int, y: Int): Boolean {
        var flag = false
        if (blackTwoLMove(x ,y)){
            move_field[x - 2][y + 2] = -1
            flag = true
        }
        if (blackTwoRMove(x ,y)){
            move_field[x + 2][y + 2] = -1
            flag = true
        }
        if (blackTwoLBMove(x ,y)){
                move_field[x - 2][y - 2] = -1
                flag = true
        }
        if (blackTwoRBMove(x ,y)){
                move_field[x + 2][y - 2] = -1
                flag = true
        }
        return flag
    }


    fun reset(state: Int) {
        for (x in 0..7){
            for(y in 0 .. 7) {
                if (state == 0) {
                    move_field[x][y] = 0
                }
                else if (state == 1){
                    if (move_field[x][y] == 1)
                        move_field[x][y] = 0
                }
                else if (state == 2) {
                    if (move_field[x][y] != 2)
                        move_field[x][y] = 0
                }
            }
        }
    }

    /**x - 1; y - 1 */
    fun whiteOneLMove(x: Int, y: Int): Boolean {
        return x - 1 >= 0 && y - 1 >= 0
    }
    /**x + 1; y - 1 */
    fun whiteOneRMove(x: Int, y: Int): Boolean {
        return x + 1 < playing_field.size && y - 1 >= 0
    }
    /**x - 1; y + 1 */
    fun blackOneLMove(x: Int, y: Int): Boolean {
        return x - 1 >= 0 && y + 1 < playing_field[x].size
    }
    /**x + 1; y + 1 */
    fun blackOneRMove(x: Int, y: Int): Boolean {
        return x + 1 < playing_field.size && y + 1 < playing_field[x].size
    }

    fun whiteTwoLMove(x: Int, y: Int): Boolean{
        var flag = false
        if (x - 2 >= 0 && y - 2 >= 0) {
            if (playing_field[x - 1][y - 1] < 0 && playing_field[x - 2][y - 2] == 0) {
                flag = true
            }
        }
        return flag
    }

    fun whiteTwoRMove(x: Int, y: Int): Boolean{
        var flag = false
        if (x + 2 < playing_field.size && y - 2 >= 0) {
            if (playing_field[x + 1][y - 1] < 0 && playing_field[x + 2][y - 2] == 0) {
                flag = true
            }
        }
        return flag
    }
    fun whiteTwoLBMove(x: Int, y: Int): Boolean{
        var flag = false
        if (x - 2 >= 0 && y + 2 < playing_field.size) {
            if (playing_field[x - 1][y + 1] < 0 && playing_field[x - 2][y + 2] == 0) {
                flag = true
            }
        }
        return flag
    }

    fun whiteTwoRBMove(x: Int, y: Int): Boolean {
        var flag = false
        if (x + 2 < playing_field.size && y + 2 < playing_field.size){
            if (playing_field[x + 1][y + 1] < 0 && playing_field[x + 2][y + 2] == 0) {
                flag = true
            }
        }
        return flag
    }

    fun blackTwoLMove(x: Int, y: Int): Boolean{
        var flag = false
        if (x - 2 >= 0 && y + 2 < playing_field.size) {
            if (playing_field[x - 1][y + 1] > 0 && playing_field[x - 2][y + 2] == 0) {
                flag = true
            }
        }
        return flag
    }

    fun blackTwoRMove(x: Int, y: Int): Boolean {
        var flag = false
        if (x + 2 < playing_field.size && y + 2 < playing_field.size){
            if (playing_field[x + 1][y + 1] > 0 && playing_field[x + 2][y + 2] == 0) {
                flag = true
            }
        }
        return flag
    }

    fun blackTwoLBMove(x: Int, y: Int): Boolean{
        var flag = false
        if (x - 2 >= 0 && y - 2 >= 0) {
            if (playing_field[x - 1][y - 1] > 0 && playing_field[x - 2][y - 2] == 0) {
                flag = true
            }
        }
        return flag
    }

    fun blackTwoRBMove(x: Int, y: Int): Boolean{
        var flag = false
        if (x + 2 < playing_field.size && y - 2 >= 0) {
            if (playing_field[x + 1][y - 1] > 0 && playing_field[x + 2][y - 2] == 0) {
                flag = true
            }
        }
        return flag
    }

    fun isKing(x: Int, y: Int): Boolean{
        var flag = false
        if (playing_field[x][y] == 1 && y == 0){
            playing_field[x][y] = 2
            flag = true
        }
        if (playing_field[x][y] == -1 && y == 7){
            playing_field[x][y] = -2
            flag = true
        }
        return flag
    }

    fun accessMoveKing(x: Int, y: Int, colorF: Int): Boolean {
        var flag = false
        val vec: Array<Int> = Array(2, { 1 })
        for (i in 0..3){
            var temp = true
            vec[i % 2] *= -1
            var j = 1
            while (temp) {
                Log.d("My Log", "${x + (j * vec[0])}  ${y + (j * vec[1])}")
                if (framework(x, y, vec, j)) {
                    if (playing_field[x + (j * vec[0])][y + (j * vec[1])] == 0) {
                        move_field[x + (j * vec[0])][y + (j * vec[1])] = 1
                        j++
                        flag = true
                    }
                    else
                        temp = false
                }
                else
                    temp = false
            }
        }
        val temp = if (killMoveKing(x ,y, colorF)){
            reset(1)
            true
        }
        else false
        return flag || temp
    }

    fun killMoveKing(x: Int, y: Int, colorF: Int): Boolean {
        var flag = false
        var vec: Array<Int> = Array(2, { 1 })
        for (i in 0..3){
            var kill = false
            var temp = true
            vec[i % 2] *= -1
            var j = 1
            while (temp) {
                if (framework(x, y, vec, j)) {
                    if ((playing_field[x + (j * vec[0])][y + (j * vec[1])] == colorF * -1 ||
                        playing_field[x + (j * vec[0])][y + (j * vec[1])] == colorF * -2) && !kill) {
                        kill = true
                    }
                    else if (playing_field[x + (j * vec[0])][y + (j * vec[1])] == 0 && kill) {
                        move_field[x + (j * vec[0])][y + (j * vec[1])] = -1
                        flag = true
                    }
                    else if (playing_field[x + (j * vec[0])][y + (j * vec[1])] != 0 && kill) {
                        kill = false
                        temp = false
                    }
                }
                else
                    temp = false
                j++
            }
        }
        return flag
    }

    fun framework(x: Int, y: Int, vec: Array<Int>, j: Int): Boolean {
        var flag = false
        if (x + (j * vec[0]) >= 0 && x + (j * vec[0]) < playing_field.size){
            if (y + (j * vec[1]) >= 0 && y + (j * vec[1]) < playing_field.size) {
                flag = true
            }
        }
        return flag
    }

    fun canMoveWhite(): Boolean {
        var flag = false
        for (x in 0..7){
            for(y in 0 .. 7){
                if (playing_field[x][y] == 1) {
                    flag = accessMove(x ,y, 1)
                }
                else if (playing_field[x][y] == 2) {
                    flag = accessMoveKing(x ,y, 1)
                }
                if (flag)
                    break
            }
            if (flag)
                break
        }
        reset(0)
        return flag
    }

    fun canMoveBlack(): Boolean {
        var flag = false
        for (x in 0..7){
            for(y in 0 .. 7){
                if (playing_field[x][y] == -1) {
                    flag = accessMove(x ,y, -1)
                }
                else if (playing_field[x][y] == -2) {
                    flag = accessMoveKing(x ,y, -1)
                }
                if (flag)
                    break
            }
            if (flag)
                break
        }
        reset(0)
        return flag
    }

    fun canKill(colorF: Int): Boolean {
        var flag = false
        for (x in 0..7) {
            for (y in 0..7) {
                if (playing_field[x][y] > 0 && colorF == 1) {
                    if (playing_field[x][y] == 1) {
                        if (whitecutDownBlack(x, y)) {
                            reset(2)
                            flag = true
                            move_field[x][y] = 2
                        }
                    }
                    else if (playing_field[x][y] == 2){
                        if (killMoveKing(x, y, colorF)) {
                            reset(2)
                            flag = true
                            move_field[x][y] = 2
                        }
                    }
                }
                else if (playing_field[x][y] < 0 && colorF == -1){
                    if (playing_field[x][y] == -1) {
                        if (blackCutDownWhite(x, y)) {
                            reset(2)
                            flag = true
                            move_field[x][y] = 2
                        }
                    }
                    else if (playing_field[x][y] == -2){
                        if (killMoveKing(x, y, colorF)) {
                            reset(2)
                            flag = true
                            move_field[x][y] = 2
                        }
                    }
                }
            }
        }
        return flag
    }
}