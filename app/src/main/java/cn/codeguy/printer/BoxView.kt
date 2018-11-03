package cn.codeguy.printer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent
import android.view.TouchDelegate
import java.util.*


/**
 * 两个矩形叠加的View
 */
class BoxView : View {
    var list: LinkedList<PointF>? = null

    var rectList: MutableList<RectF>
    val DEFAULT_VALUE = 500F
    var maxValue = 600F


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
        rectList.add(RectF(126F, 430F, 800F, 821F))
        rectList.add(RectF(200F, 305F, 650F, 1061F))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        list!!.clear()


        for (rect in rectList) {
            paint.color = Color.BLUE
            paint.style = Paint.Style.STROKE
            drawCustomRectF(canvas, rect)

//          (left,top) (right,top)
//          (left,bottom) (right,bottom)

            if (firstRect == null) {
                firstRect = rect
            } else {
                paint.color = Color.YELLOW
                paint.style = Paint.Style.FILL
                var left = Math.max(firstRect!!.left, rect.left)
                var top = Math.max(firstRect!!.top, rect.top)
                var right = Math.min(firstRect!!.right, rect.right)
                var bottom = Math.min(firstRect!!.bottom, rect.bottom)
                var rectF = RectF(left, top, right, bottom)
                canvas!!.drawRect(rectF, paint)

                paint.color = Color.BLUE
                paint.style = Paint.Style.STROKE
                drawCustomRectF(canvas, rectF)
                firstRect = null
            }
        }

        if (index != -1) {
            drawPoint(canvas!!, list!![index].x, list!![index].y)
            index = -1
        }

//        paint.style=Paint.Style.FILL
//        paint.color= Color.RED
    }


    var index = -1

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


    private fun drawCustomRectF(canvas: Canvas?, rect: RectF) {
        canvas!!.drawRect(rect, paint)

        if (index==-1){
            var left = rect.left
            var top = rect.top
            var right = rect.right
            var bottom = rect.bottom
            drawPoint(canvas, left, top)
            drawPoint(canvas, right, top)
            drawPoint(canvas, left, bottom)
            drawPoint(canvas, right, bottom)
        }

        list!!.addAll(rect.pointsFromRect())
    }

    private fun drawPoint(canvas: Canvas, x: Float, y: Float) {
        canvas.drawText("(" + x.toInt() + "," + y.toInt() + ")", x, y, paint)
    }
}

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

