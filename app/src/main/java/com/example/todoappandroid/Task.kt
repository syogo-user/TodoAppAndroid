package com.example.todoappandroid

import java.io.Serializable
import java.util.*

open class Task: Serializable {
    var id: Int = 0
    var title: String = "" // タイトル
    var contents: String = "" // 内容
    var date: Date = Date() // 日時
}