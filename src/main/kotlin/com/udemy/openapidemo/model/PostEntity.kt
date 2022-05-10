package com.udemy.openapidemo.model

import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "post")
class PostEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false)
    val id: UUID? = null
) {

    @Column(name = "title", nullable = false)
    var title: String? = null

    @Column(name = "description", nullable = false)
    var description: String? = null

    @Column(name = "authorName", nullable = false)
    var authorName: String? = null

    fun isNew() = id === null
}