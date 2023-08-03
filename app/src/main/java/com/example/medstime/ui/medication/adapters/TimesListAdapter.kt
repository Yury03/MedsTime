package com.example.medstime.ui.medication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medstime.R
import com.example.medstime.domain.models.MedicationIntakeModel

class TimesListAdapter(
    private val dataList: List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>,
    private val context: Context,
    private val medicationClick: (MedicationIntakeModel) -> Unit,
) :
    RecyclerView.Adapter<TimesListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.timeMedicationWithTimeItem)
        val intakeList: RecyclerView = itemView.findViewById(R.id.intakeList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.medication_with_time_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]
        holder.time.text = buildTimeString(dataItem.first)
        holder.intakeList.layoutManager = LinearLayoutManager(context)
        holder.intakeList.adapter = MedicationsListAdapter(dataItem.second, medicationClick)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun buildTimeString(time: MedicationIntakeModel.Time): String {
        with(time) {
            if (minute < 10) return "$hour:0$minute"
            else return "$hour:$minute"
        }
    }
}
