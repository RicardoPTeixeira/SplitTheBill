package com.example.ricardo.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ricardo.splitthebill.R
import com.example.ricardo.splitthebill.adaptor.PersonAdapter
import com.example.ricardo.splitthebill.databinding.ActivityMainBinding
import com.example.ricardo.splitthebill.model.Constant.EXTRA_PERSON
import com.example.ricardo.splitthebill.model.Constant.VIEW_PERSON
import com.example.ricardo.splitthebill.model.Person

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val personList: MutableList<Person> = mutableListOf()

    // Adapter
    private lateinit var personAdapter: PersonAdapter

    private lateinit var parl: ActivityResultLauncher<Intent>

    private fun modifyPersonList(){
        var totalMoneyAmount = 0.0
        for (i in 0 until personList.size){
            totalMoneyAmount += personList[i].spent.toDouble()
        }
        val dividedMoneyAmount = totalMoneyAmount / personList.size
        for (i in 0 until personList.size){
            personList[i].debt = (dividedMoneyAmount - personList[i].spent.toDouble()).toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        personAdapter = PersonAdapter(this, personList)
        amb.personLv.adapter = personAdapter

        personAdapter.notifyDataSetChanged()

        parl = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),) {
                result ->
            if (result.resultCode == RESULT_OK) {
                val person = result.data?.getParcelableExtra<Person>(EXTRA_PERSON)

                person?.let { _person->
                    val position = personList.indexOfFirst { it.id == _person.id }
                    if (position != -1) {
                        // Alterar na posição
                        personList[position] = _person
                    }
                    else {
                        personList.add(_person)
                    }
                    personAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(amb.personLv)

        amb.personLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val person = personList[position]
                val personIntent = Intent(this@MainActivity, PersonActivity::class.java)
                personIntent.putExtra(EXTRA_PERSON, person)
                personIntent.putExtra(VIEW_PERSON, true)
                startActivity(personIntent)
            }

    }

    override fun onResume() {
        super.onResume()
        if(personList.size > 0) modifyPersonList()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addPersonMi -> {
                parl.launch(Intent(this, PersonActivity::class.java))
                true
            }
            else -> { false }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        return when(item.itemId) {
            R.id.removePersonMi -> {
                // Remove o contato
                personList.removeAt(position)
                modifyPersonList()
                personAdapter.notifyDataSetChanged()
                true
            }
            R.id.editPersonMi -> {
                // Chama a tela para editar o contato
                val person = personList[position]
                val personIntent = Intent(this, PersonActivity::class.java)
                personIntent.putExtra(EXTRA_PERSON, person)
                personIntent.putExtra(VIEW_PERSON, false)
                parl.launch(personIntent)
                true
            }
            else -> { false }
        }
    }
}