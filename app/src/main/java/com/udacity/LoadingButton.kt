package com.udacity


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates


private enum class StatusButton(val label: Int) {
    DOWNLOAD(R.string.button_download),
    LOADING(R.string.button_loading),
}

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    var downloadState = "download"
        set(value) {
            invalidate()
            field = value
        }


    var progress = 0
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {

        color = Color.BLACK
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    var xpos = (measuredWidth.toFloat() * 50 / 100)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        xpos = (measuredWidth.toFloat() * 50 / 100)
        canvas?.drawRect(0f,
            0f,
            progress.toFloat() / 100 * measuredWidth,
            measuredHeight.toFloat(),
            paint.apply { color = resources.getColor(R.color.colorPrimaryDark) })

        canvas?.drawArc(
            RectF(xpos * 1.4f, 30f, xpos * 1.4f + 100, 130f),
            0f,
            progress.toFloat() * 360f / 100f,
            true,
            paint.apply {
                color = resources.getColor(R.color.colorAccent)

            }
        )
        canvas?.drawText(downloadState, xpos, 100f, paint.apply {
            color = resources.getColor(R.color.white)
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}