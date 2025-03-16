package com.example.assignment2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener  {
    //// SETTING UP THE ELEMENTS ON THE SCREEN ////
    lateinit var btnLogin: Button
    lateinit var btnTestingLogin: TextView
    lateinit var btnBack: ImageButton

    //// SETTING UP THE VARIABLES FOR THE AUTHENTICATION ////
    //// CODE REFERENCES:
    //// 1. https://firebase.google.com/docs/auth/android/google-signin
    //// 2. https://developer.android.com/identity/sign-in/credential-manager-siwg
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    private fun launchCredentialManager() {
        // Create a Google signin request
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // Display the accounts that user hasnt used to login
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        var context = this // Ensure the current context is Activity-based for display login pop-up

        lifecycleScope.launch {
            try {
                // Get credential
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                // Extract credential from the result of Credential Manager
                // Prepare for signing in
                handleSignIn(result.credential)
            } catch (e: GetCredentialException) { // Error getting credential
                Log.e("LoginActivity", "Error getting credential: ${e.localizedMessage}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup elements on the screen
        btnLogin = findViewById(R.id.btnSignIn)
        btnBack = findViewById(R.id.btnBackLogin)
        btnTestingLogin = findViewById(R.id.btnTestingLogin)

        // Setup event listener for button
        btnLogin.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        btnTestingLogin.setOnClickListener(this)

        // Initialize the authentication and credential manager
        auth = Firebase.auth
        credentialManager = CredentialManager.create(baseContext)

        Log.d("LoginActivity", "Screen initialized")
    }

    // Handle click events
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnBackLogin -> { // Back to the first screen
                onBackPressed()
            }
            R.id.btnTestingLogin -> { // Sign-in button (for testing with Espresso)
                // Prepare for intent
                var intent = Intent()
                intent.putExtra("DisplayName", "Test")
                intent.putExtra("UserEmail", "test@test.com")
                setResult(RESULT_OK, intent) // Set RESULT_OK flag to let the first activity know to update the data
                finish()
            }
            R.id.btnSignIn -> {
                Log.d("LoginActivity", "Sign-in button clicked")
                launchCredentialManager() // Start signing in process
            }
        }
    }

    // Handle sign in function
    // Takes in the credential data from the Credential manager
    private fun handleSignIn(credential: Credential) {
        // Check if credential is for Google sign-in
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            // Sign in to Firebase using the Google token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        }
    }

    // Authenticating with Google token
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, move user back to the home screen
                    val user = auth.currentUser
                    Log.d("LoginActivity", "Login sucessfully. / " + user?.displayName + " / " + user?.email)

                    // Prepare for intent
                    var intent = Intent()
                    intent.putExtra("DisplayName", user?.displayName)
                    intent.putExtra("UserEmail", user?.email)
                    setResult(RESULT_OK, intent) // Set RESULT_OK flag to let the first activity know to update the data
                    finish()
                } else {
                    // If sign in fails, display a snackbar to the user
                    showNotificationWithDismiss("Failed to login. Please try again later.")
                    Log.d("LoginActivity", "Login failed.", task.exception)
                }
            }
    }

    // Function to show snackbar with dismiss button
    private fun showNotificationWithDismiss(message: String) {
        val snackbar = Snackbar.make(findViewById(R.id.main), message, 5000)
        // Set action to snackbar in order to dismiss notification
        snackbar.setAction("DISMISS") {
            snackbar.dismiss()
        }
        snackbar.show()
    }
}