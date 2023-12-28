package faks.android.smartexpenses.contoller

import android.graphics.*
import com.squareup.picasso.Transformation

class ImageTransformer(private val radius: Int, private val margin: Int) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(source.width, source.height, source.config)

        val canvas = Canvas(output)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val rect = RectF(margin.toFloat(), margin.toFloat(), source.width.toFloat() - margin, source.height.toFloat() - margin)
        canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)

        if (source != output) {
            source.recycle()
        }

        return output
    }

    override fun key(): String {
        return "rounded_corners_radius_${radius}_margin_${margin}"
    }
}