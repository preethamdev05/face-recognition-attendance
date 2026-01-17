# Migration Guide: Project Restructure

## What Changed?

The repository has been restructured from a **flattened layout** to **standard Android project structure**:

### Before (Incorrect Structure)
```
root/
├── build.gradle.kts           # Module build file at root (WRONG)
├── AndroidManifest.xml        # Manifest at root (WRONG)
├── src/main/kotlin/...        # Source files at root (WRONG)
├── proguard-rules.pro         # ProGuard at root (WRONG)
└── settings.gradle.kts        # Referenced non-existent :app module
```

### After (Standard Android Structure)
```
root/
├── build.gradle.kts           # Root build file (plugins only)
├── settings.gradle.kts        # Includes :app module
├── app/
│   ├── build.gradle.kts       # App module build file
│   ├── proguard-rules.pro     # App-specific ProGuard rules
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── kotlin/...
│   │   │   └── res/...
│   │   └── test/...
│   └── .gitignore
└── local.properties
```

## Why This Change Was Necessary

### Critical Issues Fixed

1. **Gradle Sync Failure**
   - **Problem**: `settings.gradle.kts` declared `include(":app")` but no `app/` directory existed
   - **Impact**: Project could not sync in Android Studio
   - **Solution**: Created proper `app/` module structure

2. **Manifest Location**
   - **Problem**: `AndroidManifest.xml` at repository root
   - **Impact**: Android Gradle Plugin expects manifest at `app/src/main/AndroidManifest.xml`
   - **Solution**: Moved manifest to correct location

3. **Non-Standard Structure**
   - **Problem**: Source files in `src/` at root level
   - **Impact**: Breaks tooling (IDE, Gradle, CI/CD)
   - **Solution**: Follow Android standard: `app/src/main/kotlin/`

## Migration Steps for Existing Clones

If you have an existing local clone, follow these steps:

### Option 1: Fresh Clone (Recommended)

```bash
# Backup any local changes
cd /path/to/old/clone
git stash

# Clone fresh copy
cd /path/to/workspace
git clone https://github.com/preethamdev05/face-recognition-attendance.git face-recognition-new
cd face-recognition-new

# Restore your local.properties
cp ../old/clone/local.properties .
cp ../old/clone/app/google-services.json app/

# Apply any stashed changes if needed
# (manually port changes to new structure)
```

### Option 2: Update Existing Clone

```bash
cd /path/to/existing/clone

# Backup local configuration
cp local.properties ~/backup-local.properties
if [ -f google-services.json ]; then
  cp google-services.json ~/backup-google-services.json
fi

# Reset to new structure
git fetch origin
git reset --hard origin/main

# Restore configurations
cp ~/backup-local.properties local.properties
if [ -f ~/backup-google-services.json ]; then
  cp ~/backup-google-services.json app/google-services.json
fi

# Invalidate caches in Android Studio
# File -> Invalidate Caches / Restart -> Invalidate and Restart
```

## Post-Migration Verification

### 1. Verify Structure

```bash
# Check that app module exists
ls -la app/build.gradle.kts
ls -la app/src/main/AndroidManifest.xml

# Check root build file
cat build.gradle.kts | grep "apply false"
```

Expected output:
- `app/build.gradle.kts` exists
- `app/src/main/AndroidManifest.xml` exists
- Root `build.gradle.kts` contains only plugin declarations with `apply false`

### 2. Gradle Sync

Open Android Studio:
1. **File** → **Sync Project with Gradle Files**
2. Wait for sync to complete (should succeed)
3. Verify **Build Variants** shows `app` module

### 3. Build Verification

```bash
# Clean build
./gradlew clean

# Assemble debug (should succeed)
./gradlew assembleDebug

# Run static analysis
./gradlew ktlintCheck detekt

# Run tests
./gradlew test
```

All commands should complete successfully.

## Updated File Locations

### Configuration Files

| File | Old Location | New Location |
|------|-------------|-------------|
| `google-services.json` | Root or unspecified | `app/google-services.json` |
| `local.properties` | Root ✓ (unchanged) | Root |
| `proguard-rules.pro` | Root | `app/proguard-rules.pro` |
| Module build file | `build.gradle.kts` | `app/build.gradle.kts` |
| AndroidManifest | `AndroidManifest.xml` or `src/main/` | `app/src/main/AndroidManifest.xml` |

### Source Files

| File Type | Old Path | New Path |
|-----------|----------|----------|
| Kotlin source | `src/main/kotlin/...` | `app/src/main/kotlin/...` |
| Resources | `src/main/res/...` | `app/src/main/res/...` |
| Assets | `src/main/assets/...` | `app/src/main/assets/...` |
| Tests | `src/test/kotlin/...` | `app/src/test/kotlin/...` |

## Common Issues After Migration

### Issue 1: "Could not find method implementation()"

**Cause**: Android Studio is using the wrong build file

**Solution**:
```bash
# Invalidate caches
# In Android Studio: File -> Invalidate Caches / Restart
```

### Issue 2: "Module not specified"

**Cause**: Run configuration pointing to old structure

**Solution**:
1. **Run** → **Edit Configurations**
2. Delete old configurations
3. Click **+** → **Android App**
4. Select `app` module

### Issue 3: Resources not found

**Cause**: Resources still in old `src/main/res/` location

**Solution**:
```bash
# Verify resources are in app module
ls -la app/src/main/res/

# If missing, pull latest changes
git pull origin main
```

### Issue 4: "google-services.json missing"

**Cause**: File in wrong location or not git-ignored properly

**Solution**:
```bash
# Ensure it's in app/ directory
mv google-services.json app/

# Verify it's git-ignored
cat app/.gitignore | grep google-services.json
```

## CI/CD Updates

If you have custom CI/CD pipelines:

### GitHub Actions

A new workflow file is provided at `.github/workflows/ci.yml`

### Gradle Commands

All Gradle commands remain the same:
```bash
./gradlew assembleDebug    # Still works
./gradlew test             # Still works
./gradlew ktlintCheck      # Still works
```

## Breaking Changes

None for end users. This is purely a structural change for developers.

## Need Help?

If migration fails:

1. **Check Structure**: Run `tree -L 3` to verify directory layout
2. **Clean Build**: `./gradlew clean build`
3. **Re-clone**: Fresh clone is often faster than troubleshooting
4. **Open Issue**: [GitHub Issues](https://github.com/preethamdev05/face-recognition-attendance/issues) with error logs

## Timeline

- **Jan 17, 2026**: Structure migration committed
- **Backwards Compatibility**: None (one-time breaking change)
- **Support**: Old structure is no longer supported

---

**This migration ensures the project follows Android best practices and works correctly with all tooling.**
