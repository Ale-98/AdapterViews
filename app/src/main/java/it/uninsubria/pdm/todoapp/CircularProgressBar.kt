package it.uninsubria.pdm.todoapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.min

class CircularProgressBar(context: Context, attrs:AttributeSet):View(context, attrs) {

    private val mDrawRect: RectF = RectF(0f, 0f, 0f, 0f)
    private val paint: Paint = Paint()
    var percentage : Float = 0.75f
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    override fun onDraw(canvas: Canvas) {
// let us draw by calling the method of the super class
        super.onDraw(canvas);
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        val max_size = min(width, height)
        paint.strokeWidth = max_size*0.25f
        val pad = paint.strokeWidth *0.6f
        mDrawRect.set(0f+pad, 0f+pad, width.toFloat()-pad, height.toFloat()-pad)
        var startAngle = 0f;
        var drawTo = startAngle + (percentage * 360);
        paint.color = Color.LTGRAY
        canvas.drawArc(mDrawRect, 0f, 360f, false, paint)
        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        canvas.drawArc(mDrawRect, startAngle, drawTo, false, paint)
    }

}