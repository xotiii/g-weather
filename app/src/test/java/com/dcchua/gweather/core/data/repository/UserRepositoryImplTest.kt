package com.dcchua.gweather.core.data.repository

import com.dcchua.gweather.core.data.local.dao.UserDao
import com.dcchua.gweather.core.data.local.entity.UserEntity
import com.dcchua.gweather.core.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class UserRepositoryImplTest {

    private lateinit var sut: UserRepositoryImpl

    @RelaxedMockK
    private lateinit var userDao: UserDao

    @BeforeEach
    fun setup() {
        sut = UserRepositoryImpl(userDao)
    }

    @Test
    fun `registerUser should insert user and return id when successful`() = runTest {
        val username = "testuser"
        val password = "password123"
        val firstName = "John"
        val expectedId = 1L
        val userEntity = UserEntity(username = username, password = password, firstName = firstName)
        
        coEvery { userDao.insertUser(any()) } returns expectedId

        val result = sut.registerUser(username, password, firstName)

        assertEquals(expectedId, result)
        coVerify { userDao.insertUser(userEntity) }
    }

    @Test
    fun `loginUser should return user when credentials are correct`() = runTest {
        val username = "testuser"
        val password = "password123"
        val firstName = "John"
        val userEntity = UserEntity(id = 1L, username = username, password = password, firstName = firstName)
        
        coEvery { userDao.getUserByUsername(username) } returns userEntity

        val result = sut.loginUser(username, password)

        val expectedUser = User(id = 1L, firstName = firstName)
        assertEquals(expectedUser, result)
    }

    @Test
    fun `loginUser should return null when user not found`() = runTest {
        val username = "testuser"
        val password = "password123"
        
        coEvery { userDao.getUserByUsername(username) } returns null

        val result = sut.loginUser(username, password)

        assertNull(result)
    }

    @Test
    fun `loginUser should return null when password incorrect`() = runTest {
        val username = "testuser"
        val password = "password123"
        val wrongPassword = "wrongpassword"
        val userEntity = UserEntity(id = 1L, username = username, password = password, firstName = "John")
        
        coEvery { userDao.getUserByUsername(username) } returns userEntity

        val result = sut.loginUser(username, wrongPassword)

        assertNull(result)
    }
}
