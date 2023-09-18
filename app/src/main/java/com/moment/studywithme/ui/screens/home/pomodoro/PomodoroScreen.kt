package com.moment.studywithme.ui.screens.home.pomodoro

import android.annotation.SuppressLint
import android.graphics.Insets.add
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.moment.studywithme.R
import com.moment.studywithme.domain.model.Task
import com.moment.studywithme.ui.component.SWMButton
import com.moment.studywithme.ui.component.SWMIconButton
import com.moment.studywithme.ui.screens.home.TimerPickerBottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val buttonLabel = when (uiState.timerState) {
        TimerState.START -> "Start pomodoro"
        TimerState.PLAY -> "Pause"
        TimerState.PAUSE -> "Play"
    }
    val scope = rememberCoroutineScope()
    val openSettingsDialog = remember { mutableStateOf(false) }
    val openDefaultTimerDialog = remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val timerSheetState = rememberModalBottomSheetState()
    if (openDefaultTimerDialog.value) {
        if (SDK_INT >= Build.VERSION_CODES.Q) {
            TimerPickerBottomSheetDialog(sheetState = timerSheetState,
                defaultTimer = uiState.userPreferences.defaultTimerInMs,
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
        PomodoroBottomSheetDialog(
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
            onRoundsReset = {
                viewModel.resetRounds()
                openSettingsDialog.value = false
            },
            onDeleteAllTasks = {
                viewModel.deleteAllTasks()
                openSettingsDialog.value = false
            },
            onDismiss = {
                scope.launch { sheetState.hide() }
                openSettingsDialog.value = false
            }
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TimerBox(uiState.progress)
            PomodoroDashboard(uiState.timer, uiState.tasks.size, uiState.userPreferences.nbRounds)
        }
        Column(
            modifier = Modifier.weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TaskList(
                uiState.tasks,
                updateTask = {
                    viewModel.updateTask(it)
                },
                deleteTask = {
                    viewModel.deleteTask(it)
                })
        }
        Column(
            modifier = Modifier.weight(1.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputCreateTask { taskCreated ->
                viewModel.addTask(taskCreated)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SWMIconButton(
                    modifier = Modifier.size(50.dp),
                    icon = R.drawable.ic_more,
                    onClick = {
                        openSettingsDialog.value = true
                        scope.launch { sheetState.show() }
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
}

@Composable
fun TimerBox(progress: Float) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center,
        content = {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress
            )
            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50))
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = "https://cdn.pixilart.com/photos/orginal/6cd473f2bd5f695.gif",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    imageLoader = imageLoader
                )
            }
        }
    )
}

@Composable
fun PomodoroDashboard(timer: String, nbTask: Int, nbRounds: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        PomodoroDashboardSpec(spec = nbTask.toString(), name = "Tasks")
        Spacer(Modifier.weight(1f))
        PomodoroTimer(timer = timer)
        Spacer(Modifier.weight(1f))
        PomodoroDashboardSpec(spec = nbRounds.toString(), name = "Round")
    }
}

@Composable
fun PomodoroTimer(timer: String) {
    Text(text = timer, style = MaterialTheme.typography.titleLarge, lineHeight = 32.sp)
}

@Composable
fun PomodoroDashboardSpec(
    spec: String,
    name: String,
) {
    Text(text = spec, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.width(2.dp))
    Text(text = name, style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun TaskList(
    tasks: List<Task>,
    updateTask: (task: Task) -> Unit,
    deleteTask: (task: Task) -> Unit
) {
    val openDeleteTaskDialog = remember { mutableStateOf(false) }
    val selectedTask = remember { mutableStateOf(Task("", "", false)) }
    if (openDeleteTaskDialog.value)
        DeleteTaskDialog(
            onDismissRequest = { openDeleteTaskDialog.value = false },
            onConfirmation = {
                openDeleteTaskDialog.value = false
                deleteTask(selectedTask.value)
            },
            taskName = selectedTask.value.name.toString()
        )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    onCheckChanged = {
                        task.isDone = !task.isDone!!
                        updateTask(task)
                    },
                    onLongPressed = {
                        selectedTask.value = task
                        openDeleteTaskDialog.value = true
                    })
            }
        })
}