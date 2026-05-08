package biz.fabiotecnico1.sendAlertDialog

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulingScreen(
    modifier: Modifier = Modifier,
    viewModel: SchedulingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val schedulingData = uiState.schedulingData

    // Display the dialogue boxes when necessary
    if (uiState.showConfirmationDialog) {
        ConfirmationDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.updateConfirmationDialog(false) },
            onConfirm = {
                viewModel.confirmDialogData()
                Toast.makeText(context, "Data saved successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (uiState.showDatePicker) {
        DatePickerDialogCustom(
            viewModel = viewModel,
            onDismiss = { viewModel.updateDatePicker(false) },
            onDateSelected = { milliseconds ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = milliseconds
                viewModel.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }
        )
    }

    if (uiState.showTimePicker) {
        TimePickerDialogCustom(
            viewModel = viewModel,
            onDismiss = { viewModel.updateTimePicker(false) },
            onTimeSelected = { hour, minute ->
                viewModel.updateTime(hour, minute)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scheduling") },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Button to open
            Button(
                onClick = { viewModel.updateConfirmationDialog(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open scheduling form")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date selection
            Button(
                onClick = { viewModel.updateDatePicker(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Date selection")
            }

            // View selected date
            if (schedulingData.date.isNotEmpty()) {
                Text(
                    text = "Selected date: ${schedulingData.date}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selected time
            Button(
                onClick = { viewModel.updateTimePicker(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select time")
            }

            // View selected time
            if (schedulingData.time.isNotEmpty()) {
                Text(
                    text = "Selected time: ${schedulingData.time}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card showing all appointment details
            if (schedulingData.name.isNotEmpty() || schedulingData.date.isNotEmpty() ||
                schedulingData.time.isNotEmpty() || schedulingData.observation.isNotEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Scheduling data",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (schedulingData.name.isNotEmpty()) {
                            Text("Name: ${schedulingData.name}")
                        }

                        if (schedulingData.date.isNotEmpty()) {
                            Text("Date: ${schedulingData.date}")
                        }

                        if (schedulingData.time.isNotEmpty()) {
                            Text("Hora: ${schedulingData.time}")
                        }

                        Text("Attendance confirmed: ${if (schedulingData.isConfirmed) "Yes" else "No"}")

                        if (schedulingData.observation.isNotEmpty()) {
                            Text("Observation: ${schedulingData.observation}")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Button attendance confirmed
            Button(
                onClick = {
                    if (schedulingData.name.isNotEmpty() &&
                        schedulingData.date.isNotEmpty() &&
                        schedulingData.time.isNotEmpty()) {
                        Toast.makeText(
                            context,
                            "Attendance confirmed to ${schedulingData.name} on ${schedulingData.date} at ${schedulingData.time}",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.clearAllData()
                    } else {
                        Toast.makeText(
                            context,
                            "Please, fill in all required fields!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Attendance confirmed")
            }
        }
    }
}