# Deployment Guide

## Pre-Release Checklist

### Code Quality
- [ ] All tests pass
- [ ] ProGuard/R8 minification tested
- [ ] No hardcoded credentials
- [ ] Null safety enforced
- [ ] Error handling complete

### Security
- [ ] Firebase rules reviewed
- [ ] API keys restricted
- [ ] Permissions justified
- [ ] Encryption implemented
- [ ] GDPR compliance verified

### Performance
- [ ] Image compression working
- [ ] Database indexing applied
- [ ] Pagination implemented
- [ ] Memory leaks checked
- [ ] Profiling results acceptable

### Testing
- [ ] Unit tests: >80% coverage
- [ ] Integration tests passed
- [ ] UI tests for critical flows
- [ ] Device compatibility verified
- [ ] Network scenarios tested

## Build & Sign

```bash
# Build release APK
./gradlew assembleRelease

# Sign APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore keystore.jks app-release-unsigned.apk alias_name

# Align APK
zipalign -v 4 app-release-unsigned.apk app-release-signed.apk
```

## Play Store Release

1. Create release in Play Console
2. Upload signed APK
3. Fill release notes
4. Set rollout %
5. Monitor crash reports

## Monitoring

- Check Crashlytics daily
- Review Firebase Analytics
- Monitor storage usage
- Track database performance
- Review user feedback

## Rollback Plan

- Keep previous build APKs
- Maintain database backups
- Document API changes
- Plan hotfix releases
