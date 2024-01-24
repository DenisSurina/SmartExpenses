package faks.android.smartexpenses.contoller

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import faks.android.smartexpenses.R

class CategoryIconColorAdapter (private val context: Context, private val colors: Array<Int>) : ArrayAdapter<Int>(context, 0, colors) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val imageView: ImageView

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_circle_layout, parent, false)
            imageView = view.findViewById(R.id.imageView)
        } else {
            view = convertView
            imageView = view.findViewById(R.id.imageView)
        }

        val drawable = ContextCompat.getDrawable(context, R.drawable.item_circle_background) as GradientDrawable
        drawable.setColor(colors[position]) // Set the color
        imageView.setImageDrawable(drawable)

        return view
    }

}