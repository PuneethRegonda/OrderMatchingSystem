package com.dbs.ordermatching.models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.dbs.ordermatching.utils.CustodianDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
public class Client {
	
	@Id
	private String clientid;
	
	@Column(unique = true)
	private String clientname;
	
	@OneToOne
	@JoinColumn(name="custodianid")
	@JsonDeserialize(using = CustodianDeserializer.class)  
	private Custodian custodianid;
	@Column(nullable = false)
	private BigDecimal transactionlimit;
	@Column(nullable = false)
	private BigDecimal balance;
	
	public Client() {
		
	}
 
	public Client(String clientid) {
		
		this.clientid = clientid;
		
	}
	
	
	public Client(String clientid, String clientname, Custodian custodianid, BigDecimal transactionlimit,
			BigDecimal balance) {
		super();
		this.clientid = clientid;
		this.clientname = clientname;
		this.custodianid = custodianid;
		this.transactionlimit = transactionlimit;
		this.balance = balance;
	}




	public String getClientid() {
		return clientid;
	}



	public void setClientid(String clientid) {
		this.clientid = clientid;
	}



	public String getClientname() {
		return clientname;
	}



	public void setClientname(String clientname) {
		this.clientname = clientname;
	}



	public Custodian getCustodianid() {
		return custodianid;
	}



	public void setCustodianid(Custodian custodianid) {
		this.custodianid = custodianid;
	}



	public BigDecimal getTransactionlimit() {
		return transactionlimit;
	}



	public void setTransactionlimit(BigDecimal transactionlimit) {
		this.transactionlimit = transactionlimit;
	}



	public BigDecimal getBalance() {
		return balance;
	}



	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}



	@Override
	public String toString() {
		return "Client [clientid=" + clientid + ", clientname=" + clientname + ", custodianid=" + custodianid
				+ ", transactionlimit=" + transactionlimit + ", balance=" + balance + "]";
	}	

}


