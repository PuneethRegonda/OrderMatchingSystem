package com.dbs.ordermatching.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dbs.ordermatching.services.UserDetailService;
import com.dbs.ordermatching.utils.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;


@Component
public class JWTRequestFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTUtil jwtutil;
	
	@Autowired
	private UserDetailService userdetailsservice;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
	try {
		final String authorizationHeader = request.getHeader("Authorization");
		
		String custodianid = null;
		
		String jwt = null;
		
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			custodianid = jwtutil.extractUsername(jwt);
		}
		
		if(custodianid!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userdetails = this.userdetailsservice.loadUserByUsername(custodianid);
			
			if(jwtutil.validateToken(jwt, userdetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userdetails, null, userdetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		
	}catch(ExpiredJwtException e) {
		
		request.setAttribute("Exception", e);
		
	}catch(BadCredentialsException e) {
		request.setAttribute("Exception", e);
	}catch(Exception e) {
		System.out.println(e);
	}
//		System.out.println("hello");
		filterChain.doFilter(request,response);
		
	}

}
