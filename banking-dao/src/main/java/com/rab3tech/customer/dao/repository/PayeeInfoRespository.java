package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rab3tech.dao.entity.PayeeInfo;

@Repository
public interface PayeeInfoRespository extends JpaRepository<PayeeInfo,Integer>{
	
	Optional<PayeeInfo> findByCustomerIdAndPayeeAccountNo(String customerId, String accountNo);

	List<PayeeInfo> findAllByCustomerId(String customerId);

}
