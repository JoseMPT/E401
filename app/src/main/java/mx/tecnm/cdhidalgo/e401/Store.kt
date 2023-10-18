package mx.tecnm.cdhidalgo.e401

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Store : AppCompatActivity() {
    private lateinit var userText : TextView
    private lateinit var btnSignOut : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        userText = findViewById(R.id.user_store)
        btnSignOut = findViewById(R.id.sign_out)

        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun onStart() {
        super.onStart()

        /*userText.text = String.format("Welcome %s %s %s!",
            userData.name, userData.lastname1, userData.lastname2
        ).trim()*/
        userText.text = String.format("Welcome %s!",
            (auth.currentUser?.displayName ?: auth.currentUser?.email ?: "User")
        ).trim()

        btnSignOut.setOnClickListener {
            signOutUser()
        }
    }

    private fun signOutUser(){
        auth.signOut()
        firestore.clearPersistence()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}