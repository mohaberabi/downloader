package com.example.downloader

import ButtonState
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context,
    attrs,
    defStyleAttr
) {


    // i tried to my best to draw this circle animation i was not able ver to do it
    // it's so challenging  and hard for me ,
    private var attrsColor: Int = Color.GRAY
    private var attrsText: String = ""

    private var arcProgress = 0f
    private var widthSize = 0f
    private var heightSize = 0f
    private val buttonAnimator = ValueAnimator()
    private var animatedButtonWidth = 0f
    private var buttonState: ButtonState by Delegates.observable(ButtonState.Initial) { _, old, new ->

        if (old != new) {
            if (new == ButtonState.Completed || new == ButtonState.Initial) {

                buttonAnimator.end()
                circleAnimator.end()
            }
        }
        invalidate()

    }
    private val circleAnimator = ValueAnimator.ofFloat(0f, 100f)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0, 0
        ).apply {
            attrsColor = getColor(R.styleable.LoadingButton_loadingColor, Color.GRAY)
            attrsText = getString(R.styleable.LoadingButton_defaultText) ?: ""

        }
        buttonAnimator.apply {
            duration = 1000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                animatedButtonWidth = animation.animatedValue as Float
                invalidate()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    buttonState = ButtonState.Initial
                }
            })
        }

        circleAnimator.apply {
            duration = 1000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                arcProgress = animation.animatedValue as Float
                invalidate()
            }

        }


    }

    private val defaultPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.gray)
        isAntiAlias = true
    }

    private val bluePaint = Paint().apply {
        color = attrsColor
        isAntiAlias = true
    }


    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    val paintArc = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
    }

    private fun Canvas.presistDefaultButtonBg() {
        drawRect(
            0f,
            0f,
            widthSize,
            heightSize,
            defaultPaint

        )
    }

    private fun Canvas.drawLoadingCircle() {
        val rect = RectF(
            740f,
            50f,
            810f,
            110f
        )
        drawArc(rect, 0f, (360 * (arcProgress / 100)), true, paintArc)
    }

    private fun Canvas.drawButtonLoading() {
        drawRect(
            0f,
            0f,
            animatedButtonWidth,
            heightSize,
            bluePaint,
        )
    }

    private fun Canvas.drawTextForState() {
        val text = when (buttonState) {
            ButtonState.Completed -> "Completed"
            ButtonState.Loading -> "Loading"
            else -> attrsText
        }
        drawText(text, widthSize / 2f, heightSize / 2f, textPaint)
    }


    fun setState(newState: ButtonState) {
        buttonState = newState
        startLoadingAnimation()
    }

    fun reset() {
        buttonState = ButtonState.Initial
        buttonAnimator.end()
        circleAnimator.end()
    }

    private fun startLoadingAnimation() {
        buttonAnimator.setFloatValues(0f, width.toFloat())
        buttonAnimator.start()
        circleAnimator.start()

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            presistDefaultButtonBg()
            drawTextForState()
            if (buttonState == ButtonState.Loading) {
                drawButtonLoading()
                drawLoadingCircle()
            }
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w.toFloat()
        heightSize = h.toFloat()
        setMeasuredDimension(w, h)
    }

}
