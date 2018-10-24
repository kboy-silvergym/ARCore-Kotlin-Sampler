package net.kboy.sceneformsample.activity

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_cloud_anchor.*
import net.kboy.sceneformsample.R
import net.kboy.sceneformsample.fragment.CloudAnchorArFragment

class CloudAnchorActivity : AppCompatActivity() {
    private enum class AnchorState {
        NONE,
        HOSTING,
        HOSTED
    }

    private lateinit var fragment: CloudAnchorArFragment
    private var cloudAnchor: Anchor? = null
    private var state: AnchorState = AnchorState.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_anchor)

        fragment = cloudAnchorFragment.let { it as CloudAnchorArFragment }
        fragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->

            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING ||
                    state != AnchorState.NONE) {
                return@setOnTapArPlaneListener
            }

            val anchor = hitResult.createAnchor()
            setCloudAnchor(anchor)

            placeObject(fragment, cloudAnchor!!, Uri.parse("BighornSheep.sfb"))

            state = AnchorState.HOSTING
            Toast.makeText(this,
                    "Now hosting anchor...",
                    Toast.LENGTH_SHORT)
                    .show()
        }
        fragment.arSceneView.scene.addOnUpdateListener {
            checkUpdatedAnchor()
        }

        clearButton.setOnClickListener {
            setCloudAnchor(null)
        }
        fragment.planeDiscoveryController.hide()
    }

    private fun setCloudAnchor(newAnchor: Anchor?) {
        if (cloudAnchor != null) {
            cloudAnchor?.detach()
        }
        cloudAnchor = newAnchor

        state = AnchorState.NONE
        Toast.makeText(this,
                "Finish hosting anchor...",
                Toast.LENGTH_SHORT)
                .show()
    }

    // should be synchronized method?
    private fun checkUpdatedAnchor(){
        if (state != AnchorState.HOSTING) {
            return
        }
        val cloudAnchorState: Anchor.CloudAnchorState = cloudAnchor!!.cloudAnchorState

        if (cloudAnchorState.isError) {
            Toast.makeText(this,
                    "Error!" + cloudAnchorState.toString(),
                    Toast.LENGTH_SHORT)
                    .show()
            state = AnchorState.NONE
        } else if (cloudAnchorState == Anchor.CloudAnchorState.SUCCESS) {
            Toast.makeText(this,
                    "Anchor Hosted Cloud ID" + cloudAnchor!!.cloudAnchorId,
                    Toast.LENGTH_SHORT)
                    .show()
            state = AnchorState.NONE
        }

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
