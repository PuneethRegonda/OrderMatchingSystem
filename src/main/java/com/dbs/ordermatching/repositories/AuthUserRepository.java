package com.dbs.ordermatching.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dbs.ordermatching.models.AuthUser;
import com.dbs.ordermatching.models.Custodian;

public interface AuthUserRepository extends JpaRepository<AuthUser, String>{

}
