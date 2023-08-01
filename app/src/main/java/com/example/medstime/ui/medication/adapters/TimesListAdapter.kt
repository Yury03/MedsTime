package com.example.medstime.ui.medication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medstime.R
import com.example.medstime.domain.models.MedicationIntakeModel

class TimesListAdapter(private val dataList: List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>) :
    RecyclerView.Adapter<TimesListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.timeMedication)
        val intakeList: RecyclerView = itemView.findViewById(R.id.intakeList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.medication_with_time_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]
        val time = dataItem.first.toString() + ":" + dataItem.second.toString()
        holder.time.text = time
        holder.intakeList.adapter = MedicationsListAdapter(dataItem.second)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
