package com.carpool.carpool.dto

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*


@Entity
@Table(name = "users")
class User {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    var id: UUID? = null

    @Setter
    @Getter
    @Column(nullable = false)
    var fullName: String? = null

    @Getter
    @Setter
    @Column(unique = true, length = 100, nullable = false)
    var email: String? = null

    @Setter
    @Getter
    @Column(nullable = false)
    var password: String? = null

    @Setter
    @Getter
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    var createdAt: Date? = null

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Date? = null

    @Setter
    @Getter
    @Column(name = "token")
    var token: String? = null

    @Column(name = "is_enabled", nullable = false)
    var enabled: Boolean? = null

    @Setter
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    val roles: MutableSet<UserRole?> = HashSet<UserRole?>()

    constructor()

    constructor(fullName: String?, email: String?, password: String?) {
        this.fullName = fullName
        this.email = email
        this.password = password
        this.enabled = false
        this.token = UUID.randomUUID().toString()
    }

    fun setUpdatedAt() {
        this.updatedAt = Date()
    }
}
