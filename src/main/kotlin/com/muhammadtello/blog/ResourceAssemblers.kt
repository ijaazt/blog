package com.muhammadtello.blog

import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceAssembler
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class ArticleResourceAssembler: ResourceAssembler<ArticleResource, Resource<ArticleResource>> {
    override fun toResource(entity: ArticleResource?) = Resource<ArticleResource>(entity!!,
            linkTo(methodOn(ArticleController::class.java).findOne(entity.slug)).withSelfRel(),
            linkTo(methodOn(ArticleController::class.java).findAll()).withRel("articles"),
            linkTo(methodOn(UserController::class.java).findOne(entity.author.username)).withRel("author")
        )
}

@Component
class UserResourceAssembler: ResourceAssembler<User, Resource<User>> {
    override fun toResource(entity: User?) = Resource<User>(
            entity!!,
            linkTo(methodOn(UserController::class.java).findOne(entity.login)).withSelfRel(),
            linkTo(methodOn(UserController::class.java).findAll()).withRel("users"),
            linkTo(methodOn(ArticleController::class.java).findByUser(entity.login)).withRel("posts")
    )

}