# Enhanced Student Grade Tracker

A modern, professional-grade student grade management system built with Java Swing.

## Features

- **Student Management**: Add, update, and delete student records
- **Grade Tracking**: Automatic letter grade calculation (A+ to F)
- **Data Persistence**: Save/load data automatically
- **Search & Filter**: Real-time student search functionality
- **Export Options**: CSV export with timestamps
- **Statistics Dashboard**: Live grade analytics and progress tracking
- **Professional UI**: Modern color-coded interface

## Requirements

- Java 8 or higher
- No external dependencies required

## Installation

1. Download the source code
2. Compile: `javac EnhancedStudentGradeTracker.java`
3. Run: `java EnhancedStudentGradeTracker`

## Usage

### Adding Students
- Enter student name and grade (0-100)
- Press Enter or click "Add Student"
- Duplicate name detection included

### Managing Records
- **Search**: Type in search box to filter students
- **Update**: Select row and click "Update Selected"
- **Delete**: Select row and click "Delete Selected"
- **Sort**: Click column headers to sort data

### Data Management
- **Auto-save**: Prompted on exit
- **Export**: CSV format with timestamp
- **Sample Data**: Generate test data via Tools menu

## Grade System

| Score Range | Letter Grade | Status |
|-------------|--------------|--------|
| 97-100      | A+          | Pass   |
| 93-96       | A           | Pass   |
| 90-92       | A-          | Pass   |
| 87-89       | B+          | Pass   |
| 83-86       | B           | Pass   |
| 80-82       | B-          | Pass   |
| 77-79       | C+          | Pass   |
| 73-76       | C           | Pass   |
| 70-72       | C-          | Pass   |
| 67-69       | D+          | Pass   |
| 60-66       | D           | Pass   |
| Below 60    | F           | Fail   |

## Screenshots
### Main Application Interface
<img width="800" alt="Student Grade Tracker Main Interface" src="https://github.com/user-attachments/assets/7e8effd7-c19d-4c53-bc86-4d850d4cd91d">


The application features:
- Clean, modern interface with color-coded statistics
- Interactive progress bars showing class performance
- Professional table with sorting capabilities
- Comprehensive menu system for advanced operations
