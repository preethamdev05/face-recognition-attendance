#!/usr/bin/env bash

# Verification Script for Security Phase 2
# Checks if secrets are removed from Constants.kt and if release build succeeds

set -euo pipefail

RED="\033[0;31m"
GREEN="\033[0;32m"
YELLOW="\033[1;33m"
NC="\033[0m" # No Color

echo -e "${YELLOW}======================================${NC}"
echo -e "${YELLOW}Security Phase 2 Verification${NC}"
echo -e "${YELLOW}======================================${NC}"
echo ""

# Test 1: Check if Constants.kt still contains hardcoded Firebase URLs
echo -e "${YELLOW}[1/3] Checking for hardcoded secrets in Constants.kt...${NC}"

CONSTANTS_FILE="src/main/kotlin/com/attendance/facerec/util/Constants.kt"

if [ ! -f "$CONSTANTS_FILE" ]; then
    echo -e "${RED}ERROR: Constants.kt not found at $CONSTANTS_FILE${NC}"
    exit 1
fi

# Check for hardcoded Firebase URLs (should NOT exist)
if grep -q '"https://.*\.firebaseio\.com"' "$CONSTANTS_FILE"; then
    echo -e "${RED}FAILED: Hardcoded Firebase Database URL found in Constants.kt${NC}"
    echo -e "${RED}Secrets must be moved to BuildConfig via local.properties${NC}"
    exit 1
fi

if grep -q '".*\.appspot\.com"' "$CONSTANTS_FILE"; then
    echo -e "${RED}FAILED: Hardcoded Firebase Storage Bucket found in Constants.kt${NC}"
    echo -e "${RED}Secrets must be moved to BuildConfig via local.properties${NC}"
    exit 1
fi

echo -e "${GREEN}PASSED: No hardcoded secrets found in Constants.kt${NC}"
echo ""

# Test 2: Check if Constants.kt uses BuildConfig
echo -e "${YELLOW}[2/3] Verifying BuildConfig usage in Constants.kt...${NC}"

if ! grep -q 'BuildConfig.FIREBASE_DATABASE_URL' "$CONSTANTS_FILE"; then
    echo -e "${RED}FAILED: Constants.kt does not use BuildConfig.FIREBASE_DATABASE_URL${NC}"
    exit 1
fi

if ! grep -q 'BuildConfig.FIREBASE_STORAGE_BUCKET' "$CONSTANTS_FILE"; then
    echo -e "${RED}FAILED: Constants.kt does not use BuildConfig.FIREBASE_STORAGE_BUCKET${NC}"
    exit 1
fi

echo -e "${GREEN}PASSED: Constants.kt correctly uses BuildConfig${NC}"
echo ""

# Test 3: Build release APK with new ProGuard rules
echo -e "${YELLOW}[3/3] Testing release build with tightened ProGuard rules...${NC}"

if ! ./gradlew assembleRelease --quiet; then
    echo -e "${RED}FAILED: Release build failed with new ProGuard rules${NC}"
    echo -e "${RED}Check proguard-rules.pro for missing keep rules${NC}"
    exit 1
fi

echo -e "${GREEN}PASSED: Release build succeeded with optimized ProGuard rules${NC}"
echo ""

# Success summary
echo -e "${GREEN}======================================${NC}"
echo -e "${GREEN}All Security Phase 2 Checks Passed!${NC}"
echo -e "${GREEN}======================================${NC}"
echo ""
echo -e "${YELLOW}Next Steps:${NC}"
echo "1. Create local.properties with your Firebase credentials:"
echo "   firebase.database.url=https://your-project.firebaseio.com"
echo "   firebase.storage.bucket=your-project.appspot.com"
echo ""
echo "2. Copy pre-commit hook to .git/hooks/:"
echo "   cp pre-commit .git/hooks/pre-commit"
echo "   chmod +x .git/hooks/pre-commit"
echo ""
echo "3. Test device security check in your app:"
echo "   if (!SecurityUtils.isDeviceSecure(context)) {"
echo "       // Show 'Unsupported Device' dialog"
echo "   }"
echo ""

exit 0
