package com.moment.studywithme.ui.screens.home.pomodoro

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moment.studywithme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroBottomSheetDialog(
    sheetState: SheetState,
    defaultTimer: String,
    onDismiss: () -> Unit,
    onTimerChanged: () -> Unit,
    onTimerReset: () -> Unit,
    onRoundsReset: () -> Unit,
    onDeleteAllTasks: () -> Unit,
) {

    ModalBottomSheet(
        shape = RoundedCornerShape(12.dp),
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsHeader { onDismiss() }
            TimerCard(defaultTimer) {
                onTimerChanged()
            }
            Spacer(modifier = Modifier.size(8.dp))
            SettingCard(
                text = "Reset timer",
                icon = R.drawable.ic_refresh,
                onClicked = { onTimerReset() })
            Spacer(modifier = Modifier.size(8.dp))
            SettingCard(
                text = "Reset rounds",
                icon = R.drawable.ic_rotate_left,
                onClicked = { onRoundsReset() })
            Spacer(modifier = Modifier.size(8.dp))
            SettingCard(
                text = "Delete all tasks",
                icon = R.drawable.ic_trash,
                onClicked = { onDeleteAllTasks() })
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = "Version 1.0.0-alpha01",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}


@Composable
fun SettingCard(
    text: String,
    @DrawableRes icon: Int,
    onClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onClicked()
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, style = MaterialTheme.typography.labelMedium)
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun TimerCard(defaultTimer: String, onTimerChangedSelected: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onTimerChangedSelected()
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Default timer", style = MaterialTheme.typography.labelMedium)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = defaultTimer,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsHeader(onDismiss: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Settings", style = MaterialTheme.typography.labelMedium)
        },
        actions = {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onDismiss() },
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}