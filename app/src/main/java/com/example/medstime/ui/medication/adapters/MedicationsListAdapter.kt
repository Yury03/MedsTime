package com.example.medstime.ui.medication.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.medstime.R
import com.example.medstime.domain.models.MedicationIntakeModel
import java.time.LocalDateTime

class MedicationsListAdapter(
    private val dataList: List<MedicationIntakeModel>,
    private val medicationClick: (MedicationIntakeModel) -> Unit,
) :
    RecyclerView.Adapter<MedicationsListAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.medName)
        val dosage: TextView = itemView.findViewById(R.id.medDosage)
        val timeMedication: TextView = itemView.findViewById(R.id.timeMedication)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.medication_item, parent, false)
        return ViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)//TODO
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]
        holder.name.text = dataItem.name
        holder.dosage.text = buildString {
            append(dataItem.dosage.toString())
            append(" ")
            append(dataItem.dosageUnit)
        }
        with(holder.timeMedication) {
            //если время приема не настало, текста не будет
            if (timeHasCome(dataItem)) {
                if (dataItem.isTaken) {
                    text = buildString {
                        append(context.getString(R.string.medication_taken))
                        append(buildTimeString(dataItem.actualIntakeTime!!))
                    }
                    setTextColor(context.getColor(R.color.item_text_meds_taken))
                } else {
                    text = context.getString(R.string.medication_missed)
                    setTextColor(context.getColor(R.color.item_text_meds_missed))
                }
            }
        }
        holder.itemView.setOnClickListener {
            medicationClick(dataItem)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun timeHasCome(model: MedicationIntakeModel): Boolean {
        val currentDateTime = LocalDateTime.now()

        val intakeDateTime = LocalDateTime.of(
            currentDateTime.year,
            model.intakeDate.month,
            model.intakeDate.day,
            model.intakeTime.hour,
            model.intakeTime.minute
        )

        return currentDateTime.isEqual(intakeDateTime) || currentDateTime.isAfter(intakeDateTime)
    }

    private fun buildTimeString(time: MedicationIntakeModel.Time): String {
        with(time) {
            if (minute < 10) return "$hour:0$minute"
            else return "$hour:$minute"
        }
    }
}