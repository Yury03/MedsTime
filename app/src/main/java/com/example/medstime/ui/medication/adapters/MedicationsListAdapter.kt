package com.example.medstime.ui.medication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medstime.R

class MedicationsListAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<MedicationsListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Здесь можно инициализировать вьюхи элемента списка
        // Например:
        // val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
    }

    // Создание нового ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.medication_item, parent, false)
        return ViewHolder(itemView)
    }

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]

        // Присваиваем значения вьюхам элемента списка на основе данных из списка
        // Например:
        // holder.textViewTitle.text = dataItem.title
    }

    // Возвращаем количество элементов в списке
    override fun getItemCount(): Int {
        return dataList.size
    }
}