package com.developer.aitek.dansjob

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.developer.aitek.dansjob.databinding.ActivityLoginBinding
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.util.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val RC_SIGN_IN = 1001
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Login With Google
        binding.googleBtn.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        FacebookSdk.sdkInitialize(applicationContext);

        callbackManager = CallbackManager.Factory.create()
        binding.facebookBtn.setReadPermissions("email", "public_profile")
        binding.facebookBtn.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"));
        }
        binding.facebookBtn.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("LOGIN_ACTIVITY", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("LOGIN_ACTIVITY", "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("LOGIN_ACTIVITY", "facebook:onError", error)
            }
        })

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            Toast.makeText(this@LoginActivity, "Welcome back, " + account.displayName, Toast.LENGTH_SHORT).show()
            // Signed in successfully, show authenticated UI.
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        } else {
            val accessToken = AccessToken.getCurrentAccessToken()
            val isLoggedIn = accessToken != null && !accessToken.isExpired
            if (isLoggedIn) {
                Toast.makeText(this@LoginActivity, "Welcome back", Toast.LENGTH_SHORT).show()
                // Signed in successfully, show authenticated UI.
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        Log.d("LOGIN_ACTIVITY", "handleFacebookAccessToken:$accessToken")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(this@LoginActivity, "Welcome back, " + account.displayName, Toast.LENGTH_SHORT).show()
            // Signed in successfully, show authenticated UI.
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LOGIN_ACTIVITY", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this@LoginActivity, "signInResult:failed code=" + e.statusCode, Toast.LENGTH_SHORT).show()
        }
    }
}