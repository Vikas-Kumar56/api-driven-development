package com.udemy.openapidemo.delegate

import com.udemy.openapidemo.apis.PostDelegate
import com.udemy.openapidemo.handler.NotFoundException
import com.udemy.openapidemo.model.PostEntity
import com.udemy.openapidemo.models.*
import com.udemy.openapidemo.repository.PostRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class PostDelegateImpl(
    private val postRepository: PostRepository
) : PostDelegate {
    override fun addPost(addPostRequestBody: AddPostRequestBody): ResponseEntity<AddPostResponse> {
        val newPost = PostEntity().apply {
            title = addPostRequestBody.title
            description = addPostRequestBody.description
            authorName = addPostRequestBody.authorName
        }

        return runCatching {
            Logger.info("executing save operation")
            postRepository.save(newPost)
        }.fold(
            onSuccess = {
                val postId = it.id ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
                Logger.info("Post Id {}", postId)
                ResponseEntity(AddPostResponse(postId), HttpStatus.CREATED)
            },
            onFailure = {
                Logger.error("Error occurred {}", it)
                ResponseEntity(HttpStatus.BAD_REQUEST)
            }
        )

    }

    override fun deletePostById(postId: UUID): ResponseEntity<Unit> {
        val postOptional = postRepository.findById(postId)
        val post = postOptional.orElseThrow { NotFoundException("Post does not exist ${postId}") }
        postRepository.delete(post)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    override fun getAllPosts(): ResponseEntity<GetPostsResponse> {
        val posts = postRepository.findAll()
        val postDtos = posts.map {
            PostDto(
                id = it.id,
                title = it.title,
                description = it.description,
                authorName = it.authorName
            )
        }

        return ResponseEntity(GetPostsResponse(postDtos), HttpStatus.OK)
    }

    override fun getPostById(postId: UUID): ResponseEntity<PostDto> {
        val postOptional = postRepository.findById(postId)
        val post = postOptional.orElseThrow { NotFoundException("post with postid ${postId} does not exist") }
        return ResponseEntity(
            PostDto(
                id = post.id,
                title = post.title,
                description = post.description,
                authorName = post.authorName
            ), HttpStatus.OK)
    }

    override fun updatePostById(postId: UUID, updatePostRequestBody: UpdatePostRequestBody): ResponseEntity<Unit> {
        val postOptional = postRepository.findById(postId)
        val post = postOptional.orElseThrow { NotFoundException("Post does not exist ${postId}") }
        post.description = updatePostRequestBody.description
        post.title = updatePostRequestBody.title
        postRepository.save(post)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    companion object {
        val Logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}