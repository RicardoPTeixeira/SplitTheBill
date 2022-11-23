package com.example.ricardo.splitthebill.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.ricardo.splitthebill.R
import com.example.ricardo.splitthebill.model.Person

class PersonAdapter (
    context: Context,
    private val personList: MutableList<Person>
) : ArrayAdapter<Person>(context, R.layout.tile_person, personList) {
    private data class TilePersonHolder(val nameTv: TextView, val spentTv: TextView, val debtTv: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val person = personList[position]
        var personTileView = convertView
        if (personTileView == null) {
            // Inflo uma nova célula
            personTileView =
                (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.tile_person,
                    parent,
                    false
                )

            val tilePersonHolder = TilePersonHolder(
                personTileView.findViewById(R.id.nameTv),
                personTileView.findViewById(R.id.spentTv),
                personTileView.findViewById(R.id.debtTv)
            )
            personTileView.tag = tilePersonHolder
        }

        with(personTileView?.tag as TilePersonHolder) {
            nameTv.text = person.name
            spentTv.text = "Money Spent: " + person.spent
            if(person.debt.toDouble() <= 0){
                person.debt = (person.debt.toDouble() * -1).toString()
                debtTv.text = "Must Receive: R$" + person.debt
            }
            else debtTv.text = "Must Pay: R$" + person.debt
        }

        return personTileView
    }
}