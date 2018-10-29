package net.kboy.sceneformsample.activity.menu

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.kboy.sceneformsample.R
import net.kboy.sceneformsample.activity.CloudAnchorActivity
import net.kboy.sceneformsample.activity.ImageActivity
import net.kboy.sceneformsample.activity.SceneformActivity

class MainActivity : AppCompatActivity() {

    private val adapter by lazy {
        MenuAdapter {
            when (it) {
                0 -> {
                    val intent = Intent(this, SceneformActivity::class.java)
                    startActivity(intent)
                }
                1 -> {
                    val intent = Intent(this, ImageActivity::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this, CloudAnchorActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.adapter = adapter
    }
}
