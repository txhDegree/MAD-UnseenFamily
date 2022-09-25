package com.example.unseenfamily

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.unseenfamily.databinding.ActivityMainBinding
import com.example.unseenfamily.repositories.DonationRepository
import com.example.unseenfamily.viewModel.DonationItemViewModel
import com.example.unseenfamily.viewModel.DonationViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var donationViewModel: DonationViewModel
    private lateinit var donationItemViewModel: DonationItemViewModel

    override fun onStart() {
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        donationViewModel = ViewModelProvider(this).get(DonationViewModel::class.java)
        donationItemViewModel = ViewModelProvider(this).get(DonationItemViewModel::class.java)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val firebaseAuth = FirebaseAuth.getInstance()

        val headerView = binding.navView.getHeaderView(0)
        val tvUsername = headerView.findViewById<TextView>(R.id.textViewUserName)
        val tvEmail = headerView.findViewById<TextView>(R.id.textViewEmail)
        if(!firebaseAuth.currentUser!!.photoUrl.toString().isNullOrBlank()){
            val ivProfileImg = headerView.findViewById<ImageView>(R.id.imageViewProfileImg)
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            var image: Bitmap? = null
            executor.execute{
                val imageUrl = firebaseAuth.currentUser!!.photoUrl.toString()
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        ivProfileImg.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }


        firebaseAuth.currentUser!!.email
        tvUsername?.text = firebaseAuth.currentUser!!.displayName
        tvEmail?.text = firebaseAuth.currentUser!!.email

        donationViewModel.reload()

        val nav_logout = binding.navView.menu.findItem(R.id.nav_logout)
        nav_logout.setOnMenuItemClickListener {
            donationViewModel.deleteAll()
            donationItemViewModel.deleteAll()

            firebaseAuth.signOut()

            val intent = Intent(this, SignInActivity::class.java)
            finish()
            startActivity(intent)
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}