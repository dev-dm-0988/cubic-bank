package com.rab3tech.customer.service.impl;




import java.util.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.RoleRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.Role;
import com.rab3tech.utils.PasswordGenerator;
import com.rab3tech.vo.CustomerVO;

@Service
@Transactional
public class CustomerServiceImpl implements  CustomerService{
	
	@Autowired
	private MagicCustomerRepository customerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private CustomerRepository cRepository;
	
	
	@Override
	public CustomerVO createAccount(CustomerVO customerVO) {
		Customer pcustomer = new Customer();
		BeanUtils.copyProperties(customerVO, pcustomer);
		Login login = new Login();
		login.setNoOfAttempt(3);
		login.setLoginid(customerVO.getEmail());
		login.setName(customerVO.getName());
		String genPassword=PasswordGenerator.generateRandomPassword(8);
		customerVO.setPassword(genPassword);
		login.setPassword(bCryptPasswordEncoder.encode(genPassword));
		login.setToken(customerVO.getToken());
		login.setLocked("no");
		
		Role entity=roleRepository.findById(3).get();
		Set<Role> roles=new HashSet<>();
		roles.add(entity);
		//setting roles inside login
		login.setRoles(roles);
		//setting login inside
		pcustomer.setLogin(login);
		Customer dcustomer=customerRepository.save(pcustomer);
		customerVO.setId(dcustomer.getId());
		customerVO.setUserid(customerVO.getUserid());
		return customerVO;
	}


	@Override
	public CustomerVO getProfile(String email) {
		Optional<Customer> optional=cRepository.findByEmail(email);
		CustomerVO vo=new CustomerVO();
		if (optional.isPresent()) {
			Customer entity=optional.get();
			BeanUtils.copyProperties(entity,vo );
		}
		return vo;
	}


	@Override
	public String saveCustomerData(CustomerVO customerVO) {
		String message= null;
		Optional<Customer> optional= cRepository.findByEmail(customerVO.getEmail());
		
		if (optional.isPresent()) {
			Customer entity= optional.get();
			entity.setName(customerVO.getName());
			entity.setAddress(customerVO.getAddress());
			entity.setGender(customerVO.getGender());
			entity.setQualification(customerVO.getQualification());
			entity.setMobile(customerVO.getMobile());
			entity.setJobTitle(customerVO.getJobTitle());
			entity.setSsn(customerVO.getSsn());
			entity.setFather(customerVO.getFather());
			entity.setDob(customerVO.getDob());
			entity.setDom(new Timestamp(new Date().getTime()));
			cRepository.save(entity);
			message="Information has been updated successfully.";
		}else {
			message="error: there is some issue in customer data update.";
		}
		return message;
	}

}
