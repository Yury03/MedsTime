package com.example.medstime.ui.medication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medstime.R
import com.example.medstime.domain.models.MedicationIntakeModel

class MedicationsListAdapter(private val dataList: List<MedicationIntakeModel>) :
    RecyclerView.Adapter<MedicationsListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.medName)
        val dosage: TextView = itemView.findViewById(R.id.medDosage)
        val timeMedication: TextView = itemView.findViewById(R.id.timeMedication)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.medication_item, parent, false)
        return ViewHolder(itemView)
    }

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]
        holder.name.text = dataItem.name
        holder.dosage.text = buildString {
        append(dataItem.dosage.toString())
        append(" ")
        append(dataItem.dosageUnit)
    }
        with(holder.timeMedication) {
            text = context.getString(R.string.add_medication_item)
            if (dataItem.isTaken) {
                setTextColor(context.getColor(R.color.item_text_meds_taken))
            }
        }


    }

    // Возвращаем количество элементов в списке
    override fun getItemCount(): Int {
        return dataList.size
    }
}