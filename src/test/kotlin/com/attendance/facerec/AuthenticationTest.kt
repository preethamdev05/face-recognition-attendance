package com.attendance.facerec

import com.attendance.facerec.util.isValidEmail
import com.attendance.facerec.util.isValidPhoneNumber
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthenticationTest {

    @Test
    fun testValidEmail() {
        val validEmails = listOf(
            "user@example.com",
            "test.user@domain.co.uk",
            "user+tag@example.com"
        )
        validEmails.forEach {
            assertEquals("$it should be valid", true, it.isValidEmail())
        }
    }

    @Test
    fun testInvalidEmail() {
        val invalidEmails = listOf(
            "invalid.email",
            "@example.com",
            "user@"
        )
        invalidEmails.forEach {
            assertEquals("$it should be invalid", false, it.isValidEmail())
        }
    }

    @Test
    fun testValidPhoneNumber() {
        val validNumbers = listOf(
            "9876543210",
            "+919876543210",
            "98-765-43210"
        )
        validNumbers.forEach {
            assertEquals("$it should be valid", true, it.isValidPhoneNumber())
        }
    }

    @Test
    fun testInvalidPhoneNumber() {
        val invalidNumbers = listOf(
            "12345",
            "abcdefghij"
        )
        invalidNumbers.forEach {
            assertEquals("$it should be invalid", false, it.isValidPhoneNumber())
        }
    }
}
