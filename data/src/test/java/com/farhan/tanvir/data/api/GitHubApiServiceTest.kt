package com.farhan.tanvir.data.api

import com.farhan.tanvir.common.Constant
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

const val USER_RESPONSE_FILE_NAME = "GetUsersApiResponse.json"
const val USER_DETAILS_RESPONSE_FILE_NAME = "GetUserDetailsApiResponse.json"
const val USER_REPO_RESPONSE_FILE_NAME = "GetUserRepoApiResponse.json"

val json: Json =
    Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

class GitHubApiServiceTest {
    private lateinit var service: GithubApiService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service =
            Retrofit.Builder()
                .baseUrl(server.url("")) // We will use MockWebServers url
                .addConverterFactory(
                    json.asConverterFactory(
                        "application/json; charset=UTF8".toMediaType(),
                    ),
                ).build()
                .create(GithubApiService::class.java)
    }

    @Test
    fun `getUsers receives expected response`() =
        runBlocking {
            enqueueMockResponse(USER_RESPONSE_FILE_NAME)
            val response = service.getUsers()

            // Verify the request path
            val request = server.takeRequest()
            assertEquals("/users?per_page=${Constant.ITEM_PER_PAGE}", request.path)

            Assert.assertTrue(response.isSuccessful)
            Assert.assertNotNull(response.body())
            assertEquals(100, response.body()?.size)
            assertEquals("mojombo", response.body()?.get(0)?.login)
            assertEquals("defunkt", response.body()?.get(1)?.login)
        }

    @Test
    fun `getUsers handles error response`() =
        runBlocking {
            // Enqueue a 500 Internal Server Error
            server.enqueue(MockResponse().setResponseCode(500))
            val response = service.getUsers()

            // Assert the response is not successful
            assertFalse(response.isSuccessful)
            assertEquals(500, response.code())
        }

    @Test
    fun `getUserDetails returns user details successfully`() =
        runBlocking {
            val username = "mojombo"
            enqueueMockResponse(USER_DETAILS_RESPONSE_FILE_NAME)
            val response = service.getUserDetails(username)

            // Assert the response
            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
            assertEquals(username, response.body()?.login)

            // Verify the request made to the correct URL
            val request = server.takeRequest()
            assertEquals("/users/$username", request.path)
        }

    @Test
    fun `getUserDetails handles API errors correctly`() =
        runBlocking {
            val username = "mojombo"
            server.enqueue(
                MockResponse().setResponseCode(404).setBody("{\"error\": \"User not found\"}"),
            )
            val response = service.getUserDetails(username)

            // Assert the response
            assertFalse(response.isSuccessful)
            assertEquals(404, response.code())
        }

    @Test
    fun `getUserRepos returns list of repositories successfully`() =
        runBlocking {
            val username = "mojombo"
            enqueueMockResponse(USER_REPO_RESPONSE_FILE_NAME)
            val response = service.getUserRepos(username)

            // Assert the response
            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
            assertEquals(66, response.body()?.size)
            assertEquals("30daysoflaptops.github.io", response.body()?.get(0)?.name)
            assertEquals("asteroids", response.body()?.get(1)?.name)

            // Verify the request made to the correct URL with expected query parameter
            val request = server.takeRequest()
            assertEquals("/users/$username/repos?per_page=${Constant.ITEM_PER_PAGE}", request.path)
        }

    @Test
    fun `getUserRepos handles API errors correctly`() =
        runBlocking {
            val username = "mojombo"
            server.enqueue(
                MockResponse().setResponseCode(404).setBody("{\"error\": \"User not found\"}"),
            )
            val response = service.getUserRepos(username)

            // Assert the response
            assertFalse(response.isSuccessful)
            assertEquals(404, response.code())
        }

    private fun enqueueMockResponse(fileName: String) {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.setBody(source.readString(Charsets.UTF_8))
            server.enqueue(mockResponse)
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}
