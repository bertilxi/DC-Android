package utnfrsf.dondecurso.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.paperdb.Paper
import utnfrsf.dondecurso.common.launchActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)
        launchActivity(MainActivity())
        finish()
    }
}