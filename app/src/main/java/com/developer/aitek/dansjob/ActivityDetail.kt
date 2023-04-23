package com.developer.aitek.dansjob

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.developer.aitek.api.NetworkConnectionInterceptor
import com.developer.aitek.api.RemoteRequestManager
import com.developer.aitek.api.Repository
import com.developer.aitek.dansjob.R
import com.developer.aitek.dansjob.databinding.ActivityDetailBinding

class ActivityDetail : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: ViewModelMain
    private lateinit var factory: ViewModelFactoryMain

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = Repository(
            RemoteRequestManager(
                NetworkConnectionInterceptor(this@ActivityDetail),
                this@ActivityDetail
            )
        )

        factory = ViewModelFactoryMain(repository)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, factory).get(ViewModelMain::class.java)
        setContentView(this.binding.root)

        tempDeviceID = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID)

        // Setup Title
        supportActionBar?.apply {
            this.title = "Job Detail"
        }

        // Setup Live Cycle
        prepareToLoadLiveCycle()

        // Setup View
        prepareToView()
    }

    private fun prepareToView() {
        loadDetailData{}
    }

    private fun prepareToLoadLiveCycle() {
        val owner = this
        viewModel.apply {
            isLoading.observe(owner) {
                binding.detailLoader.visibility = if (it) View.VISIBLE else View.GONE
            }

            dataDetailRes.observe(owner) { data ->
                data?.apply {
                    binding.detailName.text = StringBuilder().append(data.title)
                    binding.detailDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(this.description, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(this.description)
                    }
                    binding.detailHowToApply.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(this.how_to_apply, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(this.how_to_apply)
                    }
                    binding.detailDescription.movementMethod = LinkMovementMethod.getInstance();
                    binding.detailHowToApply.movementMethod = LinkMovementMethod.getInstance();
                    Glide.with(this@ActivityDetail)
                        .load(data.company_logo)
                        .placeholder(R.mipmap.placeholder)
                        .into(binding.detailCover)
                    binding.detailApplyButton.setOnClickListener {
                        val httpIntent = Intent(Intent.ACTION_VIEW)
                        httpIntent.data = Uri.parse(this.url)

                        startActivity(httpIntent)
                    }
                }
            }
        }
    }

    private fun loadDetailData(onSuccess: () -> Unit) {
        viewModel.detailData(tempID, tempDeviceID, {
            Toast.makeText(this@ActivityDetail, it, Toast.LENGTH_SHORT).show()
        }) {
            onSuccess()
        }
    }
}