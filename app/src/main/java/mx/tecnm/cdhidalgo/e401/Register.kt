package mx.tecnm.cdhidalgo.e401

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class Register : AppCompatActivity() {
    private lateinit var btnBackToLogin : MaterialButton
    private lateinit var btnRegister : MaterialButton
    private lateinit var name : TextInputLayout
    private lateinit var lastname1 : TextInputLayout
    private lateinit var lastname2 : TextInputLayout
    private lateinit var email : TextInputLayout
    private lateinit var password : TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnBackToLogin = findViewById(R.id.btn_back_login)
        btnRegister = findViewById(R.id.register_new_user)

        name = findViewById(R.id.input_name)
        lastname1 = findViewById(R.id.input_lastname1)
        lastname2 = findViewById(R.id.input_lastname2)
        email = findViewById(R.id.input_email_register)
        password = findViewById(R.id.input_password_register)

        btnBackToLogin.setOnClickListener{
            finish()
        }

        btnRegister.setOnClickListener{
            val name = String.format("%s %s %s", name.editText?.text, lastname1.editText?.text, lastname2.editText?.text)
            val message = String.format(
                "Are you sure about your data is right?\nCurrent data is:\nName: %s\nEmail: %s\nPassword: %s",
                name.trimIndent(), email.editText?.text, password.editText?.text
            )
            val confirmData = AlertDialog.Builder(it.context)
            confirmData.setTitle("Confirm data")
            confirmData.setMessage(message.trimIndent())
            confirmData.setPositiveButton("Confirm"){ _, _ -> finish()}
            confirmData.setNegativeButton("Cancel"){ _, _ ->}
            confirmData.show()
        }
    }
}