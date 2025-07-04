package com.janz.userservice.infrastructure.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "users")
@Entity
data class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @Column(length = 128)
    var firstName: String,
    @Column(length = 128)
    var lastName: String,
    @Column(unique = true, length = 256)
    var email: String,
    @Column(unique = true, length = 32)
    var username: String,
    @Column(length = 128)
    var password: String,
    var birthday: LocalDateTime? = null,
    var active: Boolean =  true,
    @ManyToOne(fetch = FetchType.EAGER)
    var role: RoleEntity,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    @ManyToOne(fetch = FetchType.LAZY)
    var updatedBy: UserEntity? = null,
)