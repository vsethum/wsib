package com.vijays.springboot.listener;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vijays.springboot.entity.EmployeeAudit;
import com.vijays.springboot.service.EmployeeAuditService;



@Component
public class EmployeeAuditMessageListener {

	@Autowired
	private EmployeeAuditService employeeAuditService;
	
    private static final Logger log = Logger.getLogger(EmployeeAuditMessageListener.class);


    public void receiveMessage(Map<String, EmployeeAudit> message) {
        log.info("Received <" + message + ">");
        EmployeeAudit employeeAudit = message.get("employeeAudit");
        employeeAuditService.processRecievedMessage(employeeAudit);
        log.info("Message processed...");
    }
}
