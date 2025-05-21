package com.joaovitormo.desafio_luizalabs.data.local.preferences

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TokenManagerTest {

    private lateinit var context: Context
    private lateinit var tokenManager: TokenManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        tokenManager = TokenManager(context)
        tokenManager.clearTokens()
    }

    @Test
    fun `saveToken and getAccessToken should return valid token`() {
        val token = "testToken"
        val expiresIn = 3600L

        tokenManager.saveToken(token, expiresIn)

        val retrievedToken = tokenManager.getAccessToken()
        assertEquals(token, retrievedToken)
    }

    @Test
    fun `getAccessToken should return null when token is expired`() {
        val token = "expiredToken"
        val expiresIn = -1L

        tokenManager.saveToken(token, expiresIn)

        val retrievedToken = tokenManager.getAccessToken()
        assertNull(retrievedToken)
    }

    @Test
    fun `isTokenValid should return true for valid token`() {
        val token = "validToken"
        val expiresIn = 3600L
        tokenManager.saveToken(token, expiresIn)

        val isValid = tokenManager.isTokenValid()
        assertTrue(isValid)
    }

    @Test
    fun `isTokenValid should return false for expired token`() {
        val token = "expiredToken"
        val expiresIn = -1L

        tokenManager.saveToken(token, expiresIn)

        val isValid = tokenManager.isTokenValid()
        assertFalse(isValid)
    }

    @Test
    fun `clearTokens should remove token`() {
        val token = "toBeCleared"
        val expiresIn = 3600L

        tokenManager.saveToken(token, expiresIn)
        tokenManager.clearTokens()

        val retrievedToken = tokenManager.getAccessToken()
        assertNull(retrievedToken)
    }

}
