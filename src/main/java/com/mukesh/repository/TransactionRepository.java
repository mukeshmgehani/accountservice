package com.mukesh.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mukesh.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Serializable> {

}
