package com.vijays.springboot.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vijays.springboot.constants.EmployeeOperation;
import com.vijays.springboot.entity.Employee;
import com.vijays.springboot.exception.EmployeeNotFound;
import com.vijays.springboot.repositories.EmployeeRepository;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {
  
	@Autowired
	private EmployeeAuditService employeeAuditService;
	
	@Resource
	private EmployeeRepository employeeRepository;
	
	@Override
	@Transactional
	public Employee getEmployeeById(int employeeId) {
		Employee employee = employeeRepository.findOne(employeeId);
		return employee;
	}	
	
	
	@Override
	@Transactional
	public List<Employee> getAllEmployees(){
		return (List<Employee>) employeeRepository.findAll();
	}
	
	
	@Override
	@Transactional
	public synchronized Employee addEmployee(Employee employee){
 	  	Employee createdEmployee = employeeRepository.save(employee);
 	  	//trigger an audit message - rabbit mq
		employeeAuditService.addEmployeeAudit(employee, EmployeeOperation.ADD_NEW_EMPLOYEE);
		return createdEmployee;
    
	}
	
	
	@Override
	@Transactional(rollbackFor=EmployeeNotFound.class)
	public Employee updateEmployee(Employee employee) {
		Employee createdEmployee = employeeRepository.save(employee);
		return createdEmployee;
	}
	
	
	@Override
	@Transactional(rollbackFor=EmployeeNotFound.class)
	public void deleteEmployee(int employeeId) {
		employeeRepository.delete(employeeId);	
	}
	
	
	@Override
	public boolean isEmployeeExist(Employee employee) {
	        return findByName(employee.getLastName())!=null;
	}
	
	public Employee findByName(String name) {
		
		List<Employee> employees = (List<Employee>) employeeRepository.findAll();
        for(Employee employee : employees){
            if(employee.getLastName().equalsIgnoreCase(name)){
                return employee;
            }
        }
        return null;
    }
	
	
	}

