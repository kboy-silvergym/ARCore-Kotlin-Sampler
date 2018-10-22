package net.kboy.sceneformsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    private lateinit var arFragment: CustomArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        arFragment = sceneformFragment.let { it as CustomArFragment }
        arFragment.planeDiscoveryController.hide()
    }
}