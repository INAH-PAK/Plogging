package com.example.plogging.ui.login

import android.content.ContentValues
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.plogging.BuildConfig
import com.example.plogging.PloggingApplication
import com.example.plogging.R
import com.example.plogging.databinding.ActivityLoginBinding
import com.example.plogging.ui.MainActivity
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
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var legacyLoginIntent: Intent
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    private val userDataRepository by lazy {
        PloggingApplication.appContainer.provideUserDataRepository()
    }

    private val successIntent: (userToken: String, userName: String, userPhotoUrl: String, userEmail: String) -> Unit =
        { token, name, photo, email ->
            Log.i("사용자 사진", photo)
            lifecycleScope.launch {
                userDataRepository.setUserInfo(
                    token, name, photo, email
                )
            }
            startActivity(Intent(this, MainActivity::class.java))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenCreated {
            setGoogleLoginClient()
            setLayout()
        }
    }

    private fun setupSplashScreen() {
        var keepSplashScreenOn = true
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val isLogin = userDataRepository.getUserLoginState()
                when (isLogin) {
                    true -> {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    false -> {
                        keepSplashScreenOn = false
                    }
                }
            }
        }
        installSplashScreen().setKeepOnScreenCondition {
            keepSplashScreenOn
        }
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
                    successIntent(
                        idToken,
                        firebaseAuth.currentUser?.displayName ?: "",
                        firebaseAuth.currentUser?.photoUrl?.toString() ?: "",
                        firebaseAuth.currentUser?.email ?: "",
                    )

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
                val userToken = googleSignInAccount.idToken.toString()
                registerFirebase(userToken)
                successIntent(
                    userToken,
                    firebaseAuth.currentUser?.displayName ?: "",
                    firebaseAuth.currentUser?.photoUrl?.toString() ?: "",
                    firebaseAuth.currentUser?.email ?: "",
                )
            } else {
                Log.d(ContentValues.TAG, "legacy Login  :::: Fail!")
            }
        }
}