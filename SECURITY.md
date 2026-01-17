# Security Policy

## Supported Versions

We actively maintain and provide security updates for the following versions:

| Version | Supported          |
| ------- | ------------------ |
| > 1.0.0 | :white_check_mark: |
| < 1.0.0 | :x:                |

Only the **latest stable version** receives security updates. We recommend always using the most recent release.

## Reporting a Vulnerability

**DO NOT** open public GitHub issues for security vulnerabilities.

We take security seriously. If you discover a security vulnerability in the Face Recognition Attendance System, please follow these steps:

### 1. Email Security Team

Send a detailed report to:

**Email:** support@attendance-system.dev

**Subject:** [SECURITY] Brief description of the vulnerability

### 2. Include the Following Information

To help us understand and address the issue quickly, include:

- **Type of vulnerability** (e.g., authentication bypass, SQL injection, XSS)
- **Affected component** (e.g., face recognition module, Firebase authentication)
- **Steps to reproduce** the vulnerability
- **Potential impact** of the vulnerability
- **Suggested fix** (if you have one)
- **Your contact information** for follow-up questions

### 3. Response Timeline

- **Initial Response:** Within 48 hours of receiving your report
- **Status Update:** Within 7 days with our assessment
- **Fix Timeline:** Depends on severity:
  - **Critical:** Within 7 days
  - **High:** Within 14 days
  - **Medium:** Within 30 days
  - **Low:** Next scheduled release

### 4. Disclosure Policy

We follow **Coordinated Disclosure**:

1. You report the vulnerability privately
2. We acknowledge and investigate
3. We develop and test a fix
4. We release a patched version
5. We publicly disclose the vulnerability (with credit to you, if desired)

**Please allow us at least 90 days** to address the issue before public disclosure.

## Security Best Practices

When using this application, follow these security practices:

### For Administrators

1. **Secure Credentials:**
   - Never commit `local.properties` with real Firebase credentials
   - Use separate Firebase projects for dev/staging/production
   - Rotate API keys regularly

2. **Firebase Security Rules:**
   - Review and update `database_rules.json` regularly
   - Implement role-based access control (RBAC)
   - Enable Firebase App Check to prevent API abuse

3. **Network Security:**
   - Always use HTTPS for API endpoints
   - Verify SSL certificates
   - Keep `network_security_config.xml` up to date

4. **Device Security:**
   - The app blocks rooted devices by default
   - Enforce device encryption policies
   - Use SafetyNet API for device integrity checks

### For Developers

1. **Code Review:**
   - All code changes require peer review
   - Run static analysis (`detekt`) before committing
   - Check for hardcoded secrets using `verify_phase2.sh`

2. **Dependency Management:**
   - Keep dependencies up to date
   - Review dependency vulnerabilities using:
     ```bash
     ./gradlew dependencyCheckAnalyze
     ```
   - Use Firebase BOM for version consistency

3. **Data Protection:**
   - Encrypt sensitive data using `SecurityUtils.encryptData()`
   - Never log sensitive information (PIDs, passwords, tokens)
   - Use Android Keystore for cryptographic keys

4. **ProGuard/R8:**
   - Verify obfuscation rules in `proguard-rules.pro`
   - Test release builds thoroughly
   - Never disable minification in production

## Known Security Features

This application implements the following security measures:

### 1. Root Detection
- Detects rooted devices using `SecurityUtils.isDeviceSecure()`
- Gracefully blocks app usage on compromised devices

### 2. Network Security
- Enforces HTTPS-only connections
- Implements certificate pinning for Firebase
- Blocks cleartext traffic in production

### 3. Data Encryption
- Face embeddings encrypted at rest using AES-256-GCM
- Android Keystore for key management
- Encrypted SharedPreferences for sensitive data

### 4. Authentication
- Firebase Authentication with email/phone verification
- Optional biometric authentication (fingerprint/face unlock)
- Session timeout after 15 minutes of inactivity

### 5. Code Obfuscation
- R8 shrinking and obfuscation enabled
- ProGuard rules optimized for security
- Debug logs removed in release builds

## Vulnerability Disclosure History

None reported as of January 2026.

## Security Updates

Security updates are released as:

- **Patch versions** (1.0.x) for minor security fixes
- **Minor versions** (1.x.0) for moderate security improvements
- **Major versions** (x.0.0) for critical security overhauls

Subscribe to [GitHub Releases](https://github.com/preethamdev05/face-recognition-attendance/releases) for notifications.

## Bug Bounty Program

We currently do not have a bug bounty program. However, we deeply appreciate security researchers who report vulnerabilities responsibly. Contributors will be acknowledged in:

- Release notes
- Security advisories
- CONTRIBUTORS.md file

## Contact

**Security Team:** support@attendance-system.dev

**PGP Public Key:** Available upon request

## Additional Resources

- [OWASP Mobile Security Testing Guide](https://owasp.org/www-project-mobile-security-testing-guide/)
- [Android Security Best Practices](https://developer.android.com/topic/security/best-practices)
- [Firebase Security Rules](https://firebase.google.com/docs/rules)

---

**Last Updated:** January 17, 2026
