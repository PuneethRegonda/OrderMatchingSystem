package com.dbs.ordermatching.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbs.ordermatching.models.BuyInstrument;
import com.dbs.ordermatching.models.Client;
import com.dbs.ordermatching.models.ClientInstrument;
import com.dbs.ordermatching.models.ClientInstruments;
import com.dbs.ordermatching.models.Custodian;
import com.dbs.ordermatching.models.Instrument;
import com.dbs.ordermatching.models.SellInstrument;
import com.dbs.ordermatching.models.TradeHistory;
import com.dbs.ordermatching.repositories.BuyInstrumentRepository;
import com.dbs.ordermatching.repositories.TradeHistoryRepository;

@Service
public class TradeHistoryService {

	@Autowired
	private TradeHistoryRepository tradeHistoryRepo;

	@Autowired
	private BuySellInstrumentService buySellInstrumentService;
	@Autowired
	private BuyInstrumentRepository buySellInstrumentRepo;

	@Autowired
	private ClientService clientService;

	@Autowired
	private ClientInstrumentService clientInstrumentService;

	public List<TradeHistory> loadAllTradeHistoryById(Custodian custodianId) throws IllegalArgumentException {
		try {
			return this.tradeHistoryRepo.findAllBySendercustodianidOrReceivercustodianid(custodianId, custodianId);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	public boolean insertTradeHistory(TradeHistory tradeHistory) {
		if (this.tradeHistoryRepo.findById(tradeHistory.getId()).isPresent())
			return false;
		try {
			this.tradeHistoryRepo.save(tradeHistory);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	public boolean insertAllTradeHistory(List<TradeHistory> tradeHistorys) throws EntityExistsException {
		tradeHistorys.forEach(trade -> {
			if (this.tradeHistoryRepo.findById(trade.getId()).isPresent())
				throw new EntityExistsException("Trade History with id " + trade.getId() + " already Exists");
		});
		try {
			this.tradeHistoryRepo.saveAll(tradeHistorys);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	public long fetchCountofBuyTradesById(Custodian custodianId) throws IllegalArgumentException {
		try {
			return this.tradeHistoryRepo.countBySendercustodianid(custodianId);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	public long fetchCountofSellTradesById(Custodian custodianId) throws IllegalArgumentException {
		try {
			return this.tradeHistoryRepo.countByReceivercustodianid(custodianId);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	public TradeHistory tradematchingEngine(String id, boolean isBuyRequest) throws Exception {

		if (isBuyRequest) {
			BuyInstrument buyInstrument = buySellInstrumentService.loadBuyInstrumentById(id);
			Client buyerclient = clientService.findClientById(buyInstrument.clientid.getClientid());
			BigDecimal totalTransaction = new BigDecimal(buyInstrument.getPrice() * buyInstrument.getQuantity());
			ClientInstruments buyerClientInstruments ;
			try {
				buyerClientInstruments = clientInstrumentService
						.loadClientInstrumersByCliesntIdAndInstrumentId(buyerclient, buyInstrument.getInstrumentid());
			}catch (EntityNotFoundException e ) {
				buyerClientInstruments = new ClientInstruments(buyerclient,buyInstrument.getInstrumentid(),0);
			}
			if (buyerclient.getBalance().compareTo(totalTransaction) == -1) {
				throw new Exception("Insufficient transaction limit");
			}

			if (buyerclient.getTransactionlimit().compareTo(totalTransaction) == -1) {
				throw new Exception("Trade exceeds your Maximum transaction limit");
			}

			List<SellInstrument> sellInstruments = buySellInstrumentRepo.findSellInstrumentToTrade(buyerclient,
					buyInstrument.getPrice(), buyInstrument.getInstrumentid());

			if (sellInstruments == null || sellInstruments.isEmpty()) {
				System.out.println("No Match found");
				throw new Exception("No Match dound partially active for now");
			} 
			else {
				System.out.println("Match found");
				SellInstrument sellInstrument = sellInstruments.get(0);
				Client sellerClient = sellInstrument.clientid;
				ClientInstruments sellerClientInstruments = clientInstrumentService
						.loadClientInstrumersByCliesntIdAndInstrumentId(sellerClient, buyInstrument.getInstrumentid());
				
				
				double totalTradeQuantityDone = onTradeMatchFound(buyInstrument, sellInstrument);

				// changing client instruments/stocks
				buyerClientInstruments.setQuantity(buyerClientInstruments.getQuantity() + totalTradeQuantityDone);
				sellerClientInstruments
						.setQuantity(sellerClientInstruments.getQuantity() -totalTradeQuantityDone);
				return onTradeSaveAllToDB(buyInstrument, buyerclient, buyerClientInstruments, sellerClient, sellInstrument,
						sellerClientInstruments, totalTradeQuantityDone);

			}

		} else {
			SellInstrument sellInstrument = buySellInstrumentService.loadSellInstrumentById(id);
			Client sellerclient =  clientService.findClientById(sellInstrument.clientid.getClientid());
			ClientInstruments sellerclientInstruments = clientInstrumentService.loadClientInstrumersByCliesntIdAndInstrumentId(sellerclient, sellInstrument.getInstrumentid());
			
			if (sellerclientInstruments.getQuantity() < sellInstrument.getQuantity()) {
				throw new Exception("Insufficient instrument quantity");
			}
			BigDecimal totalTransaction = new BigDecimal(sellInstrument.getPrice() * sellInstrument.getQuantity());
			if (sellerclient.getBalance().compareTo(totalTransaction) == -1) {
				throw new Exception("Insufficient Transaction Limit");
			}
			
			if (sellerclient.getTransactionlimit().compareTo(totalTransaction) == -1) {
				throw new Exception("Trade exceeds your Maximum transaction limit");
			}
			

			List<BuyInstrument> buyInstruments = buySellInstrumentRepo
					.findBuyInstrumentToTrade(sellerclient, sellInstrument.getPrice(), sellInstrument.getInstrumentid());
			
			
			if (buyInstruments == null || buyInstruments.isEmpty()) {
				System.out.println("No Match found");
				throw new Exception("No Match dound partially active for now");
			} else {
				System.out.println("Match found");
				BuyInstrument buyInstrument = buyInstruments.get(0);
				Client buyerClient = buyInstrument.clientid;

				ClientInstruments buyerClientInstruments = clientInstrumentService
						.loadClientInstrumersByCliesntIdAndInstrumentId(buyerClient, buyInstrument.getInstrumentid());

				double totalTradeQuantityDone = onTradeMatchFound(buyInstrument, sellInstrument);

				// changing client instruments/stocks
				buyerClientInstruments.setQuantity(buyerClientInstruments.getQuantity() + totalTradeQuantityDone);
				sellerclientInstruments
						.setQuantity(sellerclientInstruments.getQuantity() - totalTradeQuantityDone);
				return onTradeSaveAllToDB(buyInstrument, buyerClient, buyerClientInstruments, sellerclient, sellInstrument,
						sellerclientInstruments, totalTradeQuantityDone);

			}
			
		}

	}

	/**
	 * Handles all updates to DB after trade
	 * 
	 * @param buyInstrument
	 * @param buyerclient
	 * @param buyerClientInstruments
	 * @param sellerClient
	 * @param sellInstrument
	 * @param sellerClientInstruments
	 * @throws IllegalArgumentException
	 */
	private TradeHistory onTradeSaveAllToDB(BuyInstrument buyInstrument, Client buyerclient,
			ClientInstruments buyerClientInstruments, Client sellerClient, SellInstrument sellInstrument,
			ClientInstruments sellerClientInstruments, double tradeQuantity) throws IllegalArgumentException {
		buySellInstrumentService.updateSellInstrument(sellInstrument);
		buySellInstrumentService.updateBuyInstrument(buyInstrument);

		clientInstrumentService.updateClientInstrumenets(buyerClientInstruments);
		clientInstrumentService.updateClientInstrumenets(sellerClientInstruments);

		TradeHistory trade = new TradeHistory(sellerClient.getCustodianid(), buyerclient.getCustodianid(), sellerClient,
				buyerclient, buyInstrument.instrumentid, buyInstrument.getPrice(), tradeQuantity, new Date());
		return tradeHistoryRepo.save(trade);
	}

	/**
	 * Handles business logic of deductions from ClientInstruments and Buy/Sell
	 * Instruments
	 * 
	 * @param buyInstrument
	 * @param sellInstrument
	 * @return total trade
	 */
	private double onTradeMatchFound(BuyInstrument buyInstrument, SellInstrument sellInstrument) {
		double totalTradeQuantityDone = 0;
		if (sellInstrument.quantity > buyInstrument.getQuantity()) {

			totalTradeQuantityDone = buyInstrument.getQuantity();
			// client Buy/Sell Instruments
			sellInstrument.setQuantity(sellInstrument.getQuantity() - buyInstrument.getQuantity());
			buyInstrument.setQuantity(0);
			buyInstrument.setIsactive(false);

//					s,b
//					50,10
//					10,50
		} else if (sellInstrument.quantity < buyInstrument.getQuantity()) {

			totalTradeQuantityDone = sellInstrument.getQuantity();
			buyInstrument.setQuantity(buyInstrument.getQuantity() - sellInstrument.getQuantity());
			sellInstrument.setQuantity(0);
			sellInstrument.setIsactive(false);
		} else if (sellInstrument.quantity == buyInstrument.getQuantity()) {
			totalTradeQuantityDone = buyInstrument.getQuantity();
			buyInstrument.setQuantity(0);
			sellInstrument.setQuantity(0);
			sellInstrument.setIsactive(false);
			buyInstrument.setIsactive(false);
		}
		return totalTradeQuantityDone;
	}

}
