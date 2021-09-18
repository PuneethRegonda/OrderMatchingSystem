package com.dbs.ordermatching.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.dbs.ordermatching.models.TradeHistory;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, String>{

//	List<TradeHistory> findAllBySendercustodianidOrReceivercustodianid(String sendercustodianid,String receivercustodianid);
	 
	@Query("select u from TradeHistory u where u.sendercustodianid = :sendercustodianid or u.receivercustodianid = :receivercustodianid")
	  List<TradeHistory> findAllBySendercustodianidOrReceivercustodianid(@Param("sendercustodianid") String sendercustodianid,
	                                 @Param("receivercustodianid") String receivercustodianid);
}
