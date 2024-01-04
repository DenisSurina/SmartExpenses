package faks.android.smartexpenses.contoller

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.ActivityAccountManagementBinding
import faks.android.smartexpenses.model.Account
import faks.android.smartexpenses.viewmodel.AccountManagementActivityViewModel
import java.math.BigDecimal

class AccountManagementActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAccountManagementBinding
    private lateinit var accountManagementActivityViewModel: AccountManagementActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountManagementActivityViewModel = ViewModelProvider(this)[AccountManagementActivityViewModel::class.java]

        val createAccountFloatingButton : FloatingActionButton = binding.accountManagementMainFloatingButton

        createAccountFloatingButton.setOnClickListener {
            setupAccountManagementWindow()
        }

        // Add all the existing accounts to activity
        listAccounts()

        // Write balance to top of the screen
        writeAllBalance()

    }



    private fun writeAllBalance(){

        accountManagementActivityViewModel.getBalances {balances ->
            val sum = balances.fold(BigDecimal.ZERO) { acc, next -> acc + next }
            val str = "$${sum.toPlainString()}"
            binding.accountManagementTotalBalanceTextView.text = str
        }

    }


    private fun listAccounts(){

        accountManagementActivityViewModel.getAccounts { accounts ->

            for(account in accounts){
                addAccountBriefView(account)
            }

        }

    }

    private fun addAccountBriefView(account: Account){

        val accountView = layoutInflater.inflate(R.layout.account_brief_view,null)

        val accountIcon = accountView.findViewById<ImageView>(R.id.account_brief_view_icon)
        val accountName = accountView.findViewById<TextView>(R.id.account_brief_view_name)
        val accountTotalBalance = accountView.findViewById<TextView>(R.id.account_brief_view_total_balance)
        val accountDeleteImageView = accountView.findViewById<ImageView>(R.id.delete_account_image_view)



        Picasso.get()
            .load(account.iconID)
            .transform(ImageTransformer(0, 10)) // 30px radius, 10px margin
            .into(accountIcon)

        val roundedBackground = ContextCompat.getDrawable(this, R.drawable.icons_background) as GradientDrawable
        roundedBackground.setColor(account.iconColorID)
        accountIcon.background = roundedBackground

        accountName.text = account.name

        val balance = "€${account.balance ?: "0"}"
        accountTotalBalance.text = balance


        val layoutParams = accountView.layoutParams as? ViewGroup.MarginLayoutParams
            ?: ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        layoutParams.bottomMargin = 10
        layoutParams.rightMargin = 5
        layoutParams.leftMargin = 5
        accountView.layoutParams = layoutParams


        accountDeleteImageView.setOnClickListener {
            deleteAccount(account,accountView)
        }


        binding.accountManagementLinearLayout.addView(accountView)

    }


    private fun deleteAccount(account: Account,accountView : View){

        val builder = AlertDialog.Builder(this)

        builder.setTitle("Confirm")
        builder.setMessage("Are you sure you want to delete this account?")

        builder.setPositiveButton("Yes") { dialog, which ->
            accountManagementActivityViewModel.deleteAccount(account)
            binding.accountManagementLinearLayout.removeView(accountView)
            writeAllBalance()
        }

        builder.setNegativeButton("No") { dialog, which ->

        }

        val dialog = builder.create()
        dialog.show()

    }


    // This function is used to setup account management window
    // All the logic for creating a new account is located here
    // TODO make some more complex parts into separate functions
    private fun setupAccountManagementWindow(){


        val builder = AlertDialog.Builder(this)
        val customLayout = layoutInflater.inflate(R.layout.add_account_popup_window_layout , null)


        val accountIconImageView = customLayout.findViewById<ImageView>(R.id.add_account_display_icon_image_view)
        val accountNameEditText = customLayout.findViewById<EditText>(R.id.add_account_name_edit_text)
        val openAccountIconImageGrid = customLayout.findViewById<TextView>(R.id.add_account_icon_text_view)
        val openAccountIconColorGrid = customLayout.findViewById<TextView>(R.id.add_account_icon_color_text_view)
        val accountBalanceEditText = customLayout.findViewById<EditText>(R.id.add_account_balance_edit_text)


        var accountImageIconId : Int = -1
        var accountImageColorID: Int = Color.WHITE


        // open alert dialog that let's user select color for account icon image
        val iconImageAdapter = CategoryIconImageAdapter(this,getIcons())
        val iconImageGrid = createGrid(iconImageAdapter)
        val iconImagePickerAlertDialog = AlertDialog.Builder(this)
            .setView(iconImageGrid)
            .setPositiveButton("Close", null)
            .create()

        iconImageGrid.setOnItemClickListener { _, _, position, _ ->
            val selectedImageId = iconImageAdapter.getItem(position) ?: 0
            accountImageIconId = selectedImageId
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
        val iconColorGrid = createGrid(CategoryIconColorAdapter(this, colors))

        val iconColorPickerAlertDialog = AlertDialog.Builder(this)
            .setView(iconColorGrid)
            .setPositiveButton("Close", null)
            .create()

        iconColorGrid.setOnItemClickListener { _, _, position, _ ->
            val selectedColor = colors[position]
            // Handle the color selection
            accountIconImageView.setBackgroundColor(selectedColor)
            accountImageColorID = selectedColor
            iconColorPickerAlertDialog.dismiss()
        }
        openAccountIconColorGrid.setOnClickListener {
            iconColorPickerAlertDialog.show()
        }


        val accountName = accountNameEditText.text



        builder.setView(customLayout)
            .setPositiveButton("Add") { _, _ ->

                //CREATE ACCOUNT
                accountManagementActivityViewModel.getAccountByName(accountName.toString()) {duplicateAccount ->


                    duplicateAccount ?: run {

                        val errors = StringBuilder()


                        if(accountName.isBlank())
                            errors.append("Name cannot be empty.\n")


                        if(accountImageIconId == -1)
                            errors.append("Image not specified.\n")


                        val accountBalance = stringToBigDecimal(accountBalanceEditText.text.toString())
                        if(accountBalance == null)
                            errors.append("Balance not specified or used characters that are not allowed.\n")


                        if(errors.isBlank()){

                            val newAccount  = Account(accountName.toString(),accountImageIconId,accountImageColorID,accountBalance)
                            accountManagementActivityViewModel.insertAccount(newAccount)
                            addAccountBriefView(newAccount)
                            writeAllBalance()

                        }else{
                            Toast.makeText(this,errors,Toast.LENGTH_LONG).show()
                        }

                    }


                }

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


    private fun stringToBigDecimal(input: String): BigDecimal? {
        return try {
            BigDecimal(input)
        } catch (e: NumberFormatException) {
            println(e)
            null
        }
    }

}

















