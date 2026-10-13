package com.example.a24012021002_mad_p7

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a24012021002_mad_p7.databinding.PersonItemViewBinding
import java.io.Serializable

class PersonAdapter(
    private val context: Context,
    private val array: ArrayList<Person>
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(val binding: PersonItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = PersonItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PersonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = array[position]
        with(holder.binding) {
            textviewName.text = person.name
            textviewPhone.text = person.phoneNo
            textviewEmail.text = person.emailId
            textviewAddress.text = person.address

            val obj = person as Serializable
            buttonMap.setOnClickListener {
                Intent(this@PersonAdapter.context, MapActivity::class.java).apply {
                    putExtra("Object", obj)
                    this@PersonAdapter.context.startActivity(this)
                }
            }

            buttonDelete.setOnClickListener {
                val currentPos = holder.adapterPosition
                if (currentPos != RecyclerView.NO_POSITION) {
                    (context as MainActivity).deletePerson(currentPos)
                }
            }
        }
    }
}
