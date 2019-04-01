package com.muhammadtello.blog

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest(UserController::class)
@Import(UserResourceAssembler::class)
class UserControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var userRepository: UserRepository

    @Test
    fun `List Users`() {
        val muhammad: User = User("ijaaz", "Muhammad", "", "Tello")
        every {userRepository.findAll() } returns listOf(muhammad)
        mockMvc.perform(get("/api/user/").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.content[0].firstname").value(muhammad.firstname))
    }
}