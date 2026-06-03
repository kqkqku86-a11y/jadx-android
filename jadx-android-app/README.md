# JADX Android App

Native Android application for decompiling Android packages to Java source code.

## Features

- **File Selection**: Browse and select APK, DEX, AAB, XAPK, and APKM files
- **Decompilation**: Convert Android bytecode to readable Java source
- **Code Viewer**: View and navigate decompiled code with syntax highlighting
- **Export**: Save decompiled code and resources to device storage
- **Background Processing**: Decompile large files without blocking UI

## Requirements

- Android 8.0 (API 26) or higher
- Minimum 2GB RAM for large APK decompilation
- Java 11 or higher (for building)

## Building

```bash
./gradlew :jadx-android-app:build
```

## Installation

```bash
./gradlew :jadx-android-app:installDebug
```

## Architecture

### Modules Used

- **jadx-core**: Core decompilation engine
- **jadx-plugins**: Input and processing plugins
  - jadx-dex-input: DEX file handling
  - jadx-java-input: Java class handling
  - jadx-smali-input: Smali disassembly
  - jadx-aab-input: Android App Bundle support

### Layers

- **UI Layer**: Jetpack Compose for modern Android UI
- **Service Layer**: Background decompilation service
- **Data Layer**: File management and caching

## Permissions

- `READ_EXTERNAL_STORAGE`: Read APK files from device
- `WRITE_EXTERNAL_STORAGE`: Save decompiled code
- `INTERNET`: Optional, for future features

## Known Limitations

- Large APK files (>100MB) may take several minutes to decompile
- Some obfuscated code may not decompile perfectly
- Limited code navigation in this initial version
- Smali debugging not available in this version

## Future Enhancements

- Code search and navigation
- Smali debugger integration
- Method tracing and profiling
- Deobfuscation support
- Plugin system for extensibility
