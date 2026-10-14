---
sessionId: session-261014-154912-v918
---

# Requirements

### Overview & Goals
Allow users to select and calculate time differences for dates before January 1, 1900 in the Compose Multiplatform DatePicker dialogs.

### Scope
- **In Scope**:
  - Configure `rememberDatePickerState` in `App.kt` with an expanded `yearRange` (e.g., `1800..2100` or `1000..2100`) to support pre-1900 dates.
  - Verify `kotlinx.datetime` date calculations, formatting, and month offsets with pre-1900 dates.
  - Add unit tests validating difference calculation with pre-1900 dates.
- **Out of Scope**:
  - Changing the overall UI layout or color schemes.
  - Modifying non-date input components.

### Functional Requirements
- Users can browse and select years earlier than 1900 in both Date 1 (Start Date) and Date 2 (End Date) pickers.
- Date string formatting (`formatMillisToDateString`) correctly formats timestamps for pre-1900 dates.
- Time difference calculation properly computes the years, months, and days between dates even when one or both dates precede 1900.

# Technical Design

### Current Implementation
In `shared/src/commonMain/kotlin/org/example/project/App.kt`:
- `startPickerState` and `endPickerState` are instantiated via `rememberDatePickerState()` without parameters.
- By default, Material3 `DatePickerDefaults.YearRange` restricts selectable years to `1900..2100`.
- Dates prior to 1/1/1900 cannot be selected or navigated to in the `DatePicker` UI.

### Key Decisions
- **Custom `yearRange` parameter**: Pass an explicit `yearRange: IntRange` (such as `1800..2100` or `1000..2100`) to `rememberDatePickerState(yearRange = ...)` to unlock selection for earlier historical dates.
- **kotlinx.datetime compatibility**: `kotlinx.datetime.Instant` and `LocalDate` fully support negative epoch milliseconds and pre-1900 dates, so existing date math (`periodUntil`, `minus`) remains compatible without structural changes.

### Proposed Changes
- In `shared/src/commonMain/kotlin/org/example/project/App.kt`:
  - Define a suitable year range constant (e.g. `val selectableYearRange = 1800..2100` or `1000..2100`).
  - Pass `yearRange = selectableYearRange` to `rememberDatePickerState(...)` for both `startPickerState` and `endPickerState`.

### File Structure
- `shared/src/commonMain/kotlin/org/example/project/App.kt` (modified): Update date picker states with expanded `yearRange`.
- `shared/src/commonTest/kotlin/org/example/project/SharedCommonTest.kt` (modified): Add test cases for pre-1900 date calculations and formatting.

# Testing

### Validation Approach
Verify that dates before 1900 can be represented in epoch milliseconds, formatted accurately, and calculated with period differences.

### Key Scenarios
- **Pre-1900 Start and End Dates**: Selecting a date in the 1800s (e.g., 1850-06-15) and calculating the difference to a modern date (e.g., 2024-06-15).
- **Both Dates Pre-1900**: Selecting two dates in the 1800s (e.g., 1820-01-01 and 1880-05-20) and verifying accurate year/month/day difference output.
- **Month Offset with Pre-1900 Dates**: Applying the month offset feature on pre-1900 dates to ensure `minus(DatePeriod(months = ...))` calculates correctly.

### Test Changes
- Add unit tests in `SharedCommonTest.kt` testing date math with pre-1900 dates and formatting.

# Delivery Steps

### ✓ Step 1: Update DatePicker state year ranges
`startPickerState` and `endPickerState` in `App.kt` accept dates before 1/1/1900.

- Update `rememberDatePickerState` invocations for `startPickerState` and `endPickerState` to pass an expanded `yearRange` (e.g. `1800..2100` or `1000..2100`).
- Ensure `DatePicker` and `DatePickerDialog` components in `App.kt` correctly bind the updated states and permit date selection across the full range.

### ✓ Step 2: Validate pre-1900 date calculations and add unit tests
Date calculations and formatting for pre-1900 dates are verified and covered with automated tests.

- Verify that `formatMillisToDateString` and period calculations in `App.kt` correctly handle timestamps corresponding to years prior to 1900.
- Add unit tests in `SharedCommonTest.kt` covering period difference calculations and formatting for dates before 1/1/1900 (including leap years and month offsets).