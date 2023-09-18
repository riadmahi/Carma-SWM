package com.moment.studywithme.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.moment.studywithme.ui.component.SWMTab
import com.moment.studywithme.ui.screens.home.chill.BreakScreen
import com.moment.studywithme.ui.screens.home.chill.BreakViewModel
import com.moment.studywithme.ui.screens.home.pomodoro.PomodoroScreen
import com.moment.studywithme.ui.screens.home.pomodoro.PomodoroViewModel

@Composable
fun HomeScreen(
    pomodoroViewModel: PomodoroViewModel,
    breakViewModel: BreakViewModel
) {
    val tabs = listOf("Pomodoro", "Break")
    var tabIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(
                modifier = Modifier
                    .padding(vertical = 18.dp, horizontal = 12.dp)
                    .clip(
                        shape = RoundedCornerShape(6.dp)
                    ),
                selectedTabIndex = tabIndex,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                indicator = { },
                divider = { },
            ) {
                tabs.forEachIndexed { index, title ->
                    SWMTab(label = title,
                        isSelected = tabIndex == index,
                        onSelected = {
                            tabIndex = index
                        })
                }
            }
            when (tabIndex) {
                0 -> PomodoroScreen(viewModel = pomodoroViewModel)
                1 -> BreakScreen(viewModel = breakViewModel)
            }
        }
    }
}