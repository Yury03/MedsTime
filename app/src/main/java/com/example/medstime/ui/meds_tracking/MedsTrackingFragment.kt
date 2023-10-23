package com.example.medstime.ui.meds_tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.example.medstime.ui.meds_tracking.components.MedsTrackPlaceholder
import com.example.medstime.ui.meds_tracking.components.MedsTrackingItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedsTrackingFragment : Fragment() {
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
        val state = remember {
            viewModel.state.value ?: MedsTrackState(
                isLoading = true,
                medsTrackList = listOf()
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                if (state.medsTrackList.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(state.medsTrackList) { _, medsTrackItem ->
                            MedsTrackingItem(trackModel = medsTrackItem)
                        }
                    }
                } else {
                    MedsTrackPlaceholder()
                }
            }
        }
    }
}



