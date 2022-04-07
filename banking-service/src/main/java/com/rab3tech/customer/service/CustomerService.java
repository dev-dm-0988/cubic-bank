package com.rab3tech.customer.service;

import com.rab3tech.vo.CustomerVO;

public interface CustomerService {

	CustomerVO createAccount(CustomerVO customerVO);
	
	CustomerVO getProfile(String email);
	
	String saveCustomerData(CustomerVO customerVO);
	

}
