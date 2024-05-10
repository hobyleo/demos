package com.hoby.config;

import com.hoby.handler.SpringWebSocketHandler;
import com.hoby.interceptor.SpringWebSocketHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author liaozh
 * @since 2024-05-10
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getSpringWebSocketHandler(), "/")
                .addInterceptors(getInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public SpringWebSocketHandler getSpringWebSocketHandler() {
        return new SpringWebSocketHandler();
    }

    @Bean
    public SpringWebSocketHandlerInterceptor getInterceptor() {
        return new SpringWebSocketHandlerInterceptor();
    }

}
