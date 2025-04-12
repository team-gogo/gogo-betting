package gogo.gogobetting.global.config

import gogo.gogobetting.global.filter.AuthenticationFilter
import gogo.gogobetting.global.filter.LoggingFilter
import gogo.gogobetting.global.handler.CustomAccessDeniedHandler
import gogo.gogobetting.global.handler.CustomAuthenticationEntryPointHandler
import gogo.gogobetting.global.internal.user.stub.Authority
import gogo.gogobetting.global.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.IpAddressMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val customAuthenticationEntryPointHandler: CustomAuthenticationEntryPointHandler,
    private val authenticationFilter: AuthenticationFilter,
    private val loggingFilter: LoggingFilter,
    private val securityProperties: SecurityProperties
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.formLogin { it.disable() }
            .httpBasic { it.disable() }

        http.csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }

        http.exceptionHandling { handling ->
            handling.accessDeniedHandler(customAccessDeniedHandler)
            handling.authenticationEntryPoint(customAuthenticationEntryPointHandler)
        }

        http.sessionManagement { sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(authenticationFilter, LoggingFilter::class.java)

        http.authorizeHttpRequests { httpRequests ->
            // health check
            httpRequests.requestMatchers(HttpMethod.GET, "/betting/health").permitAll()
            httpRequests.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()

            // betting
            httpRequests.requestMatchers(HttpMethod.POST, "/betting/{match_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)

            // batch
            httpRequests.requestMatchers(HttpMethod.POST, "/betting/batch/{match_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)
            httpRequests.requestMatchers(HttpMethod.POST, "/betting/batch/cancel/{match_id}").hasAnyRole(Authority.USER.name, Authority.STAFF.name)

            // server to server
            httpRequests.requestMatchers(HttpMethod.GET, "/betting/bundle").access { _, context -> hasIpAddress(context) }
            httpRequests.requestMatchers(HttpMethod.GET, "/betting/point").access { _, context -> hasIpAddress(context) }

            httpRequests.anyRequest().denyAll()
        }

        return http.build()
    }

    private fun hasIpAddress(context: RequestAuthorizationContext): AuthorizationDecision {
        val ALLOWED_IP_ADDRESS_MATCHER = IpAddressMatcher("${securityProperties.serverToServerIp}${securityProperties.serverToServerSubnet}")
        return AuthorizationDecision(ALLOWED_IP_ADDRESS_MATCHER.matches(context.request))
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // plz custom allowed client origins
        configuration.allowedOrigins = listOf("*")

        configuration.allowedMethods = listOf(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name()
        )

        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}
