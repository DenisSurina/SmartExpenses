package faks.android.smartexpenses.contoller

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

class CategoryIconImageAdapter(context: Context, private val images: Array<Int>) :
    ArrayAdapter<Int>(context, 0, images) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(context)

            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.adjustViewBounds = true
            imageView.setPadding(4, 4, 4, 4)
        } else {
            imageView = convertView as ImageView
        }

        imageView.setImageResource(getItem(position) ?: 0)
        return imageView
    }
}