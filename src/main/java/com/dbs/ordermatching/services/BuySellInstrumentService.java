package com.dbs.ordermatching.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dbs.ordermatching.models.BuyInstrument;
import com.dbs.ordermatching.models.Client;
import com.dbs.ordermatching.models.Instrument;
import com.dbs.ordermatching.models.SellInstrument;
import com.dbs.ordermatching.repositories.BuyInstrumentRepository;
import com.dbs.ordermatching.repositories.InstrumentRepository;
import com.dbs.ordermatching.repositories.SellInstrumentRepository;

@Service
public class BuySellInstrumentService {

	@Autowired
	private BuyInstrumentRepository buyRepo;
	@Autowired
	private SellInstrumentRepository sellRepo;

	

	public List<BuyInstrument> loadBuyInstrumentsByClientId(Client clientid) throws IllegalArgumentException {
		try {
			return this.buyRepo.findAllByClientid(clientid);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public List<SellInstrument> loadSellInstrumentsByClientId(Client clientid) throws IllegalArgumentException {
		try {
			return this.sellRepo.findAllByClientid(clientid);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public String updateBuyInstrument(BuyInstrument buyinstrument) throws IllegalArgumentException{

		try {
			buyinstrument =this.buyRepo.save(buyinstrument);
			return buyinstrument.getId();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public String  updateSellInstrument(SellInstrument sellinstrument) throws IllegalArgumentException{

		try {
			sellinstrument = this.sellRepo.save(sellinstrument);
			return sellinstrument.getId();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public BuyInstrument loadBuyInstrumentById(String  instrumentId) {
		try {
			Optional<BuyInstrument> cust = this.buyRepo.findById(instrumentId);
			return cust.orElseThrow(()->{	
				return new EntityNotFoundException("Instrument with  "+instrumentId+ " does not exist");
			});
			
		}catch(IllegalArgumentException e )
		{
			return null;
		}
	}	public SellInstrument loadSellInstrumentById(String  instrumentId) {
		try {
			Optional<SellInstrument> cust = this.sellRepo.findById(instrumentId);
			return cust.orElseThrow(()->{	
				return new EntityNotFoundException("Instrument with  "+instrumentId+ " does not exist");
			});
			
		}catch(IllegalArgumentException e )
		{
			return null;
		}
	}
	

}
