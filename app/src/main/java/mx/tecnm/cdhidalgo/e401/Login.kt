package mx.tecnm.cdhidalgo.e401

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

var userData: UserDataClass = UserDataClass(name = "", lastname1 = "", lastname2 = "", email = "")
class Login : AppCompatActivity() {
    private lateinit var btnGoRegister: MaterialButton
    private lateinit var btnLogin: Button
    private lateinit var inputEmail : TextInputLayout
    private lateinit var inputPassword : TextInputLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnGoRegister = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)

        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)

        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun onStart() {
        super.onStart()

        btnGoRegister.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = inputEmail.editText?.text.toString().trim()
            val password = inputPassword.editText?.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()){
                return@setOnClickListener
            }

            signInUser(email, password)
        }
    }

    private fun signInUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(this){
                userData = getUser(email)
                Toast.makeText(
                    baseContext,
                    "User logged",
                    Toast.LENGTH_SHORT,
                ).show()
                val intent = Intent(this, Store::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(
                    baseContext,
                    "User or password incorrect",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    private fun getUser(email: String): UserDataClass {
        firestore.collection("users").whereEqualTo("email", email).get()
            .addOnSuccessListener {
                for(document in it){
                    userData = document.toObject(UserDataClass::class.java)
                }
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "getUser: ", it.cause)
            }
        return userData
    }
}