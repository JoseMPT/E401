package mx.tecnm.cdhidalgo.e401

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class Login : AppCompatActivity() {
    private lateinit var btnGoRegister: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnGoRegister = findViewById(R.id.btn_register)

        btnGoRegister.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}