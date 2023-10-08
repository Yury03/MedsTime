package com.example.medstime.ui.medication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.MedicationIntakeModel
import com.example.medstime.R
import net.cachapa.expandablelayout.ExpandableLayout

class MedicationsListAdapter(
    private val dataList: List<MedicationIntakeModel>,
    private val medicationClick: (MedicationIntakeModel, String, View) -> Map<Int, View.OnClickListener>,
    private val context: Context,
) :
    RecyclerView.Adapter<MedicationsListAdapter.ViewHolder>() {
    enum class Status { NONE, IS_TAKEN, IS_NOT_TAKEN }

    private var status = Status.NONE


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.medName)
        val dosage: TextView = itemView.findViewById(R.id.medDosage)
        val timeMedication: TextView = itemView.findViewById(R.id.timeMedication)
        val removeButton: Button = itemView.findViewById(R.id.itemRemoveButton)
        val takenButton: Button = itemView.findViewById(R.id.itemTakenButton)
        val changeTimeButton: Button = itemView.findViewById(R.id.itemChangeTimeTakeButton)
        val skippedButton: Button = itemView.findViewById(R.id.itemSkippedButton)
        val editButton: Button = itemView.findViewById(R.id.itemEditButton)
        val remove: Button = itemView.findViewById(R.id.itemRemoveButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.medication_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]
        holder.name.text = dataItem.name
        holder.dosage.text = buildDosageString(dataItem)

        with(holder.timeMedication) {
            when (dataItem.isTaken) {
                true -> {
                    if (dataItem.actualIntakeTime != null) {
                        status = Status.IS_TAKEN
                        text = buildString {
                            append(context.getString(R.string.medication_taken))
                            append(buildTimeString(dataItem.actualIntakeTime!!))
                        }
                        setTextColor(context.getColor(R.color.medication_item_text_meds_taken))
                    }
                }

                false -> {
                    status = Status.IS_NOT_TAKEN
                    text = context.getString(R.string.medication_missed)
                    setTextColor(context.getColor(R.color.medication_item_text_meds_missed))
                }
            }
        }
        setVisibility(holder, status)

        holder.itemView.setOnClickListener {
            val expandableLayout = it.findViewById<ExpandableLayout>(R.id.buttonLayer)
            if (expandableLayout.isExpanded) {
                expandableLayout.collapse()
            } else {
                expandableLayout.expand()
            }
            val itemClickMap =
                medicationClick(dataItem, buildTimeAndDosageText(dataItem), holder.itemView)
            setButtonClick(itemClickMap, holder)
        }
    }

    private fun setButtonClick(itemClickMap: Map<Int, View.OnClickListener>, holder: ViewHolder) {
        with(holder) {
            changeTimeButton.setOnClickListener(itemClickMap[changeTimeButton.id])
            takenButton.setOnClickListener(itemClickMap[takenButton.id])
            skippedButton.setOnClickListener(itemClickMap[skippedButton.id])
            editButton.setOnClickListener(itemClickMap[editButton.id])
            removeButton.setOnClickListener(itemClickMap[removeButton.id])
        }
    }

    private fun setVisibility(holder: ViewHolder, status: Status) {
        with(holder) {
            when (status) {
                Status.NONE -> {
                    changeTimeButton.visibility = View.GONE
                    takenButton.visibility = View.VISIBLE
                    skippedButton.visibility = View.VISIBLE
                }

                Status.IS_TAKEN -> {
                    changeTimeButton.visibility = View.VISIBLE
                    takenButton.visibility = View.GONE
                    skippedButton.visibility = View.VISIBLE
                }

                Status.IS_NOT_TAKEN -> {
                    changeTimeButton.visibility = View.GONE
                    takenButton.visibility = View.VISIBLE
                    skippedButton.visibility = View.GONE
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    private fun buildTimeAndDosageText(medicationIntakeModel: MedicationIntakeModel): String {
        val time = buildTimeString(medicationIntakeModel.intakeTime)
        val dosage = buildDosageString(medicationIntakeModel)
        return "$time $dosage"
    }

    private fun buildTimeString(time: MedicationIntakeModel.Time): String =
        with(time) {
            if (minute < 10) return "$hour:0$minute"
            else return "$hour:$minute"
        }

    private fun buildDosageString(medicationIntakeModel: MedicationIntakeModel): String {
        val commonText =
            "${medicationIntakeModel.dosageUnit} ${medicationIntakeModel.intakeType.toRussianString()}"
        return if (medicationIntakeModel.dosage == medicationIntakeModel.dosage.toInt().toDouble())
            "${medicationIntakeModel.dosage.toInt()} $commonText"
        else
            "${medicationIntakeModel.dosage} $commonText"
    }

    private fun MedicationIntakeModel.IntakeType.toRussianString() =
        when (this) {
            MedicationIntakeModel.IntakeType.AFTER_MEAL -> context.getString(R.string.after_meal)
            MedicationIntakeModel.IntakeType.BEFORE_MEAL -> context.getString(R.string.before_meal)
            MedicationIntakeModel.IntakeType.DURING_MEAL -> context.getString(R.string.during_meal)
            MedicationIntakeModel.IntakeType.NONE -> context.getString(R.string.empty_string)
        }
}