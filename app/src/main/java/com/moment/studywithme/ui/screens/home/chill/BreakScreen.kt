package com.moment.studywithme.ui.screens.home.chill

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.moment.studywithme.R
import com.moment.studywithme.ui.component.SWMButton
import com.moment.studywithme.ui.component.SWMIconButton
import com.moment.studywithme.ui.screens.home.TimerPickerBottomSheetDialog
import com.moment.studywithme.ui.screens.home.pomodoro.TimerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreakScreen(
    viewModel: BreakViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val buttonLabel = when (uiState.timerState) {
        TimerState.START -> "Start break"
        TimerState.PLAY -> "Pause"
        TimerState.PAUSE -> "Play"
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val openSettingsDialog = remember { mutableStateOf(false) }

    val openDefaultTimerDialog = remember {
        mutableStateOf(false)
    }

    val timerSheetState = rememberModalBottomSheetState()

    if (openDefaultTimerDialog.value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            TimerPickerBottomSheetDialog(sheetState = timerSheetState,
                defaultTimer = uiState.userPreferences.defaultBreakInMs,
                onDismiss = {
                    openDefaultTimerDialog.value = false
                    openSettingsDialog.value = true
                },
                onTimerChanged = {
                    openDefaultTimerDialog.value = false
                    viewModel.updateUserPreferencesTimer(it)
                })
        }
    }

    if (openSettingsDialog.value) {
         BreakBottomSheetDialog(
            sheetState = sheetState,
            defaultTimer = uiState.timer,
            onTimerChanged = {
                openSettingsDialog.value = false
                openDefaultTimerDialog.value = true
            },
            onTimerReset = {
                viewModel.resetTimer()
                scope.launch { sheetState.hide() }
                openSettingsDialog.value = false
            },
            onDismiss = {
                scope.launch { sheetState.hide() }
                openSettingsDialog.value = false
            }
        )
    }

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(18.dp)),
        contentAlignment = Alignment.BottomCenter,
        content = {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = "https://i.pinimg.com/originals/a9/47/20/a9472027ad563404eb2419a00149af60.gif",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                imageLoader = imageLoader
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = uiState.timer,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 96.sp,
                    textAlign = TextAlign.Center
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SWMIconButton(
                        modifier = Modifier.size(50.dp),
                        icon = R.drawable.ic_more,
                        onClick = {
                            openSettingsDialog.value = true
                            scope.launch { sheetState.show()
                            }
                        })
                    SWMButton(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp), label = buttonLabel, onClick = {
                        when (uiState.timerState) {
                            TimerState.PLAY -> viewModel.commandTimer(TimerState.PAUSE)
                            TimerState.PAUSE -> viewModel.commandTimer(TimerState.PLAY)
                            TimerState.START -> viewModel.commandTimer(TimerState.START)
                        }
                    })
                }
            }
        }
    )
}
