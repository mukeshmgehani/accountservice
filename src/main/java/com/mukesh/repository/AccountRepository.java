package com.mukesh.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mukesh.entity.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account, Serializable> {
	
	Optional<Account> findByAccountNumber(String accountNumber);

}
