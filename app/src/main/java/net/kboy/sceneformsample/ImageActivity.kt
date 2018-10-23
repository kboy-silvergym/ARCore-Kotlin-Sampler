package net.kboy.sceneformsample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_image.*
import java.io.IOException

class ImageActivity : AppCompatActivity() {

    private lateinit var arFragment: CustomArFragment
    private var shouldAddModel: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        arFragment = sceneformFragment.let { it as CustomArFragment }
        arFragment.planeDiscoveryController.hide()
        arFragment.arSceneView.scene.addOnUpdateListener {
            val frame = arFragment.arSceneView.arFrame
            val augmentedImages: Collection<AugmentedImage> = frame.getUpdatedTrackables(AugmentedImage::class.java)
            augmentedImages.forEach {
                if (it.trackingState == TrackingState.TRACKING && shouldAddModel){

                    if (it.name == "airplane") {
                        placeObject(arFragment, it.createAnchor(it.centerPose),Uri.parse("Airplane.sfb"))
                        shouldAddModel = false
                    }
                }
            }

        }
    }

    fun setupAugmentedImageDb(config: Config, session: Session){
        val bitmap: Bitmap = loadAugmentedImage() ?: return
        val database = AugmentedImageDatabase(session)
        database.addImage("airplane", bitmap)
        config.augmentedImageDatabase = database
    }

    private fun loadAugmentedImage(): Bitmap? {
        try {
            val inputStream = assets.open("airplane.jpg")
            return  BitmapFactory.decodeStream(inputStream)
        } catch(e: IOException) {
            Log.d("imageLoad", "io exception while loading", e)
        }
        return null
    }

    // FIXME: copied from MainActivity
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