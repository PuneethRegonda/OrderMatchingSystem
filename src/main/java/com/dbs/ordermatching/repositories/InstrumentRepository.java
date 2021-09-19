package com.dbs.ordermatching.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.ordermatching.models.Instrument;


public interface InstrumentRepository extends JpaRepository<Instrument, String>{
//	List<Instrument> find
}
