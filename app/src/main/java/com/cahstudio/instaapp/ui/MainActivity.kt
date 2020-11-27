package com.cahstudio.instaapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cahstudio.instaapp.R
import com.cahstudio.instaapp.ui.fragment.HomeFragment
import com.cahstudio.instaapp.ui.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private lateinit var actionBar: ActionBar
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var easyImage: EasyImage
    private lateinit var mRef: DatabaseReference
    private lateinit var mStorageRef: StorageReference
    private lateinit var mUri: Uri
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureToolbar()
        initialize()
    }

    private fun configureToolbar(){
        if (toolbar_toolbar != null) setSupportActionBar(toolbar_toolbar)
        if (supportActionBar != null) {
            actionBar = supportActionBar!!
            actionBar.setDisplayShowTitleEnabled(false)
            toolbar_tvTitle.textSize = 18f
            toolbar_tvTitle.text = "InstaApp"
            toolbar_btnBack.visibility = View.VISIBLE
            toolbar_btnBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    fun initialize(){
        mFragmentManager = supportFragmentManager
        mRef = FirebaseDatabase.getInstance().reference
        mStorageRef = FirebaseStorage.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        easyImage = EasyImage.Builder(this).setCopyImagesToPublicGalleryFolder(false)
            .allowMultiple(false).build()

        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            , 1)

        main_bottomnav.setOnNavigationItemSelectedListener(this)
        main_post.setOnClickListener(this)
        moveFragment(HomeFragment(), mFragmentManager, R.id.fragment_container)
    }

    fun moveFragment(fragment: Fragment, fragmentM: FragmentManager, view: Int){
        val fragmentTransaction = fragmentM.beginTransaction()
        fragmentTransaction.replace(view, fragment)
        fragmentTransaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var isMove = false
        when(item.itemId){
            R.id.navigation_home -> {
                moveFragment(HomeFragment(), mFragmentManager, R.id.fragment_container)
                isMove = true
            }
            R.id.navigation_profile -> {
                moveFragment(ProfileFragment(), mFragmentManager, R.id.fragment_container)
                isMove = true
            }
        }
        return isMove
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            1 -> {
                if (!grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback(){
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                mUri = Uri.fromFile(imageFiles[0].file)
                try {
//                    val bitmap = MediaStore.Images.Media.getBitmap(
//                        contentResolver,
//                        Uri.fromFile(imageFiles[0].file))
//                    payment_tvProof.text = imageFiles[0].file.name
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.main_post -> {
                easyImage.openChooser(this)
            }
        }
    }
}