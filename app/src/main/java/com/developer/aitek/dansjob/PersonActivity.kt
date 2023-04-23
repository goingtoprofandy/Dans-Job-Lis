package com.developer.aitek.dansjob

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.developer.aitek.dansjob.databinding.ActivityPersonBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener


class PersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Account"
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            binding.personName.text = account.displayName
            Glide.with(this@PersonActivity)
                .load(account.photoUrl)
                .placeholder(R.mipmap.placeholder)
                .into(binding.personFace)
        }

        binding.personLogout.setOnClickListener {
            mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, OnCompleteListener<Void?> {
                    val intent = Intent(this@PersonActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                })
        }
    }
}