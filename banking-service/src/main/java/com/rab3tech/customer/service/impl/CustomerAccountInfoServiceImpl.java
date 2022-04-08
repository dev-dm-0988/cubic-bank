package com.rab3tech.customer.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.CustomerAccountEnquiryRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.service.CustomerAccountInfoService;
import com.rab3tech.dao.entity.AccountStatus;
import com.rab3tech.dao.entity.AccountType;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.vo.CustomerAccountInfoVO;

@Service
@Transactional
public class CustomerAccountInfoServiceImpl implements CustomerAccountInfoService{
	
	@Autowired
	CustomerAccountInfoRepository customerAccountInfoRepository;
	
	@Autowired
	CustomerAccountEnquiryRepository customerAccountEnquiryRepository;

	@Override
	public CustomerAccountInfoVO createAccount(String email) {
		CustomerAccountInfoVO vo= new CustomerAccountInfoVO();
		//check for duplicate account
		Optional<CustomerAccountInfo> customerAccount= customerAccountInfoRepository.findByCustomerId(email);
		
		if(customerAccount.isPresent()) { // if account exists,
			CustomerAccountInfo entity=customerAccount.get();
			BeanUtils.copyProperties(entity, vo);
		}else { // if account doesnt exist
			//query customer_saving_enquiry_tbl to get details
			Optional<CustomerSaving> optional=customerAccountEnquiryRepository.findByEmail(email);
			CustomerSaving saving=optional.get();
			CustomerAccountInfo entity=new CustomerAccountInfo();
			//setting up the account data
			entity.setCustomerId(email);
			entity.setAccountNumber(saving.getAppref());//todo
			entity.setCurrency("USD");
			entity.setBranch(saving.getLocation());//todo
			entity.setTavBalance(1000);
			entity.setAvBalance(1000);
			entity.setStatusAsOf(new Date());
			entity.setAccountType(saving.getAccType().getName());//todo
			
			CustomerAccountInfo account=customerAccountInfoRepository.save(entity);
			BeanUtils.copyProperties(account, vo);
		}
		return vo;
	}

}
