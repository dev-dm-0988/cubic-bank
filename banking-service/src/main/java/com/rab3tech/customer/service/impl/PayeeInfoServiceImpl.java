package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRespository;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.PayeeInfoVO;

@Service
@Transactional
public class PayeeInfoServiceImpl implements PayeeInfoService{
	
	@Autowired
	PayeeInfoRespository payeeInfoRespository;
	
	@Autowired
	CustomerAccountInfoRepository customerAccountInfoRepository;
	
	@Autowired
	CustomerRepository customerRepository;

	@Override
	public String savePayeeInfo(PayeeInfoVO payeeInfoVO) {
		String message=null;
		//Customer has valid account
		Optional<CustomerAccountInfo> customerAccount= customerAccountInfoRepository.findByCustomerId(payeeInfoVO.getCustomerId());
		if (customerAccount.isPresent()) {
			// check customer has saving account
			if (!customerAccount.get().getAccountType().equalsIgnoreCase("saving")) {
				message="customer doesnot have valid saving account, so cannot add payee.";
				return message;
			}
			//cannot add logged user's account as payee
			if (customerAccount.get().getAccountNumber().equalsIgnoreCase(payeeInfoVO.getPayeeAccountNo())){
				message="customer cannot own account as  payee.";
				return message;
			}
		}else {
			message="customer doesn't have valid account so cannot payee.";
			return message;
		}
		
		//check Payee has valid account 
		Optional<CustomerAccountInfo>  payeeAccount= customerAccountInfoRepository.findByAccountNumber(payeeInfoVO.getPayeeAccountNo());
		if (payeeAccount.isPresent()) {
			//checking payee has saving account or not.
			if (!payeeAccount.get().getAccountType().equalsIgnoreCase("saving")) {
				message="payee doesnot have valid saving account, so cannot add payee.";
				return message;
			}
			
			//check payee name is valid or not 
		
		
		}else {
			message="payee doesn't have valid account so cannot  payee.";
			return message;
		}
		
		//check duplicate payees
		Optional<PayeeInfo> duplicatePayee=payeeInfoRespository.findByCustomerIdAndPayeeAccountNo(payeeInfoVO.getCustomerId(), payeeInfoVO.getPayeeAccountNo());
		if(duplicatePayee.isPresent()) {
			message="payee has been added already.So, payee cannot be added.";
			return message;
		}
		
		PayeeInfo entity=new PayeeInfo();
		BeanUtils.copyProperties(payeeInfoVO, entity);
		entity.setDoe(new Timestamp(new Date().getTime()));
		entity.setDom(new Timestamp(new Date().getTime()));
		entity.setStatus("active");
		payeeInfoRespository.save(entity);
		message="Payee has been added successfully.";
		return message;
		
	}

	@Override
	public List<PayeeInfoVO> getAllPayees(String customerId) {
		List<PayeeInfoVO> listPayeeVO=new ArrayList<>();
		List<PayeeInfo> payeeEntities=payeeInfoRespository.findAllByCustomerId(customerId);
		
		for (PayeeInfo entity:payeeEntities) {
			PayeeInfoVO vo=new PayeeInfoVO();
			BeanUtils.copyProperties(entity, vo);
			listPayeeVO.add(vo);
		}
		
		return listPayeeVO;
	}

}
