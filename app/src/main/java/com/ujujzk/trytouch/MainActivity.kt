package com.ujujzk.trytouch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Word(this))
    }


    class Word(context: Context) : View(context) {

        private val MOVE_UP = 2
        private val MOVE_DOWN = 1
        private val DO_NOT_MOVE = 0


        private val paint = Paint()
        private val textBoundRect = Rect()

        private var centerX: Float = 0f
        private var centerY: Float = 0f
        private var wordCount = 1
        private var text = "word $wordCount"

        private var selfMoveDirection = 0

        private var dragY = 0f

        init {
            this.setWillNotDraw(false)
        }


        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas);

            Log.d("TAG", "Y: $centerY")
            //screen sizes (in fact it is just View sizes)
            val width = getWidth()
            val height = getHeight()

            //set the text on the horizontal center
            centerX = width / 2f

            //set the text on the vertical center (works at the start)
            if (centerY == 0f) {
                centerY = height / 2f
            }

            //make a background
            paint.color = Color.WHITE
            canvas.drawPaint(paint)

            //make the text
            paint.color = Color.BLUE
            paint.style = Paint.Style.FILL
            paint.isAntiAlias = true
            paint.textSize = 100f

            //sizes of rectangle that is made by the text
            paint.getTextBounds(text, 0, text.length, textBoundRect)
            val mTextWidth = paint.measureText(text)
            val mTextHeight = textBoundRect.height()
            //draw the text
            canvas.drawText(
                text,
                centerX - (mTextWidth / 2f),
                centerY + (mTextHeight / 2f),
                paint
            )

            if (selfMoveDirection == MOVE_UP) {

                centerY -= 30
                invalidate()

                if (centerY < 1) {
                    selfMoveDirection = DO_NOT_MOVE
                    nextWord()
                    centerY = height / 2f
                    invalidate()
                }

            } else if (selfMoveDirection == MOVE_DOWN) {

                Log.d("TAG", "downY: $centerY")
                centerY += 30
                //TimeUnit.MILLISECONDS.sleep(1);
                invalidate();

                if (centerY > height) {
                    selfMoveDirection = DO_NOT_MOVE
                    nextWord()
                    centerY = height / 2f
                    invalidate()
                }
            }
        }


        override fun onTouchEvent(event: MotionEvent): Boolean {

            // define Y-coordinate of the Touch-event
            val evY = event.getY();

            when (event.getAction()) {

                MotionEvent.ACTION_DOWN -> {
                    dragY = evY - centerY; //difference between the Touch-event and the center of the text
                }

                MotionEvent.ACTION_MOVE -> {
                    centerY = evY - dragY  //define new center of the text
                    invalidate()           //re-draw the text
                }

                MotionEvent.ACTION_UP -> {

                    //Log.d("MyTag", "2w: "+width);
                    //Log.d("MyTag", "2h: "+height);

                    if ((evY - dragY) < (height / 4)) { //upper quoter of the screen "I KNOW"

                        selfMoveDirection = MOVE_UP
                        invalidate()

                    } else if ((evY - dragY) > (height * 3 / 4)) { //lower quoter of the screen "I FORGOT"

                        selfMoveDirection = MOVE_DOWN
                        invalidate()

                        Log.d("MyTag", "downY: $centerY")

                    } else if (Math.abs(centerY - height / 2) < 20) {

                        translateWord()

                    } else {
                        centerY = height / 2f
                    }

                    invalidate()               //re-draw the text
                }
            }
            return true
        }


        private fun nextWord() {
            wordCount++
            text = "word $wordCount"
        }

        private fun translateWord() {
            text = if (text == "word $wordCount") "translate $wordCount" else "word $wordCount"
        }


    }
}
