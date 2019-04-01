package com.muhammadtello.blog

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import java.util.*

@DataJpaTest
class RepositoriesTest @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository,
        val articleRepository: ArticleRepository
) {
    @Test
    fun `When findByIdOrNull then return Article`() {
        val muhammad = User("springmuhammad", "Muhammad", "","Tello")
        entityManager.persist(muhammad)
        val article = Article("Spring Framework 5.0 goes GA", "Dear Spring community...", "Lorem ipsum", muhammad)
        entityManager.persist(article)
        entityManager.flush()
        val found = articleRepository.findById(article.id!!).get()
        assertThat(found).isEqualTo(article)
    }

    @Test
    fun `When findByLogin then return User`() {
        val muhammad = User("ijaaz", "Muhammad", "", "Tello")
        entityManager.persist(muhammad)
        entityManager.flush()
        val user = userRepository.findByLogin(muhammad.login)
        assertThat(user).isEqualTo(muhammad)
    }
}