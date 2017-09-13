package com.vijays.springboot.service;

import com.vijays.springboot.constants.EmployeeOperation;
import com.vijays.springboot.entity.Employee;
import com.vijays.springboot.entity.EmployeeAudit;

public interface EmployeeAuditService {

	void addEmployeeAudit(Employee Employee,EmployeeOperation operation);

	void processRecievedMessage(EmployeeAudit employeeAudit);
    

}