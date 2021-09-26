package com.pelicanregimen.ui.regimen

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pelicanregimen.ui.theme.PelicanRegimenTheme

private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun RegimenScreen() {
    val scroll = rememberScrollState(0)
    Column(
        modifier = Modifier.verticalScroll(scroll)
    ) {
        Box(Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            val scroll = rememberScrollState(0)
            var text by remember { mutableStateOf("Daily Vitamins") }

            var timePicked : String? by remember {
                mutableStateOf(null)
            }

            val updatedTime = { hour: Int, minute: Int ->
                timePicked = "$hour : $minute"
            }
            Column {
                Spacer(Modifier.padding(top = 65.dp))
                Text(
                    text = "New Regimen",
                    style = MaterialTheme.typography.body1,
                    color = PelicanRegimenTheme.colors.brand
                )
                Spacer(Modifier.padding(top = 20.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Name") }
                )
                Spacer(Modifier.padding(top = 20.dp))
                RegimenTimePicker(timePicked, updatedTime)
                Spacer(Modifier.padding(top = 20.dp))
                RegimenInterval()
            }
        }
    }
}

@Composable
fun RegimenTimePicker(
    timePicked : String?,
    updatedTime : ( hour: Int, minute: Int ) -> Unit,
) {
    val datePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _: TimePicker, hour: Int, minute: Int ->
            updatedTime(hour, minute)
        }, 12, 0, false
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
            .padding(top = 10.dp)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable{
                datePickerDialog.show()
            }
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {



            val (lable, iconView) = createRefs()

            Text(
                text= timePicked?:"Date Picker",
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lable) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(iconView.start)
                        width = Dimension.fillToConstraints
                    }
            )

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp, 20.dp)
                    .constrainAs(iconView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                tint = MaterialTheme.colors.onSurface
            )

        }

    }
}

@Composable
fun RegimenInterval() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Every Hour", "Every 2 Hours", "Every 6 Hours", "Every 12 Hours", "Daily")
    var selectedIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
        Text(items[selectedIndex],modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true }).background(
            Color.White))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth().background(
                Color.White)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                }) {
                    Text(text = s)
                }
            }
        }
    }
}
