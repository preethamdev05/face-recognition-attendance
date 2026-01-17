# Firebase Setup Guide

This guide walks you through setting up Firebase for the Face Recognition Attendance System.

## Prerequisites

- Google account
- Android Studio installed
- Project cloned and opened in Android Studio

## Step 1: Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **Add project**
3. Enter project name: `face-recognition-attendance`
4. Enable/disable Google Analytics (optional)
5. Click **Create project**

## Step 2: Add Android App

1. In Firebase Console, click **Add app** > **Android**
2. Register app:
   - **Android package name:** `com.attendance.facerec`
   - **App nickname:** Face Recognition Attendance
   - **SHA-1:** (optional for now, required for Google Sign-In)
3. Click **Register app**

## Step 3: Download Configuration File

1. Download `google-services.json`
2. Move it to `app/` directory:
   ```bash
   mv ~/Downloads/google-services.json /path/to/face-recognition-attendance/app/
   ```

## Step 4: Enable Authentication

1. In Firebase Console, go to **Build** > **Authentication**
2. Click **Get started**
3. Enable sign-in methods:
   - **Email/Password:** Enable
   - **Phone:** Enable (configure test phone numbers if needed)

## Step 5: Setup Realtime Database

1. Go to **Build** > **Realtime Database**
2. Click **Create database**
3. Choose location (e.g., `us-central1`)
4. Start in **test mode** (we'll secure it later)
5. Note your database URL: `https://YOUR-PROJECT-ID.firebaseio.com`

### Configure Security Rules

1. In Realtime Database, go to **Rules** tab
2. Replace default rules with content from `database_rules.json`:
   ```bash
   # Copy rules from repository
   cat database_rules.json
   ```
3. Click **Publish**

## Step 6: Setup Storage

1. Go to **Build** > **Storage**
2. Click **Get started**
3. Use default security rules (we'll update them)
4. Choose location (same as database)
5. Note your bucket: `YOUR-PROJECT-ID.appspot.com`

### Configure Storage Rules

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Allow authenticated users to read/write their own face images
    match /students/{studentId}/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == studentId;
    }
    
    // Allow admins to read/write everything
    match /{allPaths=**} {
      allow read, write: if request.auth != null && 
        exists(/databases/(default)/documents/admins/$(request.auth.uid));
    }
  }
}
```

## Step 7: Enable Firebase Services

Enable these services in Firebase Console:

1. **Crashlytics** (Build > Crashlytics)
2. **Analytics** (Build > Analytics)
3. **Cloud Messaging** (Build > Cloud Messaging)

## Step 8: Configure local.properties

1. Copy example file:
   ```bash
   cp local.properties.example local.properties
   ```

2. Edit `local.properties` and add Firebase credentials:
   ```properties
   firebase.database.url=https://YOUR-PROJECT-ID.firebaseio.com
   firebase.storage.bucket=YOUR-PROJECT-ID.appspot.com
   sdk.dir=/path/to/Android/sdk
   ```

## Step 9: Initialize Database Structure

Create initial database structure in Firebase Console:

```json
{
  "students": {},
  "attendance": {},
  "classes": {
    "default-class": {
      "name": "Computer Science A",
      "createdAt": 1705478400000
    }
  },
  "admins": {
    "YOUR_USER_ID": {
      "email": "admin@example.com",
      "role": "admin"
    }
  },
  "users": {}
}
```

## Step 10: Test Firebase Connection

1. Build and run the app:
   ```bash
   ./gradlew installDebug
   ```

2. Check logcat for Firebase initialization:
   ```bash
   adb logcat | grep Firebase
   ```

3. You should see:
   ```
   FirebaseApp: Firebase initialization successful
   ```

## Step 11: Setup GitHub Secrets (for CI/CD)

1. Go to GitHub repository > **Settings** > **Secrets and variables** > **Actions**
2. Add the following secrets:
   - `FIREBASE_DATABASE_URL`: Your Firebase database URL
   - `FIREBASE_STORAGE_BUCKET`: Your Firebase storage bucket

## Troubleshooting

### "google-services.json not found"
- Ensure file is in `app/` directory, not project root
- Sync Gradle: File > Sync Project with Gradle Files

### "Authentication failed"
- Verify package name matches exactly: `com.attendance.facerec`
- Check SHA-1 certificate if using Google Sign-In

### "Database permission denied"
- Verify security rules are published
- Check authentication is working
- Ensure user UID matches rules

### "Storage upload failed"
- Check storage rules
- Verify file size is under limits
- Ensure proper authentication

## Production Checklist

Before going to production:

- [ ] Update security rules to production mode
- [ ] Enable App Check for API abuse protection
- [ ] Set up budget alerts in Firebase Console
- [ ] Configure backup for Realtime Database
- [ ] Enable Security Rules logging
- [ ] Set up monitoring and alerts
- [ ] Review and test all security rules
- [ ] Add SHA-1/SHA-256 certificates for release builds

## Additional Resources

- [Firebase Documentation](https://firebase.google.com/docs)
- [Realtime Database Security Rules](https://firebase.google.com/docs/database/security)
- [Storage Security Rules](https://firebase.google.com/docs/storage/security)
- [Firebase Pricing](https://firebase.google.com/pricing)

## Support

If you encounter issues:

1. Check [Firebase Status Dashboard](https://status.firebase.google.com/)
2. Search [Stack Overflow](https://stackoverflow.com/questions/tagged/firebase)
3. Open an issue in this repository
4. Email: support@attendance-system.dev
