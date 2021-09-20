package com.dbs.ordermatching.restcontrollers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.ordermatching.models.AuthUser;
import com.dbs.ordermatching.models.BuyInstrument;
import com.dbs.ordermatching.models.Client;
import com.dbs.ordermatching.models.ClientInstrument;
import com.dbs.ordermatching.models.ClientInstrumentid;
import com.dbs.ordermatching.models.ClientInstruments;
import com.dbs.ordermatching.models.Custodian;
import com.dbs.ordermatching.models.Instrument;
import com.dbs.ordermatching.models.LastTradeHistory;
import com.dbs.ordermatching.models.SellInstrument;
import com.dbs.ordermatching.models.TradeHistory;
import com.dbs.ordermatching.repositories.AuthUserRepository;
import com.dbs.ordermatching.repositories.BuyInstrumentRepository;
import com.dbs.ordermatching.repositories.ClientInstrumentRepository;
import com.dbs.ordermatching.repositories.ClientInstrumentsRepository;
import com.dbs.ordermatching.repositories.ClientRepository;
import com.dbs.ordermatching.repositories.CustodianRepository;
import com.dbs.ordermatching.repositories.InstrumentRepository;
import com.dbs.ordermatching.repositories.LastTradeHistoryRepository;
import com.dbs.ordermatching.repositories.SellInstrumentRepository;
import com.dbs.ordermatching.repositories.TradeHistoryRepository;
import com.dbs.ordermatching.utils.DBInit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@RestController
@RequestMapping("/init")
public class DBInitRestController {
	
	@Autowired
	private CustodianRepository custodianRepo;
	
	@Autowired
	private ClientRepository clientRepo;

	@Autowired
	private ClientInstrumentsRepository clientInstrumentRepo;
	
	@Autowired
	private BuyInstrumentRepository brepo;
	
	@Autowired
	private SellInstrumentRepository srepo;
	
	@Autowired 
	private TradeHistoryRepository tradeHistoryRepo;
	
	
	@Autowired 
	private LastTradeHistoryRepository lastTradeRepo;
	
	
	
	@Autowired 
	private AuthUserRepository authUserRepo;
	
	
	@Autowired 
	private InstrumentRepository instrumentRepo;
	@GetMapping
	public void initialize() throws JsonMappingException, JsonProcessingException {
		System.out.println("database init running");
		if(DBInit.BUILD_DB) {
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			/**
			 * 
			 *  custodian table
			 */
			List<Custodian> custodians = Arrays.asList(mapper.readValue(DBInit.CUSTODIANS, Custodian[].class));
			custodianRepo.saveAll(custodians);
			for (Custodian custodian: custodians) {
				System.out.println(custodian);
			}
			
			/**
			 * AuthUser table
			 */
			List<AuthUser> authUser = Arrays.asList(mapper.readValue(DBInit.AUTHUSERS, AuthUser[].class));
			authUserRepo.saveAll(authUser);
			for (AuthUser custodian: authUser) {
				System.out.println(custodian);
			}
			
			/**
			 *  Client table
			 */
			
			List<Client> clients = Arrays.asList(mapper.readValue(DBInit.CLIENTSJSON, Client[].class));
			clientRepo.saveAll(clients);
			for (Client client : clients) {
				System.out.println(client);
			}
			
			/**
			 *  Instrument table
			 */
			
			List<Instrument> instruments = Arrays.asList(mapper.readValue(DBInit.INSTRUMENTSJSON, Instrument[].class));
			instrumentRepo.saveAll(instruments);
			for (Instrument instrument : instruments) {
				System.out.println(instrument);
			}
			
			/**
			 * Client Instruments table
			 */
			List <ClientInstruments> clientInstruments = new ArrayList<>();
				
			instruments.forEach(instrument->{
				clientInstruments.add(new ClientInstruments("DBS001###"+instrument.getInstrumentid(),new Client("DBS001"), instrument, 100));
				clientInstruments.add(new ClientInstruments("DBS002###"+instrument.getInstrumentid(),new Client("DBS002"), instrument, 50));
				clientInstruments.add(new ClientInstruments("DBS005###"+instrument.getInstrumentid(),new Client("DBS005"), instrument, 50));
			});
			
			clientInstrumentRepo.saveAll(clientInstruments);
			
			insertData();
			
		}
	}
	
	

	public void insertData() {
		
		Custodian sellerCust = new Custodian("CS002");
		Client sellerClient = new Client("DBS005");
		Instrument instrument = new Instrument("I001");
		BuyInstrument buy = new BuyInstrument(sellerClient,instrument,
	    		100,100,true,new Date());
	    brepo.save(buy);
	    
	    Custodian buyerCust = new Custodian("CS001");
	    Client buyerClient = new Client("DBS001");
	    SellInstrument sell = new SellInstrument(buyerClient,instrument,
	    		100,100,true,new Date());
	    srepo.save(sell);
	    
	    TradeHistory trade = new TradeHistory(
	    		sellerCust, buyerCust, 
	    		sellerClient, buyerClient, 
	    		instrument, 
	    		buy.getPrice(), buy.getQuantity(), 
	    		new Date());
	    tradeHistoryRepo.save(trade);

	    lastTradeRepo.save(new LastTradeHistory(buyerCust.getCustodianid(), trade, new Date()));

	    lastTradeRepo.save(new LastTradeHistory(sellerCust.getCustodianid(), trade, new Date()));
			
	}
}
