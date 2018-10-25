package net.kboy.sceneformsample.fragment

import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class CloudAnchorArFragment : ArFragment() {

    override fun getSessionConfiguration(session: Session?): Config {
        planeDiscoveryController.setInstructionView(null)

        val config = super.getSessionConfiguration(session)
        config.cloudAnchorMode = Config.CloudAnchorMode.ENABLED
        return config
    }
}
