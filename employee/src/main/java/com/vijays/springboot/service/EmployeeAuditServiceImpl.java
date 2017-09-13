package com.vijays.springboot.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vijays.springboot.SpringBootEmployeeRestAPI;
import com.vijays.springboot.constants.EmployeeOperation;
import com.vijays.springboot.dao.EmployeeAuditDAO;
import com.vijays.springboot.entity.Employee;
import com.vijays.springboot.entity.EmployeeAudit;

@Service("employeeAuditService")
public class EmployeeAuditServiceImpl implements EmployeeAuditService  {
  
	@Autowired
	private EmployeeAuditDAO employeeAuditDAO;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public void addEmployeeAudit(Employee employee, EmployeeOperation operation) {
		
		Map<String, EmployeeAudit> actionmap = new HashMap<>();		  
	    actionmap.put("employeeAudit", new EmployeeAudit(operation.name(),employee.getEmployeeId(),employee.getFirstName(),employee.getLastName(),employee.getTitle(),employee.getDepartment()));
		rabbitTemplate.convertAndSend(SpringBootEmployeeRestAPI.AUDIT_MESSAGE_QUEUE, actionmap);
	}
	
	
	@Override
	public void processRecievedMessage (EmployeeAudit employeeAudit) {		
		employeeAuditDAO.addAudit(employeeAudit);
	}

}