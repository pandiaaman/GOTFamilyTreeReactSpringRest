package com.example.demo.filters;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

//for all requests
@Component
@Order(1)
@WebFilter("/*")
@Slf4j
public class MainFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		log.info("inside main filter :: entering the application...");
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse  res= (HttpServletResponse) response;
		
		
		if (!req.getRequestURL().toString().contains("gotbak/api/characters")){  
			log.error("OH OH! incoming request is different one!");
			
            res.setStatus(HttpStatus.BAD_GATEWAY.value());
		    res.getOutputStream().flush();
		    res.getOutputStream().println("Incorrect URI -- no data here!!");
            return; // No hago nada.
        }
		
		log.info("incoming request LOOKS GOOD");
		chain.doFilter(request, response);
	}
}
