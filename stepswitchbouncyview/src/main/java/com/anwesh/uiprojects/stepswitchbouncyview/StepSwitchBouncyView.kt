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
val rFactor : Float = 8f

fun Int.inverse() : Float = 1f / this
fun Float.divideScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.maxScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawStepSwitch(i : Int, scale : Float, w : Float, paint : Paint) {
    paint.color = switchColor
    val gap : Float = w / (switches)
    val sf : Float = scale.sinify().divideScale(i, switches)
    val r : Float = gap / rFactor
    save()
    drawLine(0f, 0f, gap * sf, 0f, paint)
    drawCircle(r + gap * sf, 0f, r, paint)
    restore()
}

fun Canvas.drawStepSwitches(scale : Float, w : Float, paint : Paint) {
    val scDiv : Double = 1.0 / switches
    val i : Int = Math.floor(scale.toFloat() / scDiv).toInt()
    for (j in 0..(i)) {
        drawStepSwitch(j, scale, w, paint)
    }
}

fun Canvas.drawFullLine(w : Float, paint : Paint) {
    paint.color = foreColor
    drawLine(0f, 0f, w, 0f, paint)
}

fun Canvas.drawSSBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    save()
    translate(0f, gap * (i + 1))
    drawStepSwitches(scale, w, paint)
    drawFullLine(w, paint)
    restore()
}

class StepSwitchBouncyView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}