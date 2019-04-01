package com.muhammadtello.blog

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

class AuthorResource(author: User) {
    val firstname: String = author.firstname
    val lastname: String = author.lastname
    val username: String = author.login
    val description: String? = author.description
}
class ArticleResource(article: Article) {
    val content = article.content
    val addedAt = article.addedAt
    val title = article.title
    val slug = article.slug
    val headline = article.headline
    val id = article.id;
    val author = AuthorResource(article.author)
}

@RestController
@RequestMapping("/api/article", produces = [MediaType.APPLICATION_JSON_VALUE])
class ArticleController(private val repository: ArticleRepository, private val assembler: ArticleResourceAssembler) {
    @Autowired
    private lateinit var userRepository: UserRepository

    @CrossOrigin
    @GetMapping("/")
    fun findAll(): Resources<Resource<ArticleResource>> {
        val articles = repository.findAllByOrderByAddedAtDesc().map {  assembler.toResource(ArticleResource(it))}
        return Resources(articles,
                linkTo(methodOn(ArticleController::class.java).findAll()).withSelfRel())
    }

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String): Resource<ArticleResource> {
        val article = repository.findBySlug(slug) ?: throw java.lang.IllegalArgumentException("Wrong article slug provided")
        return assembler.toResource(ArticleResource(article))
    }

    @GetMapping("/user/{userLogin}")
    fun findByUser(@PathVariable userLogin: String):Resources<Resource<ArticleResource>> {
        val articles = repository.findByAuthorLogin(userLogin).map { assembler.toResource(ArticleResource(it)) }
        return Resources(articles, linkTo(methodOn(ArticleController::class.java).findByUser(userLogin)).withSelfRel())
    }

    @PostMapping("/")
    fun addArticle(@RequestBody article: ArticleRequest): ResponseEntity<HttpStatus> {
        val userDetails = SecurityContextHolder.getContext().authentication.principal!!
        if(userDetails is UserDetails) {
                repository.save(Article(
                        article.title,
                        article.headline,
                        article.content,
                        userRepository.findByLogin(userDetails.username)!!
                ))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null)
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null)
    }
}

@RestController
@RequestMapping("/api/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(private val repository: UserRepository, private val userResourceAssembler: UserResourceAssembler) {
    @GetMapping("/")
    fun findAll(): Resources<Resource<User>> {
        val users = repository.findAll().map {  userResourceAssembler.toResource(it) }
        return Resources(users, linkTo(methodOn(UserController::class.java).findAll()).withSelfRel())
    }

    @PostMapping("/")
    fun addUser(@RequestBody user: User): ResponseEntity<HttpStatus> {
        val users = repository.findAll()
        System.out.println(user)
        return if(users.any { it.login == user.login })
            ResponseEntity.status(HttpStatus.CONFLICT).body(HttpStatus.CONFLICT)
        else {
            user.password = BCryptPasswordEncoder().encode(user.password)
            repository.save(user)
            ResponseEntity.accepted().body(HttpStatus.CREATED)
        }

    }

    @GetMapping("/{login}")
    fun findOne(@PathVariable login: String): Resource<User> = userResourceAssembler.toResource(repository.findByLogin(login))
}
