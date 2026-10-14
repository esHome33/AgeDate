package org.example.project

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals

class SharedCommonTest {

    private fun dateToMillis(year: Int, month: Int, day: Int): Long {
        return LocalDate(year, month, day).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    }

    @Test
    fun testFormatMillisToDateStringPre1900() {
        val millis1850 = dateToMillis(1850, 6, 15)
        assertEquals("15 06 1850", formatMillisToDateString(millis1850))

        val millis1800 = dateToMillis(1800, 1, 1)
        assertEquals("01 01 1800", formatMillisToDateString(millis1800))

        val millis1888Leap = dateToMillis(1888, 2, 29)
        assertEquals("29 02 1888", formatMillisToDateString(millis1888Leap))

        val millis1000 = dateToMillis(1000, 12, 31)
        assertEquals("31 12 1000", formatMillisToDateString(millis1000))

        assertEquals("", formatMillisToDateString(null))
    }

    @Test
    fun testCalculateDifferencePre1900ToModernDate() {
        val startMillis = dateToMillis(1850, 6, 15)
        val endMillis = dateToMillis(2024, 6, 15)

        val result = calculateDifference(startMillis, endMillis, useMonthOffset = false)
        assertEquals("174 ans, 0 mois ", result)
    }

    @Test
    fun testCalculateDifferenceBothPre1900() {
        val startMillis = dateToMillis(1820, 1, 1)
        val endMillis = dateToMillis(1880, 5, 20)

        val result = calculateDifference(startMillis, endMillis, useMonthOffset = false)
        assertEquals("60 ans, 4 mois , 19 jours", result)
    }

    @Test
    fun testCalculateDifferenceReversedDatesPre1900() {
        val startMillis = dateToMillis(1880, 5, 20)
        val endMillis = dateToMillis(1820, 1, 1)

        val result = calculateDifference(startMillis, endMillis, useMonthOffset = false)
        assertEquals("60 ans, 4 mois , 19 jours", result)
    }

    @Test
    fun testCalculateDifferenceWithMonthOffsetPre1900() {
        val startMillis = dateToMillis(1820, 1, 1)
        val endMillis = dateToMillis(1880, 5, 20)

        // 2 months offset before 1880-05-20 gives 1880-03-20
        val result = calculateDifference(startMillis, endMillis, useMonthOffset = true, monthsBeforeDate2 = 2)
        assertEquals("60 ans, 2 mois , 19 jours", result)
    }

    @Test
    fun testCalculateDifferenceNullDates() {
        val result = calculateDifference(null, dateToMillis(1850, 1, 1))
        assertEquals("Les deux dates doivent être précisées pour calculer la durée.", result)
    }
}