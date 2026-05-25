package cl.duoc.airflytrip.trips.security;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignAuthRequestInterceptorConfig {

    @Bean
    public RequestInterceptor feignAuthRequestInterceptor() {
        return template -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
                return;
            }

            HttpServletRequest request = servletRequestAttributes.getRequest();
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader != null && !authorizationHeader.isBlank()) {
                template.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
            }
        };
    }
}
