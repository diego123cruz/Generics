package br.com.generics

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.generics.adapter.FrasesAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var telaAtual : MutableLiveData<Int> = MutableLiveData()
    private var fraseAdapter : FrasesAdapter? = null
    private var fraseList : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRecyclerView()

        telaAtual.observe(this, Observer {
            chengeListItens(it)
        })

        telaAtual.value = 0

    }

    fun chengeListItens(tela: Int){
        val res: Resources = resources

        var lista : List<String> = ArrayList()

        when (tela) {
            0 -> lista = res.getStringArray(R.array.frase1).asList()
            1 -> lista = res.getStringArray(R.array.frase2).asList()
            2 -> lista = res.getStringArray(R.array.frase3).asList()
            3 -> lista = res.getStringArray(R.array.frase4).asList()
        }

        fraseAdapter?.setLista(lista)
    }

    fun setRecyclerView(){
        fraseAdapter = FrasesAdapter(this)
        fraseAdapter?.setListener(fraseListener)
        recycler_frases.adapter = fraseAdapter
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recycler_frases.layoutManager = layoutManager
    }

    var fraseListener = object : FraseInterface {
        override fun onItemSelected(frase: String) {
            telaAtual.value?.let {
                fraseList += " $frase"

                val x = telaAtual.value
                val y = x?.plus(1)
                telaAtual.value = y

                txt_montar_frase.text = fraseList

                if(telaAtual.value == 4){
                    showFinish(fraseList)

                    telaAtual.value = 0
                    fraseList = ""
                    txt_montar_frase.text = getString(R.string.bemvindo)
                }
            }
        }
    }

    fun showFinish(str: String){
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("frase", str)
        startActivity(intent)
    }
}
