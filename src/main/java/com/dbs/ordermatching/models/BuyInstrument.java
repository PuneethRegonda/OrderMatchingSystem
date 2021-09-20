package com.dbs.ordermatching.models;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import com.dbs.ordermatching.utils.MyGenerator;


@Entity
public class BuyInstrument {
	
    @Id
    @GeneratedValue(generator = MyGenerator.generatorName)
    @GenericGenerator(name = MyGenerator.generatorName, strategy = "com.dbs.ordermatching.utils.MyGenerator")
	public String id;
    
    @ManyToOne
    @JoinColumn(name="clientid")
	public Client clientid;
	
    @ManyToOne
    @JoinColumn(name="instrumentid")
	public Instrument instrumentid;

	@Column(nullable = false) 	
	public double price;
	@Column(nullable = false) 	
	public double quantity;
	
	@Column(nullable = false) 	
	public Boolean isactive;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date createdate;

	public BuyInstrument() {
		// TODO Auto-generated constructor stub
	}

	public BuyInstrument(Client clientid, Instrument instrumentid, double price, double quantity,
			Boolean isactive, Date createdate) {
		super();
	
		this.clientid = clientid;
		this.instrumentid = instrumentid;
		this.price = price;
		this.quantity = quantity;
		this.isactive = isactive;
		this.createdate = createdate;
	}

	public Client getClientid() {
		return clientid;
	}

	public void setClientid(Client clientid) {
		this.clientid = clientid;
	}

	public Instrument getInstrumentid() {
		return instrumentid;
	}

	public void setInstrumentid(Instrument instrumentid) {
		this.instrumentid = instrumentid;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Boolean getIsactive() {
		return isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "BuyInstrument [id=" + id + ", clientid=" + clientid + ", instrumentid=" + instrumentid + ", price="
				+ price + ", quantity=" + quantity + ", isactive=" + isactive + ", createdate=" + createdate + "]";
	}

	

}
