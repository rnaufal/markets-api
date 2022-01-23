package com.rnaufal.markets.config.auditing

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing

@EnableReactiveMongoAuditing
@Configuration
class AuditingConfiguration
