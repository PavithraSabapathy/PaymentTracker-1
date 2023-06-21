package com.mashreq.paymentTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mashreq.paymentTracker.model.DataSource;

@Repository
public interface DataSourceConfigRepository extends JpaRepository<DataSource, Long>{

}
