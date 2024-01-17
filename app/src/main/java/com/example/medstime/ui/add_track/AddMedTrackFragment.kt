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

/**Фрагмент **AddMedTrackFragment** в основном нужен, чтобы изменять поле ***medTrack***, принадлежащее
 *  классу **AddMedState**. Это необходимо для упрощения UI. Фрагмент принимает в аргументы
 *  весь **AddMedState** и может менять ***medName***, ***dosageUnits***, ***medTrack***.*/
class AddMedTrackFragment : Fragment() {

    private val viewModel by viewModel<AddMedTrackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            val addMedStateJson = args.getString(AddMedFragment.ARG_KEY_STATE)!!
            viewModel.send(AddMedTrackEvent.HandleArguments(addMedStateJson))

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.state.collectAsState()
                val navController = requireActivity().findNavController(R.id.fragmentContainerView)
                val backToAddMedScreen = {
                    navController.navigate(
                        R.id.addMedFragment, Bundle().apply {
                            putString(
                                AddMedFragment.ARG_KEY_STATE,
                                viewModel.state.value.addMedStateJson
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

    companion object {

        const val ARG_KEY_TRACK_MODEL = "trackModel"
        const val LOG_TAG = "AddMedTrackFragment"
    }
}