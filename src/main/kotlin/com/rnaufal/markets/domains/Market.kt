package com.rnaufal.markets.domains

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("markets")
data class Market(
    @Id
    val id: String? = null,
    val identifier: Int,
    val longitude: Long,
    val latitude: Long,
    val setCens: Long,
    val area: Long,
    val districtCode: Int,
    val district: String,
    val townCode: Int,
    val town: String,
    val firstZone: String,
    val secondZone: String,
    val name: String,
    @Indexed(unique = true)
    val registryCode: String,
    val publicArea: String,
    val number: String,
    val neighborhood: String,
    val reference: String? = null,
    @CreatedDate
    val createdAt: Instant? = Instant.now(),
    @LastModifiedDate
    val updatedAt: Instant? = null
)
