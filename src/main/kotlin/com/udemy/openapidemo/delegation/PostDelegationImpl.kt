package com.udemy.openapidemo.delegation

import com.udemy.openapidemo.apis.PostDelegate
import com.udemy.openapidemo.model.PostEntity
import com.udemy.openapidemo.models.*
import com.udemy.openapidemo.repository.PostRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class PostDelegationImpl(
    private val postRepository: PostRepository
) : PostDelegate {
    override fun addPost(addPostRequestBody: AddPostRequestBody): ResponseEntity<AddPostResponse> {
        val newPost = PostEntity().apply {
            title = addPostRequestBody.title
            description = addPostRequestBody.description
            authorName = addPostRequestBody.authorName
        }
        Logger.info("Request is processing with data {}", newPost)
        return runCatching {
            postRepository.save(newPost)
        }.fold(
            onSuccess = {
                val postId = it.id ?: return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
                ResponseEntity(AddPostResponse(postId), HttpStatus.CREATED)
            },
            onFailure = {
                Logger.error("Error occurred during saving in database {}", it)
                ResponseEntity(HttpStatus.BAD_REQUEST)
            }
        )
    }

    override fun deletePostById(postId: UUID): ResponseEntity<Unit> {
        return super.deletePostById(postId)
    }

    override fun getAllPosts(): ResponseEntity<GetPostsResponse> {
        return super.getAllPosts()
    }

    override fun getPostById(postId: UUID): ResponseEntity<PostDto> {
        return super.getPostById(postId)
    }

    override fun updatePostById(postId: UUID, updatePostRequestBody: UpdatePostRequestBody): ResponseEntity<Unit> {
        return super.updatePostById(postId, updatePostRequestBody)
    }

    companion object {
        val Logger = LoggerFactory.getLogger(this::class.java)
    }
}