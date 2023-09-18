package com.moment.studywithme.ui.screens.home.pomodoro

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moment.studywithme.SWMApplication
import com.moment.studywithme.domain.dataservice.storage.StorageDataService
import com.moment.studywithme.domain.model.Task
import com.moment.studywithme.domain.model.UserPreferences
import com.moment.studywithme.ui.component.SWMTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.UUID


class PomodoroViewModel(private val storageDataService: StorageDataService) : ViewModel() {

    private var _uiState =
        MutableStateFlow(
            PomodoroUiState(
                timer = "25:00",
                timerState = TimerState.START,
                progress = 0F,
                tasks = mutableListOf(),
                userPreferences = UserPreferences()
            )
        )

    val uiState: StateFlow<PomodoroUiState> = _uiState

    private var InitalTimerInMs = 0L
    private var currentTimerInMs = 0L
    private var currentTimer: String = ""
    private var progress = 0F
    private var timer = SWMTimer(InitalTimerInMs, 1000)
    private var tasks = mutableListOf<Task>()
    private var timerState = TimerState.START
    private var defaultCoroutineScope = CoroutineScope(Dispatchers.IO)
    private var userPreferences = UserPreferences()

    init {
        defaultCoroutineScope.launch {
            userPreferences = storageDataService.getUserPreferences()
            currentTimer = convertMsInMMSS(userPreferences.defaultTimerInMs)
            InitalTimerInMs = userPreferences.defaultTimerInMs
            viewModelScope.launch {
                timer = SWMTimer(InitalTimerInMs, 1000)
                initTimer()
            }
            updateUiState()
        }
        getAllTasks()

    }

    private fun initTimer() {
        timer.onTick = { millisUntilFinished ->
            currentTimerInMs = millisUntilFinished
            currentTimer = convertMsInMMSS(millisUntilFinished)
            progress = 1 - (currentTimerInMs.toFloat() / InitalTimerInMs)
            updateUiState()
        }

        timer.onFinish = {
            addNewRounds()
            resetTimer()
        }
    }

    private fun addNewRounds() {
        userPreferences.nbRounds = userPreferences.nbRounds + 1
        updateUserPreferences()
        updateUiState()
    }

    fun updateUserPreferencesTimer(newTimer: Long){
        userPreferences.defaultTimerInMs = newTimer
        InitalTimerInMs = newTimer
        currentTimerInMs = 0L
        currentTimer = convertMsInMMSS(userPreferences.defaultTimerInMs)
        viewModelScope.launch {
            timer = SWMTimer(InitalTimerInMs, 1000)
            initTimer()
        }
        updateUserPreferences()
        updateUiState()
    }

    private fun updateUserPreferences() {
        defaultCoroutineScope.launch {
            storageDataService.updateUserPreferences(userPreferences)
        }
    }

    private fun getAllTasks() {
        defaultCoroutineScope.launch {
            tasks += storageDataService.getAllTasks().toMutableList()
            tasks = sortTasksByIsNotDoneFirst(tasks).toMutableList()
            updateUiState()
        }
    }

    fun addTask(taskName: String) {
        val task = Task(UUID.randomUUID().toString(), taskName, false)
        tasks = sortTasksByIsNotDoneFirst((tasks + task)).toMutableList()
        defaultCoroutineScope.launch { storageDataService.createTask(task) }
        updateUiState()
    }

    fun deleteTask(task: Task) {
        defaultCoroutineScope.launch { storageDataService.deleteTask(task) }
        tasks -= task
        updateUiState()
    }

    fun deleteAllTasks() {
        tasks.map {
            deleteTask(it)
        }
    }

    fun resetRounds() {
        userPreferences.nbRounds = 1
        updateUserPreferences()
        updateUiState()
    }

    fun updateTask(task: Task) {
        defaultCoroutineScope.launch { storageDataService.updateTask(task) }
        tasks.remove(task)
        tasks = sortTasksByIsNotDoneFirst((tasks + task)).toMutableList()
        updateUiState()
    }

    private fun sortTasksByIsNotDoneFirst(tasks: List<Task>): List<Task> {
        return tasks.sortedWith(compareBy { it.isDone })
    }

    fun commandTimer(action: TimerState) {
        when (action) {
            TimerState.START -> start()
            TimerState.PLAY -> play()
            TimerState.PAUSE -> pause()
        }
    }

    private fun play() {
        timerState = TimerState.PLAY
        updateUiState()
        timer.resume()
    }

    private fun pause() {
        timerState = TimerState.PAUSE
        updateUiState()
        timer.pause()
    }

    private fun start() {
        timerState = TimerState.PLAY
        updateUiState()
        timer.start()
    }

    fun resetTimer() {
        timer.pause()
        timer.restart()
        timer.pause()
        progress = 0f
        currentTimerInMs = InitalTimerInMs
        currentTimer = convertMsInMMSS(currentTimerInMs)
        updateUiState()
    }

    private fun updateUiState() {
        viewModelScope.launch {
            _uiState.emit(
                PomodoroUiState(
                    currentTimer,
                    timerState,
                    progress,
                    tasks,
                    userPreferences
                )
            )
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertMsInMMSS(ms: Long): String {
        val dateFormat = SimpleDateFormat("mm:ss")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val timeInHHMM = dateFormat.format(Date(ms))
        return timeInHHMM
    }

    companion object {
        fun provideFactory(
            application: SWMApplication
        ): ViewModelProvider.AndroidViewModelFactory {
            return object : ViewModelProvider.AndroidViewModelFactory(application) {
                @Suppress("unchecked_cast")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PomodoroViewModel(
                        application.getSWMAppInjector().storageDataService,
                    ) as T
                }
            }
        }
    }
}
