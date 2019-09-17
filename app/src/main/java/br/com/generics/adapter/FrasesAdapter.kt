package br.com.generics.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.generics.FraseInterface
import br.com.generics.R

class FraseViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txt_frase = itemView.findViewById<TextView>(R.id.txt_list_frase)
    var view = itemView
}

class FrasesAdapter(context: Context) : RecyclerView.Adapter<FraseViewholder>() {

    private var lista : List<String> = ArrayList()
    private var cxt : Context = context
    private var delegate : FraseInterface? = null

    fun setLista(list: List<String>){
        lista = list
        notifyDataSetChanged()
    }

    fun setListener(listener : FraseInterface){
        delegate = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FraseViewholder {
        val v = LayoutInflater.from(cxt).inflate(R.layout.item_frase, parent, false)
        return FraseViewholder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: FraseViewholder, position: Int) {
        val frase = lista.get(position)

        holder.txt_frase.text = frase
        holder.view.setOnClickListener {
            if (delegate != null){
                delegate?.onItemSelected(frase)
            }
        }
    }
}