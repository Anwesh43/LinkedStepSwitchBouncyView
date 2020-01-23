package com.anwesh.uiprojects.stepswitchbouncyview

/**
 * Created by anweshmishra on 23/01/20.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.app.Activity
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas

val nodes : Int = 5
val switches : Int = 5
val scGap : Float = 0.02f
val strokeFactor : Float = 90f
val delay : Long = 20
val switchColor : Int = Color.parseColor("#3F51B5")
val foreColor : Int = Color.parseColor("#757575")
val backColor : Int = Color.parseColor("#FAFAFA")

fun Int.inverse() : Float = 1f / this
fun Float.divideScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.maxScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()
