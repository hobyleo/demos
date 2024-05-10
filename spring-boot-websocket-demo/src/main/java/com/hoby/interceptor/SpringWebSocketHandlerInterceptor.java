package com.hoby.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @author liaozh
 * @since 2024-05-10
 */
public class SpringWebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 这里拦截的是请求升级websocket的http
        System.out.println("拦截请求：前");
        // 返回true就会建立连接，false则反之
        // 所以可以在这里设置http请求的参数的用户名和密码
        ///然后通过request.getURI()，用字符串分割后获取参数
        // 如果要把参数往下传递给SpringWebSocketHandler，可以将参数放入attributes域中
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        System.out.println("拦截请求：后");
    }

}
