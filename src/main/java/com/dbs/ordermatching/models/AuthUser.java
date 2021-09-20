package com.dbs.ordermatching.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class AuthUser {
	
	@Id
	private String custodianid;
	@Size(min=8)
	private String password;
	
	public AuthUser() {
		
	}


	public AuthUser(String custodianid, String password) {
		super();
		this.custodianid = custodianid;
		this.password = password;
	}


	public String getCustodianid() {
		return custodianid;
	}


	public void setCustodianid(String custodianid) {
		this.custodianid = custodianid;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String toString() {
		return "AuthenticationRequest [custodianid=" + custodianid + ", password=" + password + "]";
	}


    
}
