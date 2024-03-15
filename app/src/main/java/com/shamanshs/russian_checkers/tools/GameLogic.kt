package com.shamanshs.russian_checkers.tools

import android.util.Log


object GameLogic {
    var playing_field: Array<Array<Int>> = Array(8) { Array(8) { 0 } }
    var blackCount: Int = 0
    var whiteCount: Int = 0
    var moveField: Array<Array<Int>> = Array(8) { Array(8) { 0 } }
    var turn: Int = 1
    var id = -1
    var status = 0
    var youColor = 0


    fun reset(state: Int) {
        for (x in 0..7){
            for(y in 0 .. 7) {
                if (state == 0) {
                    moveField[x][y] = 0
                }
                else if (state == 1){
                    if (moveField[x][y] == 1)
                        moveField[x][y] = 0
                }
                else if (state == 2) {
                    if (moveField[x][y] != 2)
                        moveField[x][y] = 0
                }
            }
        }
    }

    fun resetField() {
        for (x in 0..7) {
            for (y in 0..7) {
                playing_field[x][y] = 0
            }
        }
    }

    fun resetLogic() {
        blackCount = 0
        whiteCount = 0
        turn = 1
        id = -1
        status = 0
        youColor = 0
    }

    fun fillField() {
        for (p in 0..63){
            var i: Int = p % 8
            var j: Int = p / 8
            if (i < 3) {
                if (i == 0 || i == 2) {
                    if (j % 2 == 1) {
                        playing_field[j][i] = -1
                        blackCount++
                    }
                }
                else {
                    if (j % 2 == 0) {
                        playing_field[j][i] = -1
                        blackCount++
                    }
                }
            }
            if (i > 4) {
                if (i == 7 || i == 5) {
                    if (j % 2 == 0) {
                        playing_field[j][i] = 1
                        whiteCount++
                    }
                }
                else {
                    if (j % 2 == 1) {
                        playing_field[j][i] = 1
                        whiteCount++
                    }
                }
            }
        }
    }

    /**x and y coordinates, colorF color figure */
    fun accessMove(x: Int, y: Int, colorF: Int): Boolean {
        var flag = false
        if (colorF == 1) {
            if (whiteOneLMove(x, y)) {
                if (playing_field[x - 1][y - 1] == 0) {
                    moveField[x - 1][y - 1] = 1
                    flag = true
                }
            }
            if (whiteOneRMove(x, y, )) {
                if (playing_field[x + 1][y - 1] == 0){
                    moveField[x + 1][y - 1] = 1
                    flag = true
                }
            }
            if (whitecutDownBlack(x, y, )) {
                reset(1)
                flag = true
            }
        }
        else if (colorF == -1) {
            if (blackOneLMove(x, y, )) {
                if (playing_field[x - 1][y + 1] == 0){
                    moveField[x - 1][y + 1] = 1
                    flag = true
                }
            }
            if (blackOneRMove(x, y, )) {
                if (playing_field[x + 1][y + 1] == 0){
                    moveField[x + 1][y + 1] = 1
                    flag = true
                }
            }
            if (blackCutDownWhite(x, y, )){
                reset(1)
                flag = true
            }
        }
        return flag
    }

    fun whitecutDownBlack(x: Int, y: Int): Boolean {
        var flag = false
        if (whiteTwoLMove(x ,y, )){
            moveField[x - 2][y - 2] = -1
            flag = true
        }
        if (whiteTwoRMove(x ,y, )){
            moveField[x + 2][y - 2] = -1
            flag = true
        }
        if (whiteTwoLBMove(x ,y, )){
            moveField[x - 2][y + 2] = -1
            flag = true
        }
        if (whiteTwoRBMove(x ,y, )){
            moveField[x + 2][y + 2] = -1
            flag = true
        }
        return flag
    }

    fun blackCutDownWhite(x: Int, y: Int): Boolean {
        var flag = false
        if (blackTwoLMove(x ,y, )){
            moveField[x - 2][y + 2] = -1
            flag = true
        }
        if (blackTwoRMove(x ,y, )){
            moveField[x + 2][y + 2] = -1
            flag = true
        }
        if (blackTwoLBMove(x ,y, )){
                moveField[x - 2][y - 2] = -1
                flag = true
        }
        if (blackTwoRBMove(x ,y, )){
                moveField[x + 2][y - 2] = -1
                flag = true
        }
        return flag
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
                if (framework(x, y, vec, j, )) {
                    if (playing_field[x + (j * vec[0])][y + (j * vec[1])] == 0) {
                        moveField[x + (j * vec[0])][y + (j * vec[1])] = 1
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
        val temp = if (killMoveKing(x ,y, colorF, )){
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
                if (framework(x, y, vec, j, )) {
                    if ((playing_field[x + (j * vec[0])][y + (j * vec[1])] == colorF * -1 ||
                        playing_field[x + (j * vec[0])][y + (j * vec[1])] == colorF * -2) && !kill) {
                        kill = true
                    }
                    else if (playing_field[x + (j * vec[0])][y + (j * vec[1])] == 0 && kill) {
                        moveField[x + (j * vec[0])][y + (j * vec[1])] = -1
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
                    flag = accessMove(x ,y, 1, )
                }
                else if (playing_field[x][y] == 2) {
                    flag = accessMoveKing(x ,y, 1, )
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
                    flag = accessMove(x ,y, -1, )
                }
                else if (playing_field[x][y] == -2) {
                    flag = accessMoveKing(x ,y, -1, )
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

    fun canKill(): Boolean {
        var flag = false
        for (x in 0..7) {
            for (y in 0..7) {
                if (playing_field[x][y] > 0 && turn == 1) {
                    if (playing_field[x][y] == 1) {
                        if (whitecutDownBlack(x, y, )) {
                            reset(2)
                            flag = true
                            moveField[x][y] = 2
                        }
                    }
                    else if (playing_field[x][y] == 2){
                        if (killMoveKing(x, y, turn, )) {
                            reset(2)
                            flag = true
                            moveField[x][y] = 2
                        }
                    }
                }
                else if (playing_field[x][y] < 0 && turn == -1){
                    if (playing_field[x][y] == -1) {
                        if (blackCutDownWhite(x, y, )) {
                            reset(2)
                            flag = true
                            moveField[x][y] = 2
                        }
                    }
                    else if (playing_field[x][y] == -2){
                        if (killMoveKing(x, y, turn, )) {
                            reset(2)
                            flag = true
                            moveField[x][y] = 2
                        }
                    }
                }
            }
        }
        return flag
    }

    fun convertToDataModel(dataGame: DataBaseModel) {
        dataGame.id = id
        dataGame.turn = turn
        dataGame.blackCount = blackCount
        dataGame.whiteCount = whiteCount
        dataGame.status = status
        for (x in 0..7){
            for (y in 0..7){
                dataGame.field[(y * 8) + x] = playing_field[x][y]
            }
        }
    }

    fun convertToGameModel(dataGame: DataBaseModel) {
        id = dataGame.id
        turn = dataGame.turn
        blackCount = dataGame.blackCount
        whiteCount = dataGame.whiteCount
        status = dataGame.status
        for (x in 0..7){
            for (y in 0..7){
                playing_field[x][y] = dataGame.field[(y * 8) + x]
            }
        }
    }
}