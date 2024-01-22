package com.example.medstime.ui.add_track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.medstime.R
import com.example.medstime.ui.add_track.components.AddMedTrack
import com.example.medstime.ui.utils.ARG_KEY_STATE
import org.koin.androidx.viewmodel.ext.android.viewModel

/**Фрагмент **AddMedTrackFragment** в основном нужен, чтобы изменять поле ***medTrack***, принадлежащее
 *  классу **AddMedState**. Это необходимо для упрощения UI. Фрагмент принимает в аргументы
 *  весь **AddMedState** и может менять ***medName***, ***dosageUnits***, ***medTrack***.*/
class AddMedTrackFragment : Fragment() {

    private val viewModel by viewModel<AddMedTrackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().getString(ARG_KEY_STATE)?.let { addMedStateJson ->
            viewModel.send(AddMedTrackEvent.HandleArguments(addMedStateJson))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            val navController = requireActivity().findNavController(R.id.fragmentContainerView)
            val backToAddMedScreen: () -> Unit = {
                val state = viewModel.state.value.addMedStateJson
                val action =
                    AddMedTrackFragmentDirections.actionAddMedTrackFragmentToAddMedFragment(state = state)
                navController.navigate(action)
            }
            setOnBackButtonCallback(backToAddMedScreen)
            setContent {
                val uiState by viewModel.state.collectAsState()
                AddMedTrack(uiState = uiState,
                    onBackButtonClick = backToAddMedScreen,
                    sendEvent = { event ->
                        viewModel.send(event)
                    })
            }
        }
    }

    private fun setOnBackButtonCallback(backToAddMedScreen: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.send(AddMedTrackEvent.BackButtonClicked)
                    backToAddMedScreen()
                }
            })
    }

    companion object {

        //        const val ARG_KEY_TRACK_MODEL = "trackModel"
        const val LOG_TAG = "AddMedTrackFragment"
    }
}