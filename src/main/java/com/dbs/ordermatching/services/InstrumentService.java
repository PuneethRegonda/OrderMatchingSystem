package com.dbs.ordermatching.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbs.ordermatching.models.Instrument;
import com.dbs.ordermatching.repositories.InstrumentRepository;

@Service
public class InstrumentService {

	@Autowired
	private InstrumentRepository instrumentRepository;
	

	public List<Instrument> loadAllInstruments() throws IllegalArgumentException{
		try {
			return  this.instrumentRepository.findAll();
		}catch(IllegalArgumentException e )
		{
			System.out.println(e.getMessage());
			throw e;
		}
	}
}
