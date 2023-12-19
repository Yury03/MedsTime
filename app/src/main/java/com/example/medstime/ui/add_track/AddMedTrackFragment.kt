package com.example.medstime.ui.add_track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
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

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.state.collectAsState()
                val navController = requireActivity().findNavController(R.id.fragmentContainerView)
                val backToAddMedScreen = {
                    navController.navigate(
                        R.id.addMedFragment,
                        Bundle().apply {
                            putString(
                                AddMedFragment.ARG_KEY_STATE,
                                addMedState
                            )
                        }
                    )
                }
                AddMedTrack(
                    uiState = uiState,
                    onBackButtonClick = backToAddMedScreen,
                    sendEvent = { event ->
                        viewModel.send(event)
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationBar()
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