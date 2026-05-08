package biz.fabiotecnico1.sendAlertDialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class SchedulingData(
    val name: String = "",
    val isConfirmed: Boolean = false,
    val date: String = "",
    val time: String = "",
    val observation: String = ""
)

data class SchedulingUiState(
    val schedulingData: SchedulingData = SchedulingData(),
    val showConfirmationDialog: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    val month: Int = Calendar.getInstance().get(Calendar.MONTH),
    val day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    val hour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    val minute: Int = Calendar.getInstance().get(Calendar.MINUTE)
)

class SchedulingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SchedulingUiState())
    val uiState: StateFlow<SchedulingUiState> = _uiState.asStateFlow()

    // The states bellow survive rotation
    var nameTemp by mutableStateOf("")
        private set

    var isConfirmedTemp by mutableStateOf(false)
        private set

    var observationTemp by mutableStateOf("")
        private set

    fun updateNameTemp(name: String) {
        nameTemp = name
    }

    fun updateConfirmedTemp(confirmed: Boolean) {
        isConfirmedTemp = confirmed
    }

    fun updateObservationTemp(observation: String) {
        observationTemp = observation
    }

    fun updateConfirmationDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showConfirmationDialog = show)
    }

    fun updateDatePicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDatePicker = show)
    }

    fun updateTimePicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(showTimePicker = show)
    }

    fun updateDate(year: Int, month: Int, day: Int) {
        val dateFormated = String.format("%02d/%02d/%d", day, month + 1, year)
        val currentDate = _uiState.value.schedulingData
        _uiState.value = _uiState.value.copy(
            schedulingData = currentDate.copy(date = dateFormated),
            year = year,
            month = month,
            day = day
        )
    }

    fun updateTime(hour: Int, minute: Int) {
        val timeFormated = String.format("%02d:%02d", hour, minute)
        val currentTime = _uiState.value.schedulingData
        _uiState.value = _uiState.value.copy(
            schedulingData = currentTime.copy(time = timeFormated),
            hour = hour,
            minute = minute
        )
    }

    fun confirmDialogData() {
        val currentData = _uiState.value.schedulingData
        _uiState.value = _uiState.value.copy(
            schedulingData = currentData.copy(
                name = nameTemp,
                isConfirmed = isConfirmedTemp,
                observation = observationTemp
            ),
            showConfirmationDialog = false
        )

        clearTempData()     // Clear the temporary fields
    }

    fun clearAllData() {
        _uiState.value = SchedulingUiState()
        clearTempData()
    }

    private fun clearTempData() {
        nameTemp = ""
        isConfirmedTemp = false
        observationTemp = ""
    }
}