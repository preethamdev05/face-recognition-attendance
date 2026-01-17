## Description

<!-- Provide a brief description of the changes in this PR -->

## Type of Change

<!-- Mark the relevant option with an 'x' -->

- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Code refactoring
- [ ] Performance improvement
- [ ] Security enhancement
- [ ] Test addition/update

## Related Issue

<!-- Link to the issue this PR addresses -->

Fixes #(issue number)

## Changes Made

<!-- Provide a detailed list of changes -->

- Change 1
- Change 2
- Change 3

## Screenshots/Recordings

<!-- If applicable, add screenshots or screen recordings to demonstrate the changes -->

### Before
<!-- Screenshot/recording of the previous behavior -->

### After
<!-- Screenshot/recording of the new behavior -->

## Testing

### Test Environment

- **Device:** [e.g., Pixel 7, Samsung Galaxy S23]
- **Android Version:** [e.g., Android 13, 14]
- **Build Type:** [Debug/Release]

### Test Cases

<!-- Describe how you tested your changes -->

- [ ] Test case 1: Description
- [ ] Test case 2: Description
- [ ] Test case 3: Description

### Test Results

```bash
# Paste output of test commands
./gradlew test
./gradlew ktlintCheck
./gradlew detekt
```

## Checklist

<!-- Mark completed items with an 'x' -->

### Code Quality

- [ ] My code follows the style guidelines of this project (Detekt + Ktlint)
- [ ] I have performed a self-review of my own code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] My changes generate no new warnings
- [ ] I have run static analysis and all checks pass

### Testing

- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
- [ ] I have tested on a physical device (not just emulator)
- [ ] I have verified the changes work on different Android versions

### Documentation

- [ ] I have made corresponding changes to the documentation
- [ ] I have updated the README.md if needed
- [ ] I have added/updated KDoc comments for public APIs
- [ ] I have updated ARCHITECTURE.md if architectural changes were made

### Security & Performance

- [ ] I have not introduced any security vulnerabilities
- [ ] I have not hardcoded any sensitive data (API keys, credentials)
- [ ] My changes do not negatively impact app performance
- [ ] I have verified ProGuard rules if I added new libraries

### Git

- [ ] My commits follow the Conventional Commits specification
- [ ] I have rebased my branch on the latest main
- [ ] I have resolved all merge conflicts
- [ ] My branch is up to date with the base branch

## Breaking Changes

<!-- If this PR introduces breaking changes, describe them and the migration path -->

None / Describe breaking changes here

## Dependencies

<!-- List any new dependencies added -->

- None / List dependencies

## Performance Impact

<!-- Describe any performance implications -->

- [ ] No performance impact
- [ ] Performance improved: [describe how]
- [ ] Performance degraded: [describe why and justify]

## Deployment Notes

<!-- Any special deployment considerations? -->

- [ ] No special deployment steps required
- [ ] Requires database migration
- [ ] Requires Firebase rules update
- [ ] Requires configuration changes in `local.properties`

## Rollback Plan

<!-- How can this change be rolled back if issues arise? -->

## Additional Notes

<!-- Any additional information reviewers should know -->

## Reviewer Checklist

<!-- For maintainers reviewing this PR -->

- [ ] Code follows project architecture (Clean Architecture + MVVM)
- [ ] Changes are covered by tests
- [ ] Documentation is adequate
- [ ] No security vulnerabilities introduced
- [ ] Performance impact is acceptable
- [ ] PR title follows Conventional Commits
- [ ] All CI/CD checks pass
