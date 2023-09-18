package com.moment.studywithme.ui.screens.home.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moment.studywithme.domain.model.Task
import com.moment.studywithme.ui.component.SWMCheckbox
import com.moment.studywithme.ui.theme.BRSonomaFont


@Composable
fun TaskCard(
    task: Task,
    onCheckChanged: () -> Unit,
    onLongPressed: () -> Unit
) {
    val cardColor =
        if (task.isDone!!) Color(0XFF1F2E2F) else MaterialTheme.colorScheme.secondaryContainer
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = cardColor,
                shape = RoundedCornerShape(10.dp)
            )
            .pointerInput(Unit){
                detectTapGestures(
                    onLongPress = {
                        onLongPressed()
                    }
                )
            },
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SWMCheckbox(isChecked = task.isDone!!) {
                onCheckChanged()
            }
            Text(
                modifier = Modifier.fillMaxSize(),
                text = task.name!!,
                style = if (task.isDone!!) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(
                    textDecoration = TextDecoration.None
                ),
                fontFamily = BRSonomaFont,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}
