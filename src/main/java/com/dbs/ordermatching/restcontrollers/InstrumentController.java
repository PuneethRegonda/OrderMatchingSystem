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
import com.dbs.ordermatching.models.Instrument;
import com.dbs.ordermatching.models.Result;
import com.dbs.ordermatching.models.SellInstrument;
import com.dbs.ordermatching.repositories.ClientRepository;
import com.dbs.ordermatching.repositories.InstrumentRepository;
import com.dbs.ordermatching.services.InstrumentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/instruments")
@CrossOrigin
@SecurityRequirement(name ="api")
public class InstrumentController {
	@Autowired
	private InstrumentService service;
	
	
	@GetMapping
	public ResponseEntity<Result> getAllInstruments() {
		
		Result result = new Result();
		try {
			List<Instrument> list = this.service.loadAllInstruments();
			result.setStatus(true);
			result.setMessage("Instruments fetched successfully.");
			result.data = list;
			return ResponseEntity.status(HttpStatus.OK).body(result);	
		}catch (EntityNotFoundException e) {
			System.out.println("Instruments not found");
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
