package film.search.filmsearch

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import film.search.filmssearch.R

class CustomRatingView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : View(context, attributeSet) {
    private val drawRectangle = RectF()
    private var defaultSize: Float = 50f
    private var radius: Float = defaultSize / 2f
    private var centerX: Float = defaultSize / 2f
    private var centerY: Float = defaultSize / 2f
    private var stroke = 10f
    private var progress = 50
    private var animProgress = 0
    private var ratingTextSize = 60f
    private var color1 = Color.parseColor("#FFE84258")
    private var color2 = Color.parseColor("#FFFD8060")
    private var color3 = Color.parseColor("#FFFEE191")
    private var color4 = Color.parseColor("#FFA0FFA4")
    private var colorBackground = Color.DKGRAY
    private lateinit var strokePaint: Paint
    private lateinit var digitPaint: Paint
    private lateinit var circlePaint: Paint
    private lateinit var barAnimator: ValueAnimator

    init {
        val attr =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomRatingView, 0, 0)
        try {
            stroke = attr.getFloat(
                R.styleable.CustomRatingView_stroke, stroke)
            progress = attr.getInt(R.styleable.CustomRatingView_progress, progress)
            color1 = attr.getColor(R.styleable.CustomRatingView_color1, color1)
            color2 = attr.getColor(R.styleable.CustomRatingView_color2, color2)
            color3 = attr.getColor(R.styleable.CustomRatingView_color3, color3)
            color4 = attr.getColor(R.styleable.CustomRatingView_color4, color4)
            colorBackground = attr.getColor(R.styleable.CustomRatingView_color_background, colorBackground)
            defaultSize = attr.getDimension(R.styleable.CustomRatingView_default_size, defaultSize)
        } finally {
            attr.recycle()
        }
    }

    private fun initPaint() {
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = stroke
            color = getPaintColor(animProgress)
            isAntiAlias = true
        }
        digitPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = ratingTextSize
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(animProgress)
            isAntiAlias = true
        }
        circlePaint = Paint().apply {
            style = Paint.Style.FILL
            color = colorBackground
        }
    }

    private fun getPaintColor(progress: Int): Int = when(progress) {
        in 0 .. 25 -> color1
        in 26 .. 50 -> color2
        in 51 .. 75 -> color3
        else -> color4
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = if (width > height) {
            height.div(2f)
        } else {
            width.div(2f)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = Math.min(chosenWidth, chosenHeight)
        centerX = minSide.div(2f)
        centerY = minSide.div(2f)

        setMeasuredDimension(minSide, minSide)
    }

    override fun onDraw(canvas: Canvas) {
        drawRating(canvas)
        drawText(canvas)
    }

    private fun chooseDimension(mode: Int, size: Int): Int =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> defaultSize.toInt()
        }

    private fun drawRating(canvas: Canvas) {
        val scale = radius * 0.8f
        canvas.save()
        canvas.translate(centerX, centerY)
        drawRectangle.set(0f - scale, 0f - scale, scale , scale)
        canvas.drawCircle(0f, 0f, radius, circlePaint)
        canvas.drawArc(drawRectangle, -90f, convertProgressToDegrees(animProgress), false, strokePaint)
        canvas.restore()
    }

    private fun convertProgressToDegrees(progress: Int): Float = progress * 3.6f

    private fun drawText(canvas: Canvas) {
        val message = String.format("%.1f", animProgress / 10f)
        val widths = FloatArray(message.length)
        digitPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        canvas.drawText(message, centerX - advance / 2, centerY  + advance / 4, digitPaint)
    }

    fun setProgress(pr: Int) {
        setProgress(pr, true)
    }

    fun setProgress(pr: Int, animate: Boolean) {
        if (animate) {
            progress = pr
            animProgress = 0
            barAnimator = ValueAnimator.ofFloat(0f, 1f)
            barAnimator.setDuration(3000)

            // reset progress without animating
            setProgress(0, false)
            barAnimator.setInterpolator(DecelerateInterpolator())
            barAnimator.addUpdateListener(AnimatorUpdateListener { animation ->
                val interpolation = animation.animatedValue as Float
                animProgress = (interpolation * progress).toInt()
                setProgress(animProgress, false)
            })
            if (!barAnimator.isStarted()) {
                barAnimator.start()
            }
        } else {
            initPaint()
//            invalidate()
            postInvalidate()
        }
    }
}
