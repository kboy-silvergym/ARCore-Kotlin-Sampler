package net.kboy.sceneformsample.fragment

import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import net.kboy.sceneformsample.activity.ImageActivity

open class ImageArFragment: ArFragment() {

    override fun getSessionConfiguration(session: Session?): Config {
        planeDiscoveryController.setInstructionView(null)

        val config = Config(session)
        config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
        session?.configure(config)
        this.arSceneView.setupSession(session)

        if (session != null) {
            activity.let { it as? ImageActivity }?.setupAugmentedImageDb(config, session)
        }
        return config
    }
}
