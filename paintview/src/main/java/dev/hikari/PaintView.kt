package dev.hikari

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class PaintView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paths = mutableListOf<Path>()
    var drawWidth: Float = 5f.dp2px
        set(value) {
            field = value
            paint.strokeWidth = value
            invalidate()
        }

    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            strokeWidth = drawWidth
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val path = Path().also { it.moveTo(event.x, event.y) }
                paths.add(path)
            }
            MotionEvent.ACTION_MOVE -> {
                paths.last().lineTo(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        for (path in paths) {
            canvas.drawPath(path, paint)
        }
    }

    inline fun saveBitmap(block: (Bitmap) -> Unit) {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        canvas.save()
        try {
            block(bitmap)
        } finally {
            bitmap.recycle()
        }
    }

    fun undo() {
        if (paths.isNotEmpty()) {
            paths.removeLast()
            invalidate()
        }
    }

    fun clear() {
        paths.clear()
        invalidate()
    }

    private inline val Float.dp2px
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )
}