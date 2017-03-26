package utnfrsf.dondecurso

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.activity_reservas.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utnfrsf.dondecurso.adapter.ReservaAdapter
import utnfrsf.dondecurso.adapter.ReservaEspecialAdapter
import utnfrsf.dondecurso.common.fromJson
import utnfrsf.dondecurso.common.fromJsonReservasEspeciales
import utnfrsf.dondecurso.domain.*
import utnfrsf.dondecurso.service.Api
import utnfrsf.dondecurso.service.ApiEndpoints

class ReservasActivity : AppCompatActivity() {

    var apiService: ApiEndpoints? = null
    var call: Call<String>? = null

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)

        apiService = Api(this).service
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        val fecha = intent.getStringExtra("fecha")
        val carrera = intent.getSerializableExtra("carrera") as Carrera
        val nivel = intent.getSerializableExtra("nivel") as Nivel
        val materia = intent.getSerializableExtra("materia") as Materia
        val comision = intent.getSerializableExtra("comision") as Comision

        call = apiService!!.requestDistribution(fecha,
                carrera.id.toString(),
                nivel.id.toString(),
                materia.id.toString(),
                comision.id.toString())
        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                val mReservas = response?.body() as String
                progressBar.visibility = View.GONE
                val reservas = fromJson(mReservas)
                val reservasEspeciales = fromJsonReservasEspeciales(mReservas)
                for (frag in supportFragmentManager.fragments) {
                    (frag as PlaceholderFragment).cargarReservas(reservas, reservasEspeciales)
                }
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {
                if (!call!!.isCanceled) {
                    progressBar.visibility = View.GONE
                    Snackbar.make(main_content, getString(R.string.error_conexion), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.reintentar), { progressBar.visibility = View.VISIBLE; call.clone().enqueue(this) })
                            .show()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_reservas, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {
        var reservas: ArrayList<Reserva> = ArrayList()
        var reservasEspeciales: ArrayList<ReservaEspecial> = ArrayList()
        var recyclerView: RecyclerView? = null
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.fragment_reservas, container, false)
            initRecyclerView(rootView)
            return rootView
        }

        fun initRecyclerView(rootView: View) {
            recyclerView = rootView.findViewById(R.id.recyclerViewReservas) as RecyclerView

            if (arguments.get("section_number") == 1) {
                recyclerView?.adapter = ReservaAdapter(reservas)
            } else {
                recyclerView?.adapter = ReservaEspecialAdapter(reservasEspeciales)
            }

            recyclerView?.layoutManager = LinearLayoutManager(activity.applicationContext) as RecyclerView.LayoutManager?
            recyclerView?.itemAnimator = DefaultItemAnimator()
            val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            recyclerView?.addItemDecoration(itemDecoration)
        }

        fun cargarReservas(listaReservas: ArrayList<Reserva>, listaReservasEspeciales: ArrayList<ReservaEspecial>) {
            Log.d("APP", listaReservas.toString())
            this.reservas.addAll(listaReservas)
            this.reservasEspeciales.addAll(listaReservasEspeciales)
            recyclerView?.adapter?.notifyDataSetChanged()

        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "RESERVAS"
                1 -> return "RESERVAS ESPECIALES"
            }
            return null
        }
    }
}
