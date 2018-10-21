package net.kboy.sceneformsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selectedObject: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeGallery()

        (sceneform_fragment as ArFragment)
                .setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
                    Log.d("model", selectedObject)
        }
    }

    private fun initializeGallery() {

        val chair = ImageView(this)
        chair.setImageResource(R.drawable.chair_thumb)
        chair.setOnClickListener {
            selectedObject = "chair"
        }
        gallery_layout.addView(chair)

        val couch = ImageView(this)
        couch.setImageResource(R.drawable.couch_thumb)
        couch.setOnClickListener {
            selectedObject = "couch"
        }
        gallery_layout.addView(couch)

        val lamp = ImageView(this)
        lamp.setImageResource(R.drawable.lamp_thumb)
        lamp.setOnClickListener {
            selectedObject = "lamp"
        }
        gallery_layout.addView(lamp)

    }

}
