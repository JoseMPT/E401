package mx.tecnm.cdhidalgo.e401

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var btnBackToLogin : MaterialButton
    private lateinit var btnRegister : MaterialButton
    private lateinit var name : TextInputLayout
    private lateinit var lastname1 : TextInputLayout
    private lateinit var lastname2 : TextInputLayout
    private lateinit var email : TextInputLayout
    private lateinit var password : TextInputLayout

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        firestore = Firebase.firestore

        btnBackToLogin = findViewById(R.id.btn_back_login)
        btnRegister = findViewById(R.id.register_new_user)

        name = findViewById(R.id.input_name)
        lastname1 = findViewById(R.id.input_lastname1)
        lastname2 = findViewById(R.id.input_lastname2)
        email = findViewById(R.id.input_email_register)
        password = findViewById(R.id.input_password_register)
    }

    override fun onStart() {
        super.onStart()
        btnBackToLogin.setOnClickListener{
            finish()
        }

        btnRegister.setOnClickListener{
            val emailInput = email.editText?.text.toString().trim()
            val passwordInput = password.editText?.text.toString().trim()

            if(emailInput.isEmpty() || passwordInput.isEmpty()) {
                return@setOnClickListener
            }

            val nameAll = String.format("%s %s %s", name.editText?.text, lastname1.editText?.text, lastname2.editText?.text)
            val message = String.format(
                "Are you sure about your data is right?\nCurrent data is:\nName: %s\nEmail: %s\nPassword: %s",
                nameAll.trimIndent(), email.editText?.text, password.editText?.text
            )

            val userData = UserDataClass(
                name.editText?.text.toString().trim(),
                lastname1.editText?.text.toString().trim(),
                lastname2.editText?.text.toString().trim(),
                emailInput
            )

            val confirmData = AlertDialog.Builder(it.context)
            confirmData.setTitle("Confirm data")
            confirmData.setMessage(message.trimIndent())
            confirmData.setPositiveButton("Confirm"){ _, _ ->
                signUpUser(emailInput, passwordInput, userData)
            }
            confirmData.setNegativeButton("Cancel"){ _, _ ->}
            confirmData.show()
        }
    }

    private fun signUpUser(email: String, password: String, userData: UserDataClass) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                saveUserData(userData)
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                Toast.makeText(
                    baseContext,
                    user?.email,
                    Toast.LENGTH_SHORT,
                ).show()
                finish()
            }
            .addOnFailureListener {
                Log.w(TAG, "createUserWithEmail:failure", it.cause)
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    private fun saveUserData(user: UserDataClass){
        firestore.collection("users").document(user.email)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(
                    baseContext,
                    "User data saved",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    baseContext,
                    "User data not saved",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    private fun signInUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Toast.makeText(
                    baseContext,
                    "User or password incorrect",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }
}