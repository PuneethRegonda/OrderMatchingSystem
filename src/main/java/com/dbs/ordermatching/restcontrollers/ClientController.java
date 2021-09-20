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
import com.dbs.ordermatching.models.Custodian;
import com.dbs.ordermatching.models.Result;
import com.dbs.ordermatching.services.ClientService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/client")
@CrossOrigin
@SecurityRequirement(name ="api")
public class ClientController {


		@Autowired
		private ClientService clientService ;
		
		
		
		@GetMapping("/{clientid}")
		public ResponseEntity<Result> findClientById(   @PathVariable String clientid) {
			Result result = new Result();
			try { 
				Client client = this.clientService.findClientById(clientid);
				result.setStatus(true);
				result.setMessage("Client found");
				result.data = client;
				return ResponseEntity.status(HttpStatus.OK).body(result);			
			}catch (EntityNotFoundException e) {
				System.out.println("Client not found error");
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
		
		
		@GetMapping("/all/{custodianId}")
		public ResponseEntity<Result> findAllClientsByCustodianId(@PathVariable String custodianId) {
			Result result = new Result();
			try { 
				List<Client> clients = this.clientService.loadAllClientById(new Custodian(custodianId));
				result.setStatus(true);
				result.setMessage("Clients found.");
				result.data = clients;
				return ResponseEntity.status(HttpStatus.OK).body(result);			
			}catch (EntityNotFoundException e) {
				System.out.println("Clients not found error");
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
