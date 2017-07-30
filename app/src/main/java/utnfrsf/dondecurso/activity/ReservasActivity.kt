package utnfrsf.dondecurso.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.animation.*
import android.widget.ProgressBar
import io.paperdb.Paper
import retrofit2.Call
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.common.Util
import utnfrsf.dondecurso.common.async
import utnfrsf.dondecurso.common.enqueue
import utnfrsf.dondecurso.common.findView
import utnfrsf.dondecurso.domain.*
import utnfrsf.dondecurso.service.Api
import utnfrsf.dondecurso.view.ReservaAdapter
import utnfrsf.dondecurso.view.ReservaEspecialAdapter

class ReservasActivity : AppCompatActivity() {

    private var api = Api.service
    private var call: Call<String>? = null
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
private var progressBar: ProgressBar? = null

    private var fecha: String = ""
    private var carrera: Carrera? = null
    private var nivel: Nivel? = null
    private var materia: Materia? = null
    private var comision: Comision? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)

        val toolbar = findView<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        progressBar = findView(R.id.progressBar)

        val mViewPager = findView<ViewPager>(R.id.mViewPager)
        mViewPager.adapter = mSectionsPagerAdapter

        val tabs = findView<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(mViewPager)

        async {
            fecha = Paper.book().read<String>("fecha")
            carrera = Paper.book().read<Carrera>("carrera")
            nivel = Paper.book().read<Nivel>("nivel")
            materia = Paper.book().read<Materia>("materia")
            comision = Paper.book().read<Comision>("comision")
            requestDist()
        }

    }

    fun requestDist() {

        call = api.requestDistribution(fecha,
                carrera!!.id.toString(),
                nivel!!.id.toString(),
                materia!!.id.toString(),
                comision!!.id.toString())

        call?.enqueue({ _, response ->
            val mReservas = response?.body() as String
            progressBar!!.visibility = View.GONE
            val reservas = Util.fromJson(mReservas)
            val reservasEspeciales = Util.fromJsonReservasEspeciales(mReservas)
            for (frag in supportFragmentManager.fragments) {
                (frag as PlaceholderFragment).cargarReservas(reservas, reservasEspeciales)
            }
        }, { call, _ ->
            if (!call!!.isCanceled) {
                progressBar!!.visibility = View.GONE
                Snackbar.make(progressBar!!, getString(R.string.error_conexion), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.reintentar), { progressBar!!.visibility = View.VISIBLE; requestDist() })
                        .show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_reservas, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    class PlaceholderFragment : Fragment() {
        var reservas: ArrayList<Reserva> = ArrayList()
        var reservasEspeciales: ArrayList<ReservaEspecial> = ArrayList()
        var recyclerView: RecyclerView? = null
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.fragment_reservas, container, false)
            initRecyclerView(rootView)
            return rootView
        }

        fun initRecyclerView(rootView: View) {
            recyclerView = rootView.findView<RecyclerView>(R.id.recyclerViewReservas)

            if (arguments.get("section_number") == 1) {
                recyclerView?.adapter = ReservaAdapter(reservas)
            } else {
                recyclerView?.adapter = ReservaEspecialAdapter(reservasEspeciales)
            }

            recyclerView?.layoutManager = LinearLayoutManager(activity.applicationContext)
            recyclerView?.itemAnimator = DefaultItemAnimator()

            //Animación
            val set = AnimationSet(true)

            var animation: Animation = AlphaAnimation(0.2f, 1.0f)
            animation.duration = 300
            set.addAnimation(animation)

            animation = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
            )
            animation.duration = 300
            animation.interpolator = AccelerateDecelerateInterpolator()
            set.addAnimation(animation)

            val controller = LayoutAnimationController(set, 0.25f)

            recyclerView?.layoutAnimation = controller
        }

        fun cargarReservas(listaReservas: ArrayList<Reserva>, listaReservasEspeciales: ArrayList<ReservaEspecial>) {
            this.reservas.addAll(listaReservas)
            this.reservasEspeciales.addAll(listaReservasEspeciales)
            recyclerView?.adapter?.notifyDataSetChanged()

        }

        companion object {
            private val ARG_SECTION_NUMBER = "section_number"
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "RESERVAS"
                1 -> return "RESERVAS ESPECIALES"
                else -> return null
            }
        }
    }
}
