package com.ujujzk.trytouch.drug

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.View

class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

    private val shadowBorder = ColorDrawable(Color.BLACK)

    private val shadow = ColorDrawable(Color.parseColor(v.tag.toString()))

    // Defines a callback that sends the drag shadow dimensions and touch point back to the system.
    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        // First, we define the shadow width and height. In our example, it will be
        // half of the size of the view that's been dragged.
        val width: Int = view.width / 2
        val height: Int = view.height / 2

        // The drag shadow is a `ColorDrawable`. This sets its dimensions to be the same as the
        // `Canvas` that the system will provide. We leave some room (four pixels) for the shadow border.
        shadow.setBounds(4, 4, width - 4, height - 4)
        shadowBorder.setBounds(0, 0, width, height)

        // Sets the size parameter's width and height values.
        // These get back to the system through the size parameter.
        size.set(width, height)

        // Sets the touch point's position to be in the middle of the drag shadow.
        touch.set(width / 2, height / 2)
    }

    // Defines a callback that draws the drag shadow in a `Canvas` that the
    // system constructs from the dimensions passed in `onProvideShadowMetrics()`.
    override fun onDrawShadow(canvas: Canvas) {

        // Draws the border drawable first.
        shadowBorder.draw(canvas)

        // Draws the actual shadow drawable onto the `Canvas` passed in
        // from the system so that the shadow content is above its border.
        shadow.draw(canvas)
    }
}