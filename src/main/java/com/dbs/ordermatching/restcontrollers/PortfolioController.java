package com.dbs.ordermatching.restcontrollers;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.ordermatching.models.BuyInstrument;
import com.dbs.ordermatching.models.Client;
import com.dbs.ordermatching.models.Result;
import com.dbs.ordermatching.models.SellInstrument;
import com.dbs.ordermatching.services.BuySellInstrumentService;
import com.dbs.ordermatching.services.ClientService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/portfolio")
@CrossOrigin
@SecurityRequirement(name ="api")

public class PortfolioController {
	
	@Autowired
	private ClientService clientService ;

	@Autowired
	private BuySellInstrumentService buySellService ;
	
	
	@GetMapping("/{clientid}/{custodianid}")
	public ResponseEntity<Result> fetchProfileData(@PathVariable String clientid,@PathVariable String custodianid) {
		Result result = new Result();
		try { 
			Client client = this.clientService.findClientById(clientid);
			
			
			List<BuyInstrument> buys=  buySellService.loadBuyInstrumentsByClientId(client);
			List<SellInstrument> sells=  buySellService.loadSellInstrumentsByClientId(client);
 			
			result.setStatus(true);
			result.setMessage("Client portfolio found");
			result.data = Map.of(
					"client",client,
					"buy",buys,
					"sell",sells
					);

			return ResponseEntity.status(HttpStatus.OK).body(result);			
		}catch (EntityNotFoundException e) {
			System.out.println("Not found error");
			result.setStatus(false);
			result.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(result);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			result.setStatus(false);
			result.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(result);
		}
	}
	
	
}
