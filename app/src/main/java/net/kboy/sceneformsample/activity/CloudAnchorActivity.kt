package net.kboy.sceneformsample.activity

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
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
    private lateinit var fragment: CloudAnchorArFragment
    private var cloudAnchor: Anchor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_anchor)

        fragment =  cloudAnchorFragment.let { it as CloudAnchorArFragment }
        fragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->

            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                return@setOnTapArPlaneListener
            }

            val anchor = hitResult.createAnchor()
            setCloudAnchor(anchor)

            placeObject(fragment, cloudAnchor!!, Uri.parse("BighornSheep.sfb"))
        }

        clearButton.setOnClickListener {
            setCloudAnchor(null)
        }
    }

    private fun setCloudAnchor(newAnchor: Anchor?) {
        if (cloudAnchor != null) {
            cloudAnchor?.detach()
        }
        cloudAnchor = newAnchor
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
