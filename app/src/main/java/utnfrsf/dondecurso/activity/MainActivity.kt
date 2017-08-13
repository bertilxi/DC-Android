package utnfrsf.dondecurso.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.common.findView
import utnfrsf.dondecurso.fragment.AboutFragment
import utnfrsf.dondecurso.fragment.FavouritesFragment
import utnfrsf.dondecurso.fragment.QueryFragment

class MainActivity : AppCompatActivity() {

    private var fragment: Fragment? = null
    private var fragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(utnfrsf.dondecurso.R.layout.activity_main)

        val toolbar = findView<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        fragmentManager = supportFragmentManager
        val bottomMenu = findView<BottomNavigationView>(R.id.bottomMenu)

        bottomMenu.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_favourite -> fragment = FavouritesFragment()
                R.id.tab_search -> fragment = QueryFragment()
                R.id.tab_next -> fragment = AboutFragment()
                else -> fragment = QueryFragment()
            }
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.main_container, fragment).commit()
            true
        }
        bottomMenu.selectedItemId = R.id.tab_favourite
    }
}

