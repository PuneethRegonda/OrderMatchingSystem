package com.dbs.ordermatching.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.ordermatching.models.AuthUser;
import com.dbs.ordermatching.models.AuthenticationResponse;
import com.dbs.ordermatching.models.Result;
import com.dbs.ordermatching.services.UserDetailService;
import com.dbs.ordermatching.utils.JWTUtil;


@RestController
@CrossOrigin
public class AuthenticationController {
	
	
	@Autowired
	private AuthenticationManager authenticationmanager;
	
	@Autowired
	private UserDetailService userdetailsservice;
	
	@Autowired
	private JWTUtil jwtutil;
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> DoAuthentication(@RequestBody AuthUser authenticationrequest)  throws Exception{
		
//		System.out.println(authenticationrequest);
		try {
		this.authenticationmanager.authenticate(
	      new UsernamePasswordAuthenticationToken(authenticationrequest.getCustodianid(), authenticationrequest.getPassword())
		);
		}catch(BadCredentialsException e) {
		   throw new Exception("Incorrect CustodianID or Password ",e);
		}
		final UserDetails userdetails  = userdetailsservice.loadUserByUsername(authenticationrequest.getCustodianid());
		
		final String jwt = jwtutil.generateToken(userdetails);
		
		return ResponseEntity.status(HttpStatus.OK).body(new Result(true, "successfull", jwt));
	}
}
