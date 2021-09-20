package com.dbs.ordermatching.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dbs.ordermatching.models.AuthUser;
import com.dbs.ordermatching.models.Custodian;
import com.dbs.ordermatching.models.MyUserDetails;
import com.dbs.ordermatching.repositories.UserRepository;


@Service
public class UserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userrepo;

	@Override
	public UserDetails loadUserByUsername(String custodianid) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<AuthUser> opt = this.userrepo.findByCustodianid(custodianid);
		
//		System.out.println("custodian id"+custodianid);
		
        opt.orElseThrow(()->new UsernameNotFoundException("NOT FOUND "+custodianid));
		
		return opt.map(MyUserDetails::new).get();
		
	}
	
	

}
