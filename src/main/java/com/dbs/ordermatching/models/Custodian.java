package com.dbs.ordermatching.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.ordermatching.utils.MyGenerator;


@Entity
public class Custodian implements Serializable{
	
	@Id
	private String custodianid;
	
	@Column(unique = true)
	private String custodianname;
	
	@Size(min=8)
	private String password;
	
	public Custodian() {
		
	}

	public Custodian( String custodianid,String custodianname, String password) {
		super();
	    this.custodianid=custodianid;
		this.custodianname = custodianname;
		this.password = "";
	}
	

	public Custodian(String custodianid) {
		super();
	    this.custodianid=custodianid;
		this.custodianname = "";
		this.password = "";
	}

	public String getCustodianid() {
		return custodianid;
	}

	public void setCustodianid(String custodianid) {
		this.custodianid = custodianid;
	}

	public String getCustodianname() {
		return custodianname;
	}


	public void setCustodianname(String custodianname) {
		this.custodianname = custodianname;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Custodian [custodianid=" + custodianid + ", custodianname=" + custodianname + ", password=" + password
				+ "]";
	}

}
