package com.example.plogging.ui

import android.content.ContentValues
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.plogging.BuildConfig
import com.example.plogging.R
import com.example.plogging.databinding.ActivityLoginBinding
import com.example.plogging.util.Constants
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var legacyLoginIntent: Intent
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    private val successIntent: (userId: String, userToken: String) -> Unit = { id, token ->
        Log.i("User Info ::: ", " id : $id , token :  $token")
        startActivity(Intent(this, MainActivity::class.java).apply {
            putExtra(Constants.USER_ID, id)
            putExtra(Constants.USER_TOKEN, token)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGoogleLoginClient()
        setLayout()
    }

    private fun setLayout() {
        binding.btnGoogleLogin.setOnClickListener { googleLogin() }
    }

    private fun setGoogleLoginClient() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }

    private fun googleLogin() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    val loginIntent =
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    intentSenderForResult.launch(loginIntent)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(
                        "원 탭 로그인 불가.",
                        "Couldn't start One Tap UI: ${e.localizedMessage}"
                    )
                }
            }
            .addOnFailureListener(this) { e ->
                // legacy Login
                googleLegacyLogin()
            }
    }

    private fun googleLegacyLogin() {
        googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        val googleClient = GoogleSignIn.getClient(this, googleSignInOptions)
        legacyLoginIntent = googleClient.signInIntent
        requestActivityForResult.launch(legacyLoginIntent)
    }

    private val intentSenderForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val idToken =
                        oneTapClient.getSignInCredentialFromIntent(result.data).googleIdToken
                    if (idToken != null) {
                        registerFirebase(idToken)
                    }
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(ContentValues.TAG, "One-tap dialog was closed.")
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(ContentValues.TAG, "One-tap encountered a network error.")
                            Snackbar.make(binding.root, "네트워크 연결을 확인해주세요.", 50)
                        }
                        else -> {
                            Log.d(
                                ContentValues.TAG, "Couldn't get credential from result." +
                                        " (${e.localizedMessage})"
                            )
                        }
                    }
                }
            } else {
                Log.d(ContentValues.TAG, "OneTab Login  :::: Fail")
            }
        }

    private fun registerFirebase(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userName =
                        firebaseAuth.currentUser?.displayName ?: "UNKNOWN"
                    successIntent(userName, idToken)
                } else {
                    Log.w(
                        ContentValues.TAG,
                        "signInWithCredential:failure",
                        task.exception
                    )
                }
            }
    }

    private val requestActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val googleSignInAccount =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data).result
                val userId = googleSignInAccount.id.toString()
                val userToken = googleSignInAccount.idToken.toString()
                registerFirebase(userToken)
                successIntent(userId, userToken)
            } else {
                Log.d(ContentValues.TAG, "legacy Login  :::: Fail!")
            }
        }
}