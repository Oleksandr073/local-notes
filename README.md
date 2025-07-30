# LocalNotes

A modern Android note-taking app built with Jetpack Compose and Material 3.

## Features

- **Create and Edit Notes**: Write notes with titles, content, and folder organization
- **Search Functionality**: Search through notes by title, content, or folder
- **Folder Organization**: Organize notes into folders for better categorization
- **Persistent Storage**: Notes are saved locally using DataStore
- **Modern UI**: Beautiful Material 3 design with smooth animations
- **Error Handling**: Proper error handling with user-friendly messages
- **Loading States**: Visual feedback during data operations

## Architecture

The app follows MVVM (Model-View-ViewModel) architecture:

- **Model**: `Note` data class representing note entities
- **View**: Compose UI components (`NoteListScreen`, `NoteEditorScreen`)
- **ViewModel**: `NotesViewModel` handling business logic and state management
- **Storage**: `NoteStorage` class using DataStore for persistence

## Key Improvements Made

### 1. **Separation of Concerns**
- Moved business logic from `MainActivity` to `NotesViewModel`
- Created proper state management with `NotesUiState`
- Separated UI components into reusable composables

### 2. **Enhanced User Experience**
- Added search functionality with real-time filtering
- Implemented proper loading states and error handling
- Added validation for note creation/editing
- Improved visual design with Material 3 components

### 3. **Better Data Management**
- Integrated `NoteStorage` for persistent data
- Added proper error handling for storage operations
- Implemented efficient note filtering and search

### 4. **Improved UI Components**
- Enhanced `NoteListScreen` with search, delete, and better card design
- Improved `NoteEditorScreen` with validation and better UX
- Added error snackbars for user feedback
- Implemented proper focus management

### 5. **Code Quality**
- Better readability with clear component separation
- Proper state management using StateFlow
- Consistent error handling throughout the app
- Scalable architecture for future features

## Future Enhancements

- Rich text formatting
- Note categories and tags
- Export/import functionality
- Cloud sync capabilities
- Dark/light theme toggle
- Note sharing features

## Technical Stack

- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM with ViewModel
- **Storage**: DataStore with Gson serialization
- **State Management**: Kotlin Flow and StateFlow
- **Language**: Kotlin
- **Minimum SDK**: API 34 (Android 14) 