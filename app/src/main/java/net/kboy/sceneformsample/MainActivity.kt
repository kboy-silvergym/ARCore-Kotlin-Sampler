package net.kboy.sceneformsample

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.ImageView
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var selectedObject: Uri
    private lateinit var fragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeGallery()

        fragment = sceneform_fragment.let { it as ArFragment }
        fragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                return@setOnTapArPlaneListener
            }
            val anchor = hitResult.createAnchor()
            placeObject(fragment, anchor, selectedObject)
        }
}

    private fun initializeGallery() {
        val chair = ImageView(this)
        chair.setImageResource(R.drawable.chair_thumb)
        chair.setOnClickListener {
            selectedObject = Uri.parse("chair.sfb")
        }
        gallery_layout.addView(chair)

        val couch = ImageView(this)
        couch.setImageResource(R.drawable.couch_thumb)
        couch.setOnClickListener {
            selectedObject = Uri.parse("couch.sfb")
        }
        gallery_layout.addView(couch)

        val lamp = ImageView(this)
        lamp.setImageResource(R.drawable.lamp_thumb)
        lamp.setOnClickListener {
            selectedObject = Uri.parse("lamp.sfb")
        }
        gallery_layout.addView(lamp)
    }

    private fun placeObject(fragment: ArFragment, anchor: Anchor, model: Uri) {
        ModelRenderable.builder()
                .setSource(fragment.context, model)
                .build()
                .thenAccept {
                    addNodeToScene(fragment, anchor, it)
                }
                .exceptionally {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(it.message).setTitle("Error")
                    val dialog = builder.create()
                    dialog.show()
                    return@exceptionally null
                }
    }

    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(fragment.transformationSystem)
        node.renderable = renderable
        node.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        node.select()
    }

}
