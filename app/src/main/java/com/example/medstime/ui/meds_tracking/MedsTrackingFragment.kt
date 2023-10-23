package com.example.medstime.ui.meds_tracking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import com.example.medstime.R
import com.example.medstime.ui.meds_tracking.components.MedsTrackPlaceholder
import com.example.medstime.ui.meds_tracking.components.MedsTrackingList
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedsTrackingFragment : Fragment() {
    companion object {
        private const val TAG = "MedsTrackingFragment"
    }

    //todo при полной миграции оставить только MedsTrackingScreen()
    private val viewModel by viewModel<MedsTrackViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MedsTrackingScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun MedsTrackingScreen() {
        val state by viewModel.state.asFlow().collectAsState(
            initial = MedsTrackState(
                isLoading = true,
                medsTrackList = listOf()
            )
        )
        Log.i(TAG, "[UPDATE STATE]: $state")
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(ContextCompat.getColor(requireContext(), R.color.screen_back))),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                Log.i(TAG, "[UPDATE]: state.isLoading = true")
                CircularProgressIndicator()
            } else {
                Log.i(TAG, "[UPDATE]: state.isLoading = false")
                MedsTrackingList(trackingList = state.medsTrackList)
                if (state.medsTrackList.isEmpty()) {
                    MedsTrackPlaceholder()
                }
            }
        }
    }
}



