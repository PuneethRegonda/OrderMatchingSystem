package com.dbs.ordermatching.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbs.ordermatching.models.Client;
import com.dbs.ordermatching.models.Custodian;
import com.dbs.ordermatching.repositories.ClientRepository;

@Service
public class ClientService {

	
	@Autowired
	private ClientRepository clientRepo;
	

	public List<Client> loadAllClientById(Custodian custodianId ) throws IllegalArgumentException{
		try {
			return  this.clientRepo.findAllByCustodianid(custodianId);
		}catch(IllegalArgumentException e )
		{
			System.out.println(e.getMessage());
			throw e;
		}
	}
	
	public Client findClientById(String  clientId) throws IllegalArgumentException{
		try {
			Optional<Client> client = this.clientRepo.findById(clientId);
			
			return client.orElseThrow(()->{	
				return new EntityNotFoundException("Client with  "+clientId+ " does not exist");
			});
			
		}catch(IllegalArgumentException e )
		{
			throw e;
		}
	}
	
	public long fetchCountofClientById(Custodian custodianId ) throws IllegalArgumentException{
		try {
			return  this.clientRepo.countByCustodianid(custodianId);
		}catch(IllegalArgumentException e )
		{
			System.out.println(e.getMessage());
			throw e;
		}
	}
	
	

}
