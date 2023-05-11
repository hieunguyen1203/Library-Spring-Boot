package library.hieund.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException authException) throws IOException, ServletException {

	final Map<String, Object> mapBodyException = new HashMap<>();

	mapBodyException.put("code", HttpServletResponse.SC_UNAUTHORIZED);
	mapBodyException.put("error", "Authentication Failed");

	// mapBodyException.put("message", "Message from AuthenticationEntryPoint");
	// mapBodyException.put("exception", "My stack trace exception");
	// mapBodyException.put("path", request.getServletPath());
	mapBodyException.put("timestamp", (new Date()).getTime());

	response.setContentType("application/json");
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

	final ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getOutputStream(), mapBodyException);

    }
}