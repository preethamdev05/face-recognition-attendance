#!/usr/bin/env bash

# Automated Migration Script for Android Project Structure
# Moves files from root-level src/ to app/src/ and cleans up old artifacts

set -euo pipefail

RED="\033[0;31m"
GREEN="\033[0;32m"
YELLOW="\033[1;33m"
BLUE="\033[0;34m"
NC="\033[0m" # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Android Project Structure Migration${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check if we're in the repository root
if [ ! -f "settings.gradle.kts" ]; then
    echo -e "${RED}ERROR: Must run this script from repository root${NC}"
    echo -e "${RED}Current directory: $(pwd)${NC}"
    exit 1
fi

echo -e "${YELLOW}This script will:${NC}"
echo "  1. Move src/ directory to app/src/ (if exists at root)"
echo "  2. Delete old build.gradle.kts, proguard-rules.pro, AndroidManifest.xml from root"
echo "  3. Delete verify_phase2.sh artifact"
echo "  4. Preserve local.properties and google-services.json"
echo ""

read -p "Proceed with migration? (y/N): " -n 1 -r
echo ""
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}Migration cancelled.${NC}"
    exit 0
fi

echo ""
echo -e "${GREEN}Starting migration...${NC}"
echo ""

# Step 1: Move src/ to app/src/ if it exists at root
if [ -d "src" ] && [ ! -d "app/src" ]; then
    echo -e "${YELLOW}[1/5] Moving src/ to app/src/${NC}"
    mkdir -p app
    mv src app/
    echo -e "${GREEN}✓ Moved src/ to app/src/${NC}"
elif [ -d "src" ] && [ -d "app/src" ]; then
    echo -e "${YELLOW}[1/5] Both src/ and app/src/ exist${NC}"
    echo -e "${YELLOW}Merging contents...${NC}"
    cp -r src/* app/src/
    rm -rf src
    echo -e "${GREEN}✓ Merged and removed root src/${NC}"
else
    echo -e "${GREEN}[1/5] No root-level src/ found (already migrated)${NC}"
fi

# Step 2: Delete old build.gradle.kts from root IF it contains android block (app-level)
if [ -f "build.gradle.kts" ]; then
    if grep -q "android {" "build.gradle.kts"; then
        echo -e "${YELLOW}[2/5] Removing old app-level build.gradle.kts from root${NC}"
        rm -f build.gradle.kts
        echo -e "${GREEN}✓ Removed old build.gradle.kts (new root build file from latest commit will be used)${NC}"
    else
        echo -e "${GREEN}[2/5] Root build.gradle.kts is correct (plugins only)${NC}"
    fi
else
    echo -e "${GREEN}[2/5] No build.gradle.kts at root${NC}"
fi

# Step 3: Delete root-level AndroidManifest.xml
if [ -f "AndroidManifest.xml" ]; then
    echo -e "${YELLOW}[3/5] Removing root-level AndroidManifest.xml${NC}"
    rm -f AndroidManifest.xml
    echo -e "${GREEN}✓ Removed AndroidManifest.xml (now in app/src/main/)${NC}"
else
    echo -e "${GREEN}[3/5] No AndroidManifest.xml at root${NC}"
fi

# Step 4: Delete root-level proguard-rules.pro
if [ -f "proguard-rules.pro" ]; then
    echo -e "${YELLOW}[4/5] Removing root-level proguard-rules.pro${NC}"
    rm -f proguard-rules.pro
    echo -e "${GREEN}✓ Removed proguard-rules.pro (now in app/)${NC}"
else
    echo -e "${GREEN}[4/5] No proguard-rules.pro at root${NC}"
fi

# Step 5: Delete verify_phase2.sh
if [ -f "verify_phase2.sh" ]; then
    echo -e "${YELLOW}[5/5] Removing verify_phase2.sh artifact${NC}"
    rm -f verify_phase2.sh
    echo -e "${GREEN}✓ Removed verify_phase2.sh${NC}"
else
    echo -e "${GREEN}[5/5] No verify_phase2.sh found${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Migration Complete!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

echo -e "${YELLOW}Next Steps:${NC}"
echo "  1. Re-pull latest changes: ${BLUE}git pull origin main${NC}"
echo "  2. Invalidate Android Studio caches: ${BLUE}File -> Invalidate Caches / Restart${NC}"
echo "  3. Verify structure: ${BLUE}ls -la app/src/main/${NC}"
echo "  4. Sync Gradle: ${BLUE}./gradlew build${NC}"
echo ""

# Verify critical files exist
echo -e "${YELLOW}Verifying structure...${NC}"

ERRORS=0

if [ ! -f "app/build.gradle.kts" ]; then
    echo -e "${RED}✗ Missing app/build.gradle.kts${NC}"
    ERRORS=$((ERRORS+1))
else
    echo -e "${GREEN}✓ app/build.gradle.kts exists${NC}"
fi

if [ ! -f "app/src/main/AndroidManifest.xml" ]; then
    echo -e "${RED}✗ Missing app/src/main/AndroidManifest.xml${NC}"
    ERRORS=$((ERRORS+1))
else
    echo -e "${GREEN}✓ app/src/main/AndroidManifest.xml exists${NC}"
fi

if [ ! -f "settings.gradle.kts" ]; then
    echo -e "${RED}✗ Missing settings.gradle.kts${NC}"
    ERRORS=$((ERRORS+1))
else
    echo -e "${GREEN}✓ settings.gradle.kts exists${NC}"
fi

if [ $ERRORS -gt 0 ]; then
    echo ""
    echo -e "${RED}WARNING: ${ERRORS} critical files missing${NC}"
    echo -e "${YELLOW}Run: git pull origin main${NC}"
    exit 1
fi

echo ""
echo -e "${GREEN}Structure verification passed!${NC}"
echo -e "${BLUE}You can now open the project in Android Studio.${NC}"
echo ""

exit 0
