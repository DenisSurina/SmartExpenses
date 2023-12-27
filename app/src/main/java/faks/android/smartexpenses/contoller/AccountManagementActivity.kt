package faks.android.smartexpenses.contoller

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.ActivityAccountManagementBinding
import faks.android.smartexpenses.model.Category

class AccountManagementActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAccountManagementBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val createAccountFloatingButton : FloatingActionButton = binding.accountManagementMainFloatingButton

        createAccountFloatingButton.setOnClickListener {
            setupAccountManagementWindow()
        }

    }


    private fun setupAccountManagementWindow(){


        val builder = AlertDialog.Builder(this)
        val customLayout = layoutInflater.inflate(R.layout.add_account_popup_window_layout , null)


        val accountIconImageView = customLayout.findViewById<ImageView>(R.id.add_account_display_icon_image_view)
        val accountName = customLayout.findViewById<EditText>(R.id.add_account_name_edit_text)
        val openAccountIconImageGrid = customLayout.findViewById<TextView>(R.id.add_account_icon_text_view)
        val openAccountIconColorGrid = customLayout.findViewById<TextView>(R.id.add_account_icon_color_text_view)
        val accountBalance = customLayout.findViewById<EditText>(R.id.add_account_balance_edit_text)



        // open alert dialog that let's user select color for account icon image
        val iconImageAdapter = CategoryIconImageAdapter(this,getIcons())
        val iconImageGrid = createGrid(iconImageAdapter)
        val iconImagePickerAlertDialog = AlertDialog.Builder(this)
            .setView(iconImageGrid)
            .setPositiveButton("Close", null)
            .create()

        iconImageGrid.setOnItemClickListener { _, _, position, _ ->
            val selectedImageId = iconImageAdapter.getItem(position) ?: 0
            accountIconImageView.setImageResource(selectedImageId)
            iconImagePickerAlertDialog.dismiss() // Dismiss the dialog after selecting an image
        }
        openAccountIconImageGrid.setOnClickListener {
            iconImagePickerAlertDialog.show()
        }




        // open alert dialog that let's user select color for account icon color
        val colors = arrayOf(
            Color.RED, Color.BLUE, Color.GREEN
        )
        val iconColorGrid = createGrid(CategoryIconImageAdapter(this, colors))

        val iconColorPickerAlertDialog = AlertDialog.Builder(this)
            .setView(iconColorGrid)
            .setPositiveButton("Close", null)
            .create()

        iconColorGrid.setOnItemClickListener { _, _, position, _ ->
            val selectedColor = colors[position]
            // Handle the color selection
            accountIconImageView.setBackgroundColor(selectedColor)
            iconColorPickerAlertDialog.dismiss()
        }
        openAccountIconColorGrid.setOnClickListener {
            iconColorPickerAlertDialog.show()
        }





        builder.setView(customLayout)
            .setPositiveButton("Add") { _, _ ->

                //CREATE ACCOUNT

            }
            .setNegativeButton("Exit") { _, _ -> }

        val dialog = builder.create()
        dialog.show()

    }


    private fun createGrid(adapter: ArrayAdapter<Int>) : GridView{

        val grid = GridView(this)
        grid.numColumns = 5
        grid.horizontalSpacing = 8
        grid.verticalSpacing = 8
        grid.setPadding(16, 16, 16, 16)
        grid.adapter = adapter


        return grid

    }

    private fun getIcons() : Array<Int>{

        return arrayOf(
            R.drawable.paint_pallete,
            R.drawable.family,
            R.drawable.cheers,
            R.drawable.credit_card
        )

    }

}

















