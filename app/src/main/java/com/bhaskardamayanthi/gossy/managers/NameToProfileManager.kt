package com.bhaskardamayanthi.gossy.managers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

object NameToProfileManager {
    fun textAsBitmap(text: String, textSize: Float, textColor: Int, backgroundColor: Int): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT

        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)

        val width = textBounds.width() + 40 // Padding
        val height = textBounds.height() + 20 // Padding

        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)

        val backgroundPaint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val smallerTextSize = textSize * 1f // Adjust this multiplier as needed
        paint.textSize = smallerTextSize

        val textX = (width - textBounds.width()) / 2f - textBounds.left
        val textY = (height + textBounds.height()) / 2f - textBounds.bottom
        canvas.drawText(text, textX, textY, paint)

        return image
    }
     fun getProfileInitial(name: String): String {
        return if (name.isNotBlank()) {
            val initials = name.trim().split(" ")
                .mapNotNull { it.firstOrNull()?.toUpperCase() }
                .joinToString("")
            if (initials.isNotEmpty()) initials else name.first().toString().toUpperCase()
        } else {
            ""
        }
    }

     fun getRandomColor(): Int {

        return Color.WHITE
    }
}