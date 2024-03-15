package com.shamanshs.russian_checkers.tools

class DataBaseModel {
    var turn: Int = 1
    var id = -1
    var status = 0
    var blackCount: Int = 0
    var whiteCount: Int = 0
    var field: MutableList<Int> = MutableList(64) { 0 }
}