package com.vijays.springboot.service;

import java.util.List;

import com.vijays.springboot.entity.Employee;

public interface EmployeeService {

	List<Employee> getAllEmployees();
    Employee getEmployeeById(int EmployeeId);
    Employee addEmployee(Employee Employee);
    Employee updateEmployee(Employee Employee);
    void deleteEmployee(int EmployeeId);
    boolean isEmployeeExist(Employee employee); 
   

}