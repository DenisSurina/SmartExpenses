package faks.android.smartexpenses.misc
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import faks.android.smartexpenses.R
import faks.android.smartexpenses.contoller.ImageTransformer

fun ImageView.setupImageWithBorder(iconId: Int, colorId: Int, radius: Int, margin: Int, borderWidth: Int, borderColor: Int = Color.BLACK) {

    Picasso.get()
        .load(iconId)
        .transform(ImageTransformer(radius, margin))
        .into(this)


    val roundedBackground = context?.let { ContextCompat.getDrawable(it, R.drawable.icons_background) } as GradientDrawable

    roundedBackground.setColor(colorId)
    roundedBackground.setStroke(borderWidth, borderColor)
    this.background = roundedBackground
}