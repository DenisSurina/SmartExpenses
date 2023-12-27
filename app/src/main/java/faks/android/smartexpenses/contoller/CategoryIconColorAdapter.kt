package faks.android.smartexpenses.contoller

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class CategoryIconColorAdapter (private val context: Context, private val colors: Array<Int>) :
    ArrayAdapter<Int>(context, 0, colors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val colorView: View
        if (convertView == null) {
            colorView = View(context)
            colorView.layoutParams = ViewGroup.LayoutParams(50, 50) // size of each color square
        } else {
            colorView = convertView
        }

        colorView.setBackgroundColor(colors[position])
        return colorView
    }
}