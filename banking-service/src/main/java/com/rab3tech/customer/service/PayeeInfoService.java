package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.vo.PayeeInfoVO;

public interface PayeeInfoService {

	public String savePayeeInfo(PayeeInfoVO payeeInfoVO);
	
	List<PayeeInfoVO> getAllPayees(String customerId);
	

}
