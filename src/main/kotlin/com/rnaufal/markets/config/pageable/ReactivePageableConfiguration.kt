package com.rnaufal.markets.config.pageable

import org.springframework.context.annotation.Configuration
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

@Configuration
class ReactivePageableConfiguration : WebFluxConfigurer {

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        val reactivePageableHandlerMethodArgumentResolver = ReactivePageableHandlerMethodArgumentResolver()
        reactivePageableHandlerMethodArgumentResolver.setOneIndexedParameters(true)
        configurer.addCustomResolver(reactivePageableHandlerMethodArgumentResolver)
    }
}
