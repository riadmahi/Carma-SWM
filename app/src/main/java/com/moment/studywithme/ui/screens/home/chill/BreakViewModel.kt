package com.moment.studywithme.ui.screens.home.chill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moment.studywithme.SWMApplication
import com.moment.studywithme.domain.dataservice.storage.StorageDataService
import com.moment.studywithme.domain.model.UserPreferences
import com.moment.studywithme.ui.component.SWMTimer
import com.moment.studywithme.ui.screens.home.pomodoro.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class BreakViewModel(private val storageDataService: StorageDataService) : ViewModel() {

    private var _uiState =
        MutableStateFlow(BreakUiState("5:00", TimerState.START, UserPreferences()))

    val uiState: StateFlow<BreakUiState> = _uiState
    private var InitalTimerInMs = (5 * 60000).toLong()
    private var currentTimerInMs = 0L
    private var currentTimer: String = "5:00"
    private var timerState = TimerState.START
    private var timer = SWMTimer(InitalTimerInMs, 1000)
    private var defaultCoroutineScope = CoroutineScope(Dispatchers.IO)
    private var userPreferences = UserPreferences()

    init {
        defaultCoroutineScope.launch {
            userPreferences = storageDataService.getUserPreferences()
            currentTimer = convertMsInMMSS(userPreferences.defaultBreakInMs)
            InitalTimerInMs = userPreferences.defaultBreakInMs
            viewModelScope.launch {
                timer = SWMTimer(InitalTimerInMs, 1000)
                initTimer()
            }
            updateUiState()
        }

    }

    private fun initTimer() {
        timer.onTick = { millisUntilFinished ->
            currentTimerInMs = millisUntilFinished
            currentTimer = convertMsInMMSS(millisUntilFinished)
            updateUiState()
        }
    }


    fun updateUserPreferencesTimer(newTimer: Long){
        userPreferences.defaultBreakInMs = newTimer
        InitalTimerInMs = newTimer
        currentTimerInMs = 0L
        currentTimer = convertMsInMMSS(userPreferences.defaultBreakInMs)
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

    private fun updateUiState() {
        viewModelScope.launch {
            _uiState.emit(BreakUiState(currentTimer, timerState, userPreferences))
        }
    }

    fun resetTimer() {
        timer.pause()
        timer.restart()
        timer.pause()
        currentTimerInMs = InitalTimerInMs
        currentTimer = convertMsInMMSS(currentTimerInMs)
        updateUiState()
    }

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
                    return BreakViewModel(
                        application.getSWMAppInjector().storageDataService,
                    ) as T
                }
            }
        }
    }
}