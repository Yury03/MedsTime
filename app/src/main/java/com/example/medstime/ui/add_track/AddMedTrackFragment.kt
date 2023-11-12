package com.example.medstime.ui.add_track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.medstime.ui.add_track.components.AddMedTrack
import com.example.medstime.ui.main_activity.MainActivity


class AddMedTrackFragment : Fragment() {
    companion object {
        const val ARG_KEY_MED_NAME = "med_name"
        const val ARG_KEY_DOSAGE_UNITS = "dosage_units"
        const val ARG_KEY_MEDS_TRACK_MODEL_ID = "meds_track_model_id"
    }

    private lateinit var medName: String
    private lateinit var dosageUnits: String
    private lateinit var medsTrackModelId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideBottomNavigationBar()
        arguments?.let { args ->
            medName = args.getString(ARG_KEY_MED_NAME) ?: ""
            dosageUnits = args.getString(ARG_KEY_DOSAGE_UNITS) ?: ""
            medsTrackModelId = args.getString(ARG_KEY_MEDS_TRACK_MODEL_ID)
                ?: ""//todo get model in view model if id is not empty
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AddMedTrackScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AddMedTrackScreen() {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            AddMedTrack(
                medName = medName,
                dosageUnit = dosageUnits,
            )
        }
    }
    private fun hideBottomNavigationBar() {
        (requireActivity() as MainActivity).hideBottomNavigationBar()
    }
    private fun showBottomNavigationBar() {
        (requireActivity() as MainActivity).showBottomNavigationBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigationBar()
    }

}