package cn.codeguy.printer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent
import java.util.*


/**
 * N个矩形叠加的View
 */
class NBoxView : View {
    var list: LinkedList<PointF>? = null

    var rectList: MutableList<RectF>
    /**
     * 默认宽度高度
     */
    val DEFAULT_VALUE = 500F

    /**
     * 最大值，用于尺寸的缩放
     */
    var maxValue = 600F

    /**
     * 点击的坐标位于点集合中的位置
     */
    var index = -1

    var paint = Paint()
    var firstRect: RectF? = null

    init {
        paint.strokeWidth = 2F
        paint.textSize = 24F
        rectList = mutableListOf()
        list = LinkedList()
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        rectList.add(RectF(20F, 230F, 400F, 682F))
        rectList.add(RectF(126F, 430F, 800F, 821F))
        rectList.add(RectF(200F, 305F, 650F, 1061F))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        list!!.clear()

        for (rect in rectList) {
            list!!.addAll(rect.pointsFromRect())
            if (firstRect == null) {
                firstRect = rect
            } else {
                firstRect = drawInterect(rect, canvas)
            }
        }

        paint.color = Color.YELLOW
        paint.style = Paint.Style.FILL
        canvas!!.drawRect(firstRect, paint)


        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE

        for (rect in rectList) {
            canvas!!.drawRect(rect, paint)
            drawPoints(rect, canvas)
//          (left,top) (right,top)
//          (left,bottom) (right,bottom)
        }
        canvas!!.drawRect(firstRect, paint)
        drawPoints(firstRect, canvas)

        if (index != -1) {
            drawPoint(canvas!!, list!![index].x, list!![index].y)
            index = -1
        }
        firstRect = null

    }

    private fun drawInterect(rect: RectF, canvas: Canvas?): RectF {
        var left = Math.max(firstRect!!.left, rect.left)
        var top = Math.max(firstRect!!.top, rect.top)
        var right = Math.min(firstRect!!.right, rect.right)
        var bottom = Math.min(firstRect!!.bottom, rect.bottom)
        var rectF = RectF(left, top, right, bottom)
        list!!.addAll(rectF.pointsFromRect())
        return rectF
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var rectF = RectF(ev.x, ev.y, ev.x, ev.y)
        list!!.forEach {

            var value = 50F
            var rect = RectF(it.x - value, it.y - value, it.x + value, it.y + value)
            if (index == -1 && rectF.intersect(rect)) {
                index = list!!.indexOf(it)
                invalidate()
            }
        }

        return super.dispatchTouchEvent(ev)
    }


    private fun drawPoints(rect: RectF?, canvas: Canvas) {
        if (index == -1) {
            var left = rect!!.left
            var top = rect!!.top
            var right = rect!!.right
            var bottom = rect!!.bottom
            drawPoint(canvas, left, top)
            drawPoint(canvas, right, top)
            drawPoint(canvas, left, bottom)
            drawPoint(canvas, right, bottom)
        }

    }

    private fun drawPoint(canvas: Canvas, x: Float, y: Float) {
        canvas.drawText("(" + x.toInt() + "," + y.toInt() + ")", x, y, paint)
    }
}

/**
 * add point into collections
 */
private fun RectF.pointsFromRect(): List<PointF> {
    var points = LinkedList<PointF>()
    var point1 = PointF(left, top)
    points.add(point1)

    var point2 = PointF(right, top)
    points.add(point2)

    var point3 = PointF(left, bottom)
    points.add(point3)

    var point4 = PointF(right, bottom)
    points.add(point4)
    return points
}

