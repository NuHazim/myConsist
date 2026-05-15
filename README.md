# MyConsist

A smart productivity Android app that helps you manage your tasks, track deadlines, and build daily habits — all stored locally on your device.

---

## Features

### Todolist Page
- Create and manage task groups
- Add tasks inside each group
- Assign a deadline to any task
- Check or uncheck tasks as done

### Reminder Page
- Automatically displays all tasks that have a deadline
- Sorted by most urgent first
- Only shows pending (not done) tasks

### Habits Page
- Create habits assigned to specific days of the week (e.g. Monday, Wednesday, Friday) or every day
- Check or uncheck habits for the current day
- View a calendar-style history of past completed and missed habits

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Platform | Android |
| Local Database | Room (SQLite) |
| Architecture | MVVM (ViewModel + LiveData + Repository) |
| Async Operations | ExecutorService |

---

## Architecture

The app follows the standard Android MVVM pattern:

```
UI (Activity/Fragment)
        ↕  observe / call
    ViewModel
        ↕
    Repository
        ↕
      DAO
        ↕
  Room Database
```

---

## Database Schema

### `TaskGroup`
Represents a group that contains tasks.

| Column | Type | Description |
|---|---|---|
| groupId | INTEGER (PK) | Auto-generated primary key |
| groupName | TEXT | Name of the group |
| position | INTEGER | Display order set by user |

### `Task`
A single task item inside a group.

| Column | Type | Description |
|---|---|---|
| taskId | INTEGER (PK) | Auto-generated primary key |
| groupId | INTEGER (FK) | References TaskGroup |
| title | TEXT | Task description |
| isDone | INTEGER | 0 = pending, 1 = done |
| deadlineMs | LONG | Unix timestamp in milliseconds, nullable |
| position | INTEGER | Display order within the group |

### `Habit`
A recurring habit set by the user.

| Column | Type | Description |
|---|---|---|
| habitId | INTEGER (PK) | Auto-generated primary key |
| title | TEXT | Habit name |
| daysOfWeek | TEXT | Comma-separated days e.g. `"MON,WED,FRI"` or `"EVERYDAY"` |
| position | INTEGER | Display order set by user |

### `HabitLog`
Records whether a habit was completed on a specific date.

| Column | Type | Description |
|---|---|---|
| habitLogId | INTEGER (PK) | Auto-generated primary key |
| habitId | INTEGER (FK) | References Habit |
| logDate | TEXT | Date in `YYYY-MM-DD` format |
| isDone | INTEGER | 0 = missed, 1 = completed |

### Relationships
- `TaskGroup` → `Task` : one-to-many (one group has many tasks)
- `Habit` → `HabitLog` : one-to-many (one habit has many daily log entries)

---
## Key Design Decisions

**Why `deadlineMs` is stored as `Long`**
SQLite has no native date type. Storing deadlines as Unix timestamps (milliseconds since epoch) allows for accurate sorting, comparison, and direct compatibility with Android's `DatePicker` and `TimePicker` which return milliseconds natively.

**Why `logDate` is stored as `TEXT` in `YYYY-MM-DD` format**
Habit logs are always looked up by exact date (e.g. `WHERE logDate = '2026-05-05'`), never by range math. The `YYYY-MM-DD` format also sorts correctly as plain text.

**Why `daysOfWeek` is stored as a comma-separated `TEXT`**
Keeps the schema simple — no junction table needed. Easy to parse in Java with `String.split(",")` and straightforward to display in the UI.

**Why separate DAOs over a single combined DAO**
Each DAO is responsible for one table, keeping queries organized and files easy to navigate as the project scales.

---

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Build and run on an emulator or physical device (Android 8.0+)
4. No internet connection required — all data is stored locally

---

## Status

> Currently in active development. Settings page planned for a future release.
