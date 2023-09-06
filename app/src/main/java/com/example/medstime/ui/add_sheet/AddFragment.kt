package com.example.medstime.ui.add_sheet

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.medstime.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddFragment : BottomSheetDialogFragment(R.layout.bottom_sheet_add) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.findViewById<Button>(R.id.addMedsButton).setOnClickListener {
//            findNavController().navigate(R.id.addMedicationFragment)
//            dismiss()
//        }
//        view.findViewById<Button>(R.id.addTrackButton).setOnClickListener {
//            findNavController().navigate(R.id.addMedFragment)
//            dismiss()
//        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}