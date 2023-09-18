package com.moment.studywithme.ui.screens.home

import android.os.Build
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.moment.studywithme.R
import com.moment.studywithme.ui.component.SWMButton

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerPickerBottomSheetDialog(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    defaultTimer: Long,
    onTimerChanged: (timer: Long) -> Unit
) {
    val timer = remember { mutableStateOf((defaultTimer / 60000).toInt()) }

    ModalBottomSheet(
        shape = RoundedCornerShape(12.dp),
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Default timer", style = MaterialTheme.typography.labelMedium)
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onDismiss() },
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                AndroidView(
                    factory = { context ->
                        NumberPicker(context).apply {
                            setOnValueChangedListener { numberPicker, i, i2 ->
                                timer.value = numberPicker.value
                            }
                            minValue = 5
                            maxValue = 60
                            setTextColor(ContextCompat.getColor(context, R.color.white))
                            textSize = 64f
                            selectionDividerHeight = 0
                        }
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "min", style = MaterialTheme.typography.titleLarge)

            }
            Spacer(modifier = Modifier.height(12.dp))
            SWMButton(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(50.dp),
                label = "Save timer",
                onClick = {
                   onTimerChanged((timer.value * 60000).toLong())
                })
            Spacer(modifier = Modifier.height(12.dp))

        }


    }
}