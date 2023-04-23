package com.developer.aitek.dansjob

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.aitek.api.NetworkConnectionInterceptor
import com.developer.aitek.api.RemoteRequestManager
import com.developer.aitek.api.Repository
import com.developer.aitek.api.data.ItemJob
import com.developer.aitek.dansjob.databinding.ActivityMainBinding
import com.google.android.material.switchmaterial.SwitchMaterial


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModelMain
    private lateinit var factory: ViewModelFactoryMain

    private lateinit var adapter: Adapter<ItemJob?>
    private var isErrorLoaded = false
    private var query = ""
    private var location = ""
    private var isFullTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = Repository(RemoteRequestManager(
            NetworkConnectionInterceptor(this@MainActivity),
            this@MainActivity
        ))

        factory = ViewModelFactoryMain(repository)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, factory).get(ViewModelMain::class.java)
        setContentView(binding.root)

        // Setup Title
        supportActionBar?.apply {
            this.title = "Job List"
        }

        printHashKey(this)

        // Setup Live Cycle
        prepareToLoadLiveCycle()

        // Setup View
        prepareToView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.person) {
            startActivity(Intent(this@MainActivity, PersonActivity::class.java))
        } else if (item.itemId == R.id.search) {
            showBottomSheetDialog(this@MainActivity, R.layout.search_dialog) { dialog ->
                dialog.setTitle("Search Job")
                val searchEdt = dialog.findViewById<EditText>(R.id.searchEdt)
                val locationEdt = dialog.findViewById<EditText>(R.id.locationEdt)
                val fullTimeSwt = dialog.findViewById<SwitchMaterial>(R.id.fullTimeSwt)
                val applyFilterBtn = dialog.findViewById<Button>(R.id.applyFilterBtn)

                applyFilterBtn?.setOnClickListener {
                    searchEdt?.apply { query = this.text.toString() }
                    locationEdt?.apply { location = this.text.toString() }
                    fullTimeSwt?.apply { isFullTime = this.isChecked }

                    adapter.data = mutableListOf()
                    dialog.dismiss()
                    loadNextDataFromApi(1)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun prepareToView() {
        adapter = Adapter(R.layout.item_job, mutableListOf(),
            { itemView, item ->
                val itemTitle = itemView.findViewById<TextView>(R.id.itemTitle)
                val itemCompany = itemView.findViewById<TextView>(R.id.itemCompany)
                val itemLocationAndType = itemView.findViewById<TextView>(R.id.itemLocationAndType)
                val itemLogo = itemView.findViewById<ImageView>(R.id.itemLogo)
                val gotoIcon = itemView.findViewById<ImageView>(R.id.gotoIcon)
                if (item != null) {
                    itemTitle.text = item.title
                    itemCompany.text = item.company
                    itemLocationAndType.text = StringBuilder().append(item.location).append(" | ").append(item.type)
                    Glide.with(this@MainActivity)
                        .load(item.company_logo)
                        .placeholder(R.mipmap.placeholder)
                        .into(itemLogo)
                } else {
                    gotoIcon.visibility = View.GONE
                }
            },
            { _, item ->
                item?.apply {
                    tempID = item.id
                    startActivity(Intent(this@MainActivity, ActivityDetail::class.java))
                }
            })

        binding.mainListsPokemon.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.mainListsPokemon.layoutManager = layoutManager
        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (!isErrorLoaded && totalItemsCount >= 10)
                    loadNextDataFromApi(page)
            }
        }
        binding.mainListsPokemon.addOnScrollListener(scrollListener)

        viewModel.loadData(1, "", "", false) {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun loadNextDataFromApi(page: Int) {
        viewModel.loadData(page, query, location, isFullTime) {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT)
                .show()
            isErrorLoaded = true
        }
    }

    private fun prepareToLoadLiveCycle() {
        val owner = this
        viewModel.apply {
            isLoading.observe(owner) {
                if (it) {
                    binding.mainLoader.visibility = View.VISIBLE
                } else {
                    binding.mainLoader.visibility = View.GONE
                }
            }

            dataRes.observe(owner) {
                adapter.data.addAll(it)
            }
        }
    }
}