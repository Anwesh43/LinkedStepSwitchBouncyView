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

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class SSBNode(var i : Int, val state : State = State()) {

        private var next : SSBNode? = null
        private var prev : SSBNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = SSBNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawSSBNode(i, state.scale, paint)
            next?.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : SSBNode {
            var curr : SSBNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class StepSwitchBouncy(var i : Int) {

        private val root : SSBNode = SSBNode(0)
        private var curr : SSBNode = root
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            root.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : StepSwitchBouncyView) {

        private val animator : Animator = Animator(view)
        private val ssb : StepSwitchBouncy = StepSwitchBouncy(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(backColor)
            ssb.draw(canvas, paint)
            animator.animate {
                ssb.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            ssb.startUpdating {
                animator.start()
            }
        }
    }
}