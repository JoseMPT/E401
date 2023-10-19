package mx.tecnm.cdhidalgo.e401

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverPassword : AppCompatActivity() {
    private lateinit var inputRecover: TextInputLayout
    private lateinit var btnRecover: Button
    private lateinit var btnBackToLogin: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        inputRecover = findViewById(R.id.input_email_recover)
        btnRecover = findViewById(R.id.btn_recover)
        btnBackToLogin = findViewById(R.id.btn_back)

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        btnBackToLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        btnRecover.setOnClickListener {
            val email = inputRecover.editText?.text.toString()
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                inputRecover.error = null
                Toast.makeText(this, "Typed a valid email", LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    Toast.makeText(this, "Email to recover password was sent", LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error", LENGTH_SHORT).show()
                }
        }
    }
}