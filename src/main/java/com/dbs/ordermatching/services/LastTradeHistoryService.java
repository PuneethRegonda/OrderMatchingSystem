package com.dbs.ordermatching.services;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dbs.ordermatching.models.LastTradeHistory;
import com.dbs.ordermatching.repositories.LastTradeHistoryRepository;

@Service
public class LastTradeHistoryService {
	
	@Autowired
	private LastTradeHistoryRepository lastTradeHistoryRepo;
	
	public LastTradeHistory loadById(String  id) {
		try {
			Optional<LastTradeHistory> cust = this.lastTradeHistoryRepo.findById(id);
			return cust.orElseThrow(()->{	
				return new EntityNotFoundException("TradeHistory with  "+id+ " does not exist");
			});			
		}catch(IllegalArgumentException e )
		{
			return null;
		}
	}
	
	public LastTradeHistory saveLastTradeHistory(LastTradeHistory  lastTradeHistory) {
		try {
			LastTradeHistory lastTrade = this.lastTradeHistoryRepo.save(lastTradeHistory);
			return lastTrade;	
		}catch(IllegalArgumentException e )
		{
			return null;
		}
	}
}
