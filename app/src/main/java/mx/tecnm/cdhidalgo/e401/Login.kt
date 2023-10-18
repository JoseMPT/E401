package mx.tecnm.cdhidalgo.e401

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


var userData: UserDataClass = UserDataClass(name = "", lastname1 = "", lastname2 = "", email = "")
class Login : AppCompatActivity() {
    private lateinit var btnGoRegister: MaterialButton
    private lateinit var btnLogin: Button
    private lateinit var btnGoogle: Button
    private lateinit var inputEmail : TextInputLayout
    private lateinit var inputPassword : TextInputLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    /* Google */
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private lateinit var googleSignInClient: GoogleSignInClient

    private val codeResult = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnGoRegister = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)
        btnGoogle = findViewById(R.id.btn_login_google)

        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)


        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .setAutoSelectEnabled(true)
            .build()

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

        btnGoogle.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender,
                            codeResult,
                            null,
                            0,
                            0,
                            0,
                            null
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d(TAG, "No saved credentials: ${e.localizedMessage}")
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        when (requestCode) {
            codeResult -> {
                try {
                    val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = googleCredential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with Firebase.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success")
                                        val user = auth.currentUser
                                        Toast.makeText(
                                            baseContext,
                                            "User logged: ${user?.email}",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                        onSuccessfulLogin()
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                                    }
                                }
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.w("Google Sign In", "signInResult:failed code=" + e.statusCode, e)
                }
            }
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
                Log.w(TAG, "getUser: ", it.cause)
            }
        return userData
    }

    private fun signInWithGoogle(){
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, codeResult,
                        null, 0, 0, 0, null)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                e.localizedMessage?.let { Log.d(TAG, it) }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "User logged: ${user?.email}",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun onSuccessfulLogin(){
        val intent = Intent(this, Store::class.java)
        startActivity(intent)
    }
}