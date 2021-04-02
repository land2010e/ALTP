package com.t3h.demoaltp

data class Question(
    val question:String,
    val caseA:String,
    val caseB:String,
    val caseC:String,
    val caseD:String,
    val level:Int,
    val trueCase:Int
)