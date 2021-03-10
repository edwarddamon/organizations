package com.lhamster.config;

import com.lhamster.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalConfig implements WebMvcConfigurer {
    /**
     * 配置jwt拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        // 指定进和不仅拦截器的路由
        registry.addInterceptor(new JwtInterceptor()).addPathPatterns("/**").excludePathPatterns(
                "/swagger*/**", "/v2/**", "/swagger-resources/**", "/configuration/**",
                "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.gif", "/**/*.jpeg", "/**/*.jpg", "/**/*.svg", "/**/*.woff", "/**/*.woff2", "/**/*.ttf");
    }

    /**
     * 配置跨域
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        // 表示什么域名跨域 *表示全部都跨域
        corsConfiguration.addAllowedOrigin("*");
        // 设置有效时长
        corsConfiguration.setMaxAge(3600L);
        // 注入进去
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        CorsFilter corsFilter = new CorsFilter(urlBasedCorsConfigurationSource);
        return corsFilter;
    }
}
