package com.udemy.openapidemo.controller

import com.udemy.openapidemo.apis.Post
import com.udemy.openapidemo.handler.NotFoundException
import com.udemy.openapidemo.models.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class PostController : Post {

    private val posts = mutableListOf<PostDto>()

    override fun addPost(addPostRequestBody: AddPostRequestBody): ResponseEntity<AddPostResponse> {
        val postId = UUID.randomUUID()
        val (title, authorName, description) = addPostRequestBody
        posts.add(
            PostDto(
                id = postId,
                title = title,
                description = description,
                authorName = authorName
            )
        )
        return ResponseEntity(AddPostResponse(postId), HttpStatus.OK)
    }

    override fun deletePostById(postId: UUID): ResponseEntity<Unit> {
        val post = posts.find { postId == it.id }
        post?.let {
            posts.remove(post)
        } ?: throw NotFoundException("$postId is not exist")

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    override fun getAllPosts(): ResponseEntity<GetPostsResponse> {
        return ResponseEntity(
            GetPostsResponse(
                posts = posts
            ), HttpStatus.OK
        )
    }

    override fun getPostById(postId: UUID): ResponseEntity<PostDto> {
        val post = posts.find { postId == it.id }
        post ?: throw NotFoundException("$postId is not exist")
        return ResponseEntity(post,HttpStatus.OK)
    }

    override fun updatePostById(postId: UUID, updatePostRequestBody: UpdatePostRequestBody): ResponseEntity<Unit> {
        val post = posts.find { postId == it.id }
        post ?: throw NotFoundException("$postId is not exist")

        if (!posts.remove(post)) {
            throw RuntimeException("Failed to update")
        }

        posts.add(PostDto(
            id = post.id,
            title = updatePostRequestBody.title,
            description = updatePostRequestBody.description,
            authorName = post.authorName
        ))

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}