package com.example.demo.filters;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
		
		String incomingUser = req.getUserPrincipal().getName();
		String remoteHost = req.getRemoteHost();
		String remoteAddr = req.getRemoteAddr();
		int remotePort = req.getRemotePort();
		String remoteUser = req.getRemoteUser();
		
//		Object userAttr = req.getAttribute("User Attributes");
		
		log.info("Incoming USER :: {} \n remoteHost :: {} \n remoteAddr :: {} \n remotePort :: {} \n remoteUser :: {} \n userattr {} "
				, incomingUser
				, remoteHost
				, remoteAddr
				, remotePort
				, remoteUser
//				, userAttr.toString()
				);
		
		/////////////////////////////
		//https://stackoverflow.com/questions/57434960/how-do-i-get-user-details-after-user-successfuly-logs-in-with-github-i-want-to
		//checking how authentication is working
		
		Authentication authentication = SecurityContextHolder.getContext()
	            .getAuthentication();
		
		Object principal = authentication.getPrincipal();
		
		
		log.info("principal fetched :: {} ", principal.toString());
		
		if(principal instanceof OAuth2AuthenticationToken){
			log.info("inside OAuth2AuthenticationToken to get the details!!!");
			
		    OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)principal;
		     
		    //By default its DefaultOAuth2User.
		    OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
		    
		    Map<String,Object> attributes =  oAuth2User.getAttributes();
		     
		    for(Map.Entry<String, Object> entry : attributes.entrySet()) {
		    	log.info("Attribute entry :: KEY : {}  => VALUE : {} ", entry.getKey(),entry.getValue());
		    }
		     
		}else {
//			log.info("NOT AN INSTANCE OF OAuth2AuthenticationToken");
//			
//			OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)principal;
//		     
//		    //By default its DefaultOAuth2User.
//			DefaultOAuth2User oAuth2User = (DefaultOAuth2User) oAuth2AuthenticationToken.getPrincipal();
//		    
//		    Map<String,Object> attributes =  oAuth2User.getAttributes();
//		     
//		    for(Map.Entry<String, Object> entry : attributes.entrySet()) {
//		    	log.info("Attribute entry :: KEY : {}  => VALUE : {} ", entry.getKey(),entry.getValue());
//		    }
		}
		
		

		
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
