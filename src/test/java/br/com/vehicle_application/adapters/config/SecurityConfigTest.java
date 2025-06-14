package br.com.vehicle_application.adapters.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Mock
    private HttpSecurity httpSecurity;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(securityConfig, "jwkSetUri",
                "http://keycloak.example.com/auth/realms/vehicle/protocol/openid-connect/certs");
    }

    @Test
    void testJwtDecoderBean() {
        JwtDecoder jwtDecoder = securityConfig.jwtDecoder();
        assertNotNull(jwtDecoder);
        assertTrue(jwtDecoder instanceof NimbusJwtDecoder);
    }

    @Test
    void testJwtAuthenticationConverterBean() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();
        assertNotNull(converter);
    }

    @Test
    void testCorsConfigurationSourceBean() {
        CorsConfigurationSource corsSource = securityConfig.corsConfigurationSource();
        assertNotNull(corsSource);
        assertTrue(corsSource instanceof UrlBasedCorsConfigurationSource);
    }

    @Test
    void testFilterChain() throws Exception {
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.oauth2ResourceServer(any())).thenReturn(httpSecurity);

        securityConfig.filterChain(httpSecurity);

        verify(httpSecurity).cors(any());
        verify(httpSecurity).csrf(any());
        verify(httpSecurity).sessionManagement(any());
        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).oauth2ResourceServer(any());
    }

    @Test
    void testFilterChainWithException() throws Exception {
        when(httpSecurity.cors(any())).thenThrow(new RuntimeException("Test exception"));

        try {
            securityConfig.filterChain(httpSecurity);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }
}