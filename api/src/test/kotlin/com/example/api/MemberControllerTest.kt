package com.example.api

import com.example.application.member.MemberCommandService
import com.example.application.member.MemberQueryService
import com.example.application.member.dto.MemberDto
import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.Loggers
import java.util.*

@WebFluxTest(MemberController::class)
class MemberControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var memberCommandService: MemberCommandService

    @MockBean
    private lateinit var memberQueryService: MemberQueryService

    private lateinit var member: MemberDto
    private lateinit var memberId: MemberId

    @BeforeEach
    fun setUp() {
        memberId = MemberId(UUID.randomUUID())
        member = MemberDto.fromDomain(Member(
            id = memberId,
            name = "John Doe",
            email = "john.doe@example.com",
        ))

        // Initialize WebTestClient with logging filter
        webTestClient = webTestClient.mutate()
            .filter(logRequest())
            .filter(logResponse())
            .build()
    }

    @Test
    fun `should create member successfully`() {
        val request = CreateMemberRequest(name = "John Doe", email = "john.doe@example.com")
        whenever(memberCommandService.createMember(request.name, request.email)).thenReturn(Mono.just(member))

        webTestClient.post()
            .uri("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(memberId.toString())
            .jsonPath("$.name").isEqualTo("John Doe")
            .jsonPath("$.email").isEqualTo("john.doe@example.com")
    }

    @Test
    fun `should get member by id successfully`() {
        whenever(memberQueryService.getMember(memberId)).thenReturn(Mono.just(member))

        webTestClient.get()
            .uri("/members/{id}", memberId.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(memberId.toString())
            .jsonPath("$.name").isEqualTo("John Doe")
            .jsonPath("$.email").isEqualTo("john.doe@example.com")
    }

    @Test
    fun `should update member successfully`() {
        val request = UpdateMemberRequest(name = "John Updated", email = "john.updated@example.com")
        val updatedMember = member.copy(name = request.name, email = request.email)
        whenever(memberCommandService.updateMember(memberId, request.name, request.email)).thenReturn(Mono.just(updatedMember))

        webTestClient.put()
            .uri("/members/{id}", memberId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(memberId.toString())
            .jsonPath("$.name").isEqualTo("John Updated")
            .jsonPath("$.email").isEqualTo("john.updated@example.com")
    }

    @Test
    fun `should delete member successfully`() {
        whenever(memberCommandService.deleteMember(memberId)).thenReturn(Mono.empty())

        webTestClient.delete()
            .uri("/members/{id}", memberId.toString())
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `should activate member successfully`() {
        val activatedMember = member.copy( status = MemberStatus.ACTIVE.name )
        whenever(memberCommandService.activateMember(memberId)).thenReturn(Mono.just(activatedMember))

        webTestClient.post()
            .uri("/members/{id}/activate", memberId.toString())
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(memberId.toString())
            .jsonPath("$.status").isEqualTo(MemberStatus.ACTIVE.toString())
    }

    @Test
    fun `should deactivate member successfully`() {
        val deactivatedMember =member.copy( status = MemberStatus.INACTIVE.name )
        whenever(memberCommandService.deactivateMember(memberId)).thenReturn(Mono.just(deactivatedMember))

        webTestClient.post()
            .uri("/members/{id}/deactivate", memberId.toString())
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(memberId.toString())
            .jsonPath("$.status").isEqualTo(MemberStatus.INACTIVE.toString())
    }

    @Test
    fun `should get all members successfully`() {
        val members = listOf(member)
        whenever(memberQueryService.getAllMembers()).thenReturn(Flux.fromIterable(members))

        webTestClient.get()
            .uri("/members")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(MemberDto::class.java)
            .hasSize(1)
            .contains(member)
    }

    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
            println("Request: ${clientRequest.method()} ${clientRequest.url()}")
            clientRequest.headers().forEach { name, values ->
                values.forEach { value -> println("$name: $value") }
            }
            Mono.just(clientRequest)
        }
    }

    private fun logResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse ->
            clientResponse.bodyToMono(String::class.java).flatMap { body ->
                Loggers.getLogger(MemberControllerTest::class.java).info("Response: ${clientResponse.statusCode()}")
                clientResponse.headers().asHttpHeaders().forEach { name, values ->
                    values.forEach { value -> Loggers.getLogger(MemberControllerTest::class.java).info("$name: $value") }
                }
                Loggers.getLogger(MemberControllerTest::class.java).info("Response Body: $body")
                Mono.just(clientResponse)
            }
        }
    }
}