package com.dbs.ordermatching.restcontrollers;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.ordermatching.models.Client;
import com.dbs.ordermatching.models.ClientInstrument;
import com.dbs.ordermatching.models.ClientInstruments;
import com.dbs.ordermatching.models.Custodian;
import com.dbs.ordermatching.models.Result;
import com.dbs.ordermatching.services.ClientInstrumentService;
import com.dbs.ordermatching.services.ClientService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/client/instruments")
@CrossOrigin
@SecurityRequirement(name ="api")
public class ClientInstrumentsController {


		@Autowired
		private ClientInstrumentService clientInstrumentService ;
		
		@GetMapping("/{clientid}")
		public ResponseEntity<Result> findAllClientInstrumentsByClientId(@PathVariable String clientid) {
			Result result = new Result();
			try { 
				List<ClientInstruments> clientInstruments = this.clientInstrumentService.loadAllInstrumentsByClientId(new Client(clientid));
				result.setStatus(true);
				result.setMessage("Client Instruments found.");
				result.data = clientInstruments;
				return ResponseEntity.status(HttpStatus.OK).body(result);			
			}catch (EntityNotFoundException e) {
				System.out.println("Client Instruments not found error");
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
