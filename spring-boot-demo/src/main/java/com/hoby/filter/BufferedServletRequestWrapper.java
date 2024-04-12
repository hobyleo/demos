package com.hoby.filter;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 缓存请求包装类
 *
 * @author hoby
 * @since 2023-08-20
 */
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper {

    private BufferedServletInputStream stream;
    private String contentBody;

    public BufferedServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (stream == null) {
            ServletRequest request = this.getRequest();
            int len = request.getContentLength();
            byte[] buffer = new byte[len];
            receive(len, request.getInputStream(), buffer);
            this.stream = new BufferedServletInputStream(buffer);
        }
        this.stream.reset();
        return this.stream;
    }

    public String getContentBody() throws IOException {
        if (contentBody == null) {
            String contentString = receiveString(this.getRequest());
            if (contentString.isEmpty()) return null;
            contentBody = contentString;
        }
        return contentBody;
    }

    public String receiveString(ServletRequest request) throws IOException {
        int len = request.getContentLength();
        if (len <= 0) return "";
        byte[] buffer = new byte[len];
        receive(len, getInputStream(), buffer);
        return new String(buffer, StandardCharsets.UTF_8);
    }

    public void receive(int len, ServletInputStream stream, byte[] buffer) throws IOException {
        int offset = 0;
        while (offset < len) {
            int partLen = 4096;
            if (len - offset < partLen) {
                partLen = len - offset;
            }

            int i = stream.read(buffer, offset, partLen);
            if (i < 0) {
                throw new IOException("Read request body error");
            }
            offset = offset + i;
        }
    }
}
