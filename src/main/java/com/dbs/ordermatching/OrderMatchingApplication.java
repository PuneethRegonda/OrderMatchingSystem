package com.dbs.ordermatching;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dbs.ordermatching.repositories.TradeHistoryRepository;





@SpringBootApplication
public class OrderMatchingApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OrderMatchingApplication.class, args);
	}

	

	
	@Bean
	public PasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}
	
//	@Autowired
//	private TradeHistoryRepository repo ;
//	
//	@Bean
//	public void runpro() {
//		String sellid = repo.TRADEMATCH_ALGO(1, "093086e10e6240c8b6fea498e20cd15c");
//		System.out.println(sellid);
//	}

	
}
