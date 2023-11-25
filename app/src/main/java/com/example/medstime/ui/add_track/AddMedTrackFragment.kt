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
import androidx.navigation.findNavController
import com.example.medstime.R
import com.example.medstime.ui.add_med.AddMedFragment
import com.example.medstime.ui.add_track.components.AddMedTrack
import com.example.medstime.ui.main_activity.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddMedTrackFragment : Fragment() {
    companion object {
        const val ARG_KEY_MED_NAME = "med_name"
        const val ARG_KEY_DOSAGE_UNITS = "dosage_units"
        const val ARG_KEY_MEDS_TRACK_MODEL_ID = "meds_track_model_id"
        const val LOG_TAG = "AddMedTrackFragment"
    }

    private val viewModel by viewModel<AddMedTrackViewModel>()
    private var medsTrackModelId: String? = null
    private lateinit var medName: String
    private lateinit var dosageUnits: String
    private lateinit var addMedState: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideBottomNavigationBar()
        arguments?.let { args ->
            medName = args.getString(ARG_KEY_MED_NAME) ?: ""
            dosageUnits = args.getString(ARG_KEY_DOSAGE_UNITS) ?: ""
            medsTrackModelId = args.getString(ARG_KEY_MEDS_TRACK_MODEL_ID)
            addMedState = args.getString(AddMedFragment.ARG_KEY_STATE)!!
            viewModel.send(
                AddMedTrackEvent.HandleArguments(
                    medName = medName,
                    dosageUnits = dosageUnits,
                    medsTrackModelId = medsTrackModelId,
                )
            )
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
                navController = requireActivity().findNavController(R.id.fragmentContainerView),
                addMedFragmentState = addMedState,
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