package com.dbs.ordermatching.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.ordermatching.models.AuthUser;
import com.dbs.ordermatching.models.Custodian;

public interface UserRepository extends JpaRepository<AuthUser, String>{
     Optional<AuthUser> findByCustodianid(String custodianid);
}
