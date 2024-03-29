package hu.ait.tictactoeandroid.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.MainThread
import hu.ait.tictactoeandroid.MainActivity
import hu.ait.tictactoeandroid.R
import hu.ait.tictactoeandroid.model.TicTacToeModel

class TicTacToeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    lateinit var paintBg: Paint
    lateinit var paintCircle: Paint
    lateinit var paintCross: Paint
    lateinit var paintLine: Paint
    lateinit var paintText: Paint
    var isOver: Boolean = false
    lateinit var bitmapBackground: Bitmap

    // private var circles = mutableListOf<PointF>()

    init {
        paintBg = Paint()
        paintBg.color = Color.BLACK
        paintBg.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintCircle = Paint()
        paintCircle.color = Color.BLUE
        paintCircle.style = Paint.Style.STROKE
        paintCircle.strokeWidth = 5f

        paintCross = Paint()
        paintCross.color = Color.RED
        paintCross.style = Paint.Style.STROKE
        paintCross.strokeWidth = 5f

        paintText = Paint()
        paintText.textSize = 400f
        paintText.color = Color.RED
        isOver = false

        bitmapBackground = BitmapFactory.decodeResource(
            context?.resources,
            R.drawable.blueback
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBg)

        // Start game timer
        (context as MainActivity).startTimer()

        drawGameArea(canvas!!)
        drawPlayers(canvas!!)

        // Check if game is over and create respective messages
        isOver = TicTacToeModel.gameOver(canvas!!, height.toFloat(), width.toFloat())
        if (isOver) {
            var msg = ""
                if (TicTacToeModel.result == TicTacToeModel.CIRCLE) {
                    msg = context.getString(R.string.winner_msg, "circle")
                } else if (TicTacToeModel.result == TicTacToeModel.CROSS) {
                    msg = context.getString(R.string.winner_msg, "cross")
                } else {
                    msg = context.getString(R.string.draw_game)
                }
            (context as MainActivity).showTextMessage(msg)
            (context as MainActivity).showMessage(msg)
            (context as MainActivity).stopTimer()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmapBackground = Bitmap.createScaledBitmap(
            bitmapBackground, width, height, false)
        paintText.textSize = height/3f
    }

    private fun drawGameArea(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        // two horizontal lines
        canvas.drawLine(
            0f, (height / 3).toFloat(), width.toFloat(), (height / 3).toFloat(),
            paintLine
        )
        canvas.drawLine(
            0f, (2 * height / 3).toFloat(), width.toFloat(),
            (2 * height / 3).toFloat(), paintLine
        )

        // two vertical lines
        canvas.drawLine(
            (width / 3).toFloat(), 0f, (width / 3).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (2 * width / 3).toFloat(), 0f, (2 * width / 3).toFloat(), height.toFloat(),
            paintLine
        )
    }

    private fun drawPlayers(canvas: Canvas) {
        for (i in 0..2) {
            for (j in 0..2) {
                if (TicTacToeModel.getFieldContent(i, j) == TicTacToeModel.CIRCLE) {
                    val centerX = (i * width / 3 + width / 6).toFloat()
                    val centerY = (j * height / 3 + height / 6).toFloat()
                    val radius = height / 6 - 4

                    canvas.drawCircle(centerX, centerY, radius.toFloat(), paintCircle)
                } else if (TicTacToeModel.getFieldContent(i, j) == TicTacToeModel.CROSS) {
                    canvas.drawLine((i * width / 3).toFloat(), (j * height / 3).toFloat(),
                        ((i + 1) * width / 3).toFloat(),
                        ((j + 1) * height / 3).toFloat(), paintCross)

                    canvas.drawLine(((i + 1) * width / 3).toFloat(), (j * height / 3).toFloat(),
                        (i * width / 3).toFloat(), ((j + 1) * height / 3).toFloat(), paintCross)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_DOWN && !isOver) {

            val tX = event.x.toInt() / (width / 3)
            val tY = event.y.toInt() / (height / 3)

            if (tX < 3 && tY < 3 && TicTacToeModel.getFieldContent(tX, tY) ==
                TicTacToeModel.EMPTY) {
                TicTacToeModel.setFieldContent(tX, tY, TicTacToeModel.getNextPlayer())
                TicTacToeModel.changeNextPlayer()
                invalidate()

                var nextMessage = context.getString(R.string.text_next_player, "X")
                if (TicTacToeModel.getNextPlayer() == TicTacToeModel.CIRCLE){
                    nextMessage = context.getString(R.string.text_next_player, "O")
                }
                (context as MainActivity).showTextMessage(nextMessage)
                (context as MainActivity).resetTimer()
            }
        }

        return true
    }

    public fun resetGame() {
        TicTacToeModel.resetModel()
        var nextMessage = context.getString(R.string.text_next_player, "X")
        (context as MainActivity).showTextMessage(nextMessage)
        (context as MainActivity).resetTimer()
        invalidate()
    }
}