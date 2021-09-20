package com.dbs.ordermatching.restcontrollers;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.ordermatching.models.AuthUser;
import com.dbs.ordermatching.models.Custodian;
import com.dbs.ordermatching.models.Result;
import com.dbs.ordermatching.repositories.CustodianRepository;
import com.dbs.ordermatching.repositories.UserRepository;
import com.dbs.ordermatching.utils.JWTUtil;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/custodian")
@CrossOrigin
@SecurityRequirement(name ="api")

public class CustodianController {

	@Autowired
	private JWTUtil jwtutil;
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired 
	private CustodianRepository custodianRepo;
	
	@GetMapping("/getuser")
	public ResponseEntity<Result> findCustomerid(HttpServletRequest request) throws UsernameNotFoundException {
		
        final String authorizationHeader = request.getHeader("Authorization");
		
		String custodianid = null;
		
		
		String jwt = null;
		
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			System.out.println("JWT: "+jwt);
			custodianid= jwtutil.extractUsername(jwt);
			
		}
		Optional<AuthUser> opt = userrepository.findByCustodianid(custodianid);
		
		String clientId = custodianid;
		opt.orElseThrow(()->new UsernameNotFoundException("Username Not Found "));
		
		Optional<Custodian> cust=  custodianRepo.findById(opt.get().getCustodianid());
		
		 cust.orElseThrow(()->{	
			return new EntityNotFoundException("Custodian with  "+clientId+ " does not exist");
		});
		
		return ResponseEntity.status(HttpStatus.OK).body(new Result(true, "User found", cust.get()));
		
		
	}
	
	
}
