package com.dbs.ordermatching.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class MyUserDetails implements UserDetails{
	
	private String custodianid;
	private String password;
	private boolean active;
	private List<GrantedAuthority> authorities; 
	
    public MyUserDetails() {
		
	}
	
	public MyUserDetails(AuthUser custodian) {
		
		this.custodianid  = custodian.getCustodianid();
		this.password = custodian.getPassword();
		this.active = true;
//		this.authorities = Arrays.stream(customeruser.getRoles().split(","))
//				           .map(SimpleGrantedAuthority::new)
//				           .collect(Collectors.toList());
		
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<GrantedAuthority> list = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
				return list;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.custodianid;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.active;
	}

	
	
}
