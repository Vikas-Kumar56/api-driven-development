package com.udemy.openapidemo.repository

import com.udemy.openapidemo.model.PostEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PostRepository : JpaRepository<PostEntity, UUID>