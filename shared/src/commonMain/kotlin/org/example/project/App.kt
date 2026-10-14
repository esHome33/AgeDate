package org.example.project


import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.datetime.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    // Date states (represented as timestamps in Milliseconds)
    var startDateMillis by remember { mutableStateOf<Long?>(null) }
    var endDateMillis by remember { mutableStateOf<Long?>(null) }
    var useMonthOffset by remember { mutableStateOf(true) }
    var monthsBeforeDate2Text by remember { mutableStateOf("2") }

    // Dialog visibility states
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val selectableYearRange = 1000..2100
    val startPickerState = rememberDatePickerState(yearRange = selectableYearRange)
    val endPickerState = rememberDatePickerState(yearRange = selectableYearRange)

    val copyTextToClipboard = rememberCopyTextToClipboard()

    var showCopiedHint by remember { mutableStateOf(false) }
    LaunchedEffect(showCopiedHint) {
        if (showCopiedHint) {
            delay(1500.milliseconds)
            showCopiedHint = false
        }
    }


    // Automatically recalculates period whenever start or end date modifications happen
    val monthsBeforeDate2 = monthsBeforeDate2Text.toIntOrNull() ?: 0

    val calculatedDifference = remember(startDateMillis, endDateMillis, useMonthOffset, monthsBeforeDate2) {
        calculateDifference(startDateMillis, endDateMillis, useMonthOffset, monthsBeforeDate2)
    }
    val colorScheme = if (!isSystemInDarkTheme()) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }
    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Calcul de temps entre deux dates",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Intercept box clicks to show the picker while keeping text styling native
                Box(modifier = Modifier.fillMaxWidth().clickable { showStartPicker = true }) {
                    OutlinedTextField(
                        value = formatMillisToDateString(startDateMillis),
                        onValueChange = {},
                        label = { Text("Date 1 :") },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Décalage de date 2",
                        color = colorScheme.onPrimary
                    )
                    Switch(
                        checked = useMonthOffset,
                        onCheckedChange = { useMonthOffset = it }
                    )
                }

                if (useMonthOffset) {
                    OutlinedTextField(
                        value = monthsBeforeDate2Text,
                        onValueChange = { newValue ->
                            monthsBeforeDate2Text = newValue.filter { it.isDigit() }
                        },
                        label = { Text("x mois avant date 2") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(modifier = Modifier.fillMaxWidth().clickable { showEndPicker = true }) {
                    OutlinedTextField(
                        value = formatMillisToDateString(endDateMillis),
                        onValueChange = {},
                        label = { Text("Date 2 :") },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Final difference display text field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (copyTextToClipboard(calculatedDifference)) {
                                showCopiedHint = true
                            }
                        }
                ) {
                    OutlinedTextField(
                        value = calculatedDifference,
                        onValueChange = {},
                        label = { Text("Différence : ") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledSupportingTextColor = MaterialTheme.colorScheme.primary

                        ),
                        supportingText = {
                            if (showCopiedHint) {
                                Text("Copié dans le presse-papiers")
                            } else {
                                Text("Cliquez pour copier")
                            }
                        },
                    )
                }
            }
        }

        // Modal UI logic for Start Date Selector
        if (showStartPicker) {
            DatePickerDialog(
                onDismissRequest = { showStartPicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        startDateMillis = startPickerState.selectedDateMillis
                        showStartPicker = false
                    }) { Text("Confirm") }
                },
                dismissButton = {
                    TextButton(onClick = { showStartPicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = startPickerState)
            }
        }

        // Modal UI logic for End Date Selector
        if (showEndPicker) {
            DatePickerDialog(

                onDismissRequest = { showEndPicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        endDateMillis = endPickerState.selectedDateMillis
                        showEndPicker = false
                    }) { Text("Confirm") }
                },
                dismissButton = {
                    TextButton(onClick = { showEndPicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = endPickerState)
            }
        }
    }
}

// Helper function to format timestamp into human-readable string
fun formatMillisToDateString(millis: Long?): String {
    if (millis == null) return ""
    val localDate = Instant.fromEpochMilliseconds(millis)
        .toLocalDateTime(TimeZone.UTC)
        .date
    return "${localDate.day.toString().padStart(2, '0')} " +
            "${localDate.month.number.toString().padStart(2, '0')} " +
            "${localDate.year}"
}

fun calculateDifference(
    startDateMillis: Long?,
    endDateMillis: Long?,
    useMonthOffset: Boolean = false,
    monthsBeforeDate2: Int = 0
): String {
    return if (startDateMillis != null && endDateMillis != null) {
        val startDate = Instant.fromEpochMilliseconds(startDateMillis).toLocalDateTime(TimeZone.UTC).date
        val endDate = Instant.fromEpochMilliseconds(endDateMillis).toLocalDateTime(TimeZone.UTC).date
        val adjustedEndDate = if (useMonthOffset) {
            endDate.minus(DatePeriod(months = monthsBeforeDate2))
        } else {
            endDate
        }

        // Prevent negative calculations if selected in reverse order
        val (earlierDate, laterDate) = if (startDate <= adjustedEndDate) {
            startDate to adjustedEndDate
        } else {
            adjustedEndDate to startDate
        }

        // Execute the multiplatform date period math
        val period = earlierDate.periodUntil(laterDate)
        val textAn = when (period.years) {
            0 -> ""
            1 -> "1 an,"
            else -> "${period.years} ans,"
        }
        val textJours = when (period.days) {
            0 -> ""
            1 -> ", 1 jour"
            else -> ", ${period.days} jours"
        }
        val textMois = when (period.months) {
            0 -> "0 mois"
            else -> "${period.months} mois"
        }

        "$textAn $textMois $textJours"
    } else {
        "Les deux dates doivent être précisées pour calculer la durée."
    }
}
