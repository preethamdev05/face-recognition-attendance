# Post-Migration Verification Checklist

After running `migrate-structure.sh`, verify the following:

## 1. Directory Structure

```bash
tree -L 4 -I 'build|.gradle|.idea'
```

**Expected Output:**
```
.
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   ├── .gitignore
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── kotlin/
│       │   │   └── com/attendance/facerec/...
│       │   └── res/
│       └── test/
├── build.gradle.kts        # Root (plugins only)
├── settings.gradle.kts
├── local.properties
├── detekt.yml
└── pre-commit
```

## 2. Gradle Sync

### Command Line
```bash
./gradlew tasks --all
```

**Should list tasks including:**
- `app:assembleDebug`
- `app:assembleRelease`
- `app:test`
- `app:ktlintCheck`
- `app:detekt`

### Android Studio
1. Open project
2. Wait for "Gradle sync"
3. **Should complete successfully** (no errors in "Build" tab)
4. Module dropdown should show: **app**

## 3. File Existence Checks

```bash
# Critical files
[ -f app/build.gradle.kts ] && echo "✓ app/build.gradle.kts" || echo "✗ MISSING"
[ -f app/src/main/AndroidManifest.xml ] && echo "✓ AndroidManifest" || echo "✗ MISSING"
[ -f app/proguard-rules.pro ] && echo "✓ ProGuard rules" || echo "✗ MISSING"

# Old files should NOT exist
[ ! -f AndroidManifest.xml ] && echo "✓ Old manifest removed" || echo "✗ Still exists"
[ ! -f proguard-rules.pro ] && echo "✓ Old proguard removed" || echo "✗ Still exists"
[ ! -d src ] && echo "✓ Old src/ removed" || echo "✗ Still exists"
[ ! -f verify_phase2.sh ] && echo "✓ Artifact removed" || echo "✗ Still exists"
```

## 4. Build Verification

### Clean Build
```bash
./gradlew clean
./gradlew assembleDebug
```

**Expected Result:**
- No errors
- APK created at: `app/build/outputs/apk/debug/app-debug.apk`

### Verify APK
```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

**Should show APK size:** ~10-20 MB

## 5. Static Analysis

```bash
# Ktlint
./gradlew ktlintCheck

# Detekt
./gradlew detekt
```

**Both should complete** (warnings OK, errors should be fixed)

## 6. Test Execution

```bash
./gradlew test
```

**Expected Result:**
- All unit tests pass
- No test failures

## 7. Configuration Files

### local.properties
```bash
cat local.properties
```

**Should contain:**
```properties
firebase.database.url=https://your-project.firebaseio.com
firebase.storage.bucket=your-project.appspot.com
sdk.dir=/path/to/Android/sdk
```

### google-services.json
```bash
ls -la app/google-services.json
```

**Should exist in `app/` directory** (git-ignored)

## 8. Git Status

```bash
git status
```

**Expected:**
```
On branch main
Your branch is up to date with 'origin/main'.

Untracked files:
  local.properties
  app/google-services.json

nothing to commit, working tree clean
```

(local.properties and google-services.json should be git-ignored)

## 9. Android Studio Module View

1. Open **Project** tab (left sidebar)
2. Switch view to **Project** (not Android)
3. Expand `app/` folder
4. Verify structure matches expected layout

## 10. Run Configuration

1. **Run** → **Edit Configurations**
2. **+** → **Android App**
3. **Module** dropdown should show: `face-recognition-attendance.app.main`
4. Click **OK**
5. Click **Run** (green play button)
6. App should install and launch on device/emulator

## Common Issues

### Issue: "Could not find :app"

**Fix:**
```bash
rm -rf .gradle
rm -rf .idea
./gradlew clean
# Restart Android Studio
```

### Issue: "Manifest not found"

**Fix:**
```bash
# Verify location
ls -la app/src/main/AndroidManifest.xml

# If missing, re-pull
git pull origin main
```

### Issue: "BuildConfig not found"

**Fix:**
```bash
# Ensure local.properties has Firebase URLs
cat local.properties | grep firebase

# Rebuild
./gradlew clean build
```

## Success Criteria

☑️ Directory structure follows Android standard  
☑️ Gradle sync completes without errors  
☑️ `./gradlew assembleDebug` succeeds  
☑️ Static analysis runs (ktlint + detekt)  
☑️ Unit tests pass  
☑️ No old root-level src/, AndroidManifest.xml, etc.  
☑️ App module visible in Android Studio  
☑️ Run configuration works  
☑️ App installs and launches  

## Next Steps

Once all checks pass:

1. Read [CONTRIBUTING.md](CONTRIBUTING.md) for development guidelines
2. Review [ARCHITECTURE.md](ARCHITECTURE.md) for code structure
3. Check [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md) for detailed migration docs
4. Start development!

## Need Help?

If any check fails:
1. Review error messages carefully
2. Check [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md) "Common Issues" section
3. Try fresh clone as last resort
4. Open [GitHub Issue](https://github.com/preethamdev05/face-recognition-attendance/issues) with:
   - Error message
   - Output of `./gradlew build --stacktrace`
   - Android Studio version
   - OS version
