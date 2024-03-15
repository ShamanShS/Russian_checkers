//package com.shamanshs.russian_checkers.tools
//
//object figures {
//    var playing_field: Array<Array<Int>> = Array(8) { Array(8) { 0 } }
//    var blackCount: Int = 0
//    var whiteCount: Int = 0
//
//    fun fillField() {
//        for (p in 0..63){
//            var i: Int = p % 8
//            var j: Int = p / 8
//            if (i < 3) {
//                if (i == 0 || i == 2) {
//                    if (j % 2 == 1) {
//                        playing_field[j][i] = -1
//                        blackCount++
//                    }
//                }
//                else {
//                    if (j % 2 == 0) {
//                        playing_field[j][i] = -1
//                        blackCount++
//                    }
//                }
//            }
//            if (i > 4) {
//                if (i == 7 || i == 5) {
//                    if (j % 2 == 0) {
//                        playing_field[j][i] = 1
//                        whiteCount++
//                    }
//                }
//                else {
//                    if (j % 2 == 1) {
//                        playing_field[j][i] = 1
//                        whiteCount++
//                    }
//                }
//            }
//        }
//    }
//}