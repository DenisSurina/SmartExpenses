package faks.android.smartexpenses.contoller

import android.os.Bundle
import android.widget.EditText
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
            openAccountManagementWindow()
        }

    }


    private fun openAccountManagementWindow(){


        val builder = AlertDialog.Builder(this)
        val customLayout = layoutInflater.inflate(R.layout.add_account_popup_window_layout , null)

        val accountName = customLayout.findViewById<EditText>(R.id.add_account_name_edit_text)


        builder.setView(customLayout)
            .setPositiveButton("Add") { _, _ ->


            }
            .setNegativeButton("Exit") { _, _ -> }

        val dialog = builder.create()
        dialog.show()

    }

}