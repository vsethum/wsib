package com.vijays.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.vijays.springboot.entity.Employee;
import com.vijays.springboot.exception.EmployeeNotFound;
import com.vijays.springboot.service.EmployeeService;
import com.vijays.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;  //service that will perform the CRUD on employee
	
	public static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	// -------------------Retrieve an employee based on employee id---------------------------------------------
	@SuppressWarnings("unchecked")
	@GetMapping("employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Integer id) {
		
		Employee employee = employeeService.getEmployeeById(id);		
		if (employee == null) {
            logger.error("Employee with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Employee with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	// -------------------Retrieve All Employees---------------------------------------------
	
	@GetMapping("employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		
		List<Employee> list = employeeService.getAllEmployees();
		
		if (list.isEmpty()) {
			 logger.error("No Employees found.");
	            return new ResponseEntity(HttpStatus.NO_CONTENT);
	            // TODO: VS HttpStatus.NOT_FOUND?
	    }
		 return new ResponseEntity<List<Employee>>(list, HttpStatus.OK);
	}
	
	 // -------------------Create an Employee-------------------------------------------
	
	@PostMapping("employees")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee, UriComponentsBuilder builder) {
		logger.info("Creating Employee : {}", employee);
		
		if (employeeService.isEmployeeExist(employee)) {
	            logger.error("Unable to create. An Employee with last name {} already exist", employee.getLastName());
	            return new ResponseEntity(new CustomErrorType("Unable to create. An Employee with last name " + 
	            employee.getLastName() + " already exist."),HttpStatus.CONFLICT);
	    }
		 
		 
        Employee newEmployee = employeeService.addEmployee(employee);
        if (newEmployee==null) {
        	return new ResponseEntity<Employee>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/api/employees/{id}").buildAndExpand(newEmployee.getEmployeeId()).toUri());
        return new ResponseEntity<Employee>(newEmployee, headers, HttpStatus.CREATED);
     
	}
	
	@PutMapping("employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) {
		
		logger.info("Updating Employee with id {}", id);
		
		Employee currentEmployee = employeeService.getEmployeeById(id);
		 
        if (currentEmployee == null) {
            logger.error("Unable to update. Employee with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. Employee with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
 
        if(employee.getDepartment()!=null) {
        	currentEmployee.setDepartment(employee.getDepartment());
        }
        
        if(employee.getFirstName()!=null) {
        	currentEmployee.setFirstName(employee.getFirstName());
        }
        
        if(employee.getLastName()!=null) {
        	currentEmployee.setLastName(employee.getLastName());
        }
        
        if (employee.getTitle()!=null) {
        	currentEmployee.setTitle(employee.getTitle());
        }
        
        if(employee.getHiredDate()!=null) {
        	currentEmployee.setHiredDate(employee.getHiredDate());
        }
        
        if(employee.getSupervisorEmployeeId() !=0) {
        	currentEmployee.setSupervisorEmployeeId(employee.getSupervisorEmployeeId()); 
        }
		employeeService.updateEmployee(currentEmployee);
		return new ResponseEntity<Employee>(currentEmployee, HttpStatus.OK); 
	}
	
	@DeleteMapping("employees/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") Integer id){
		
		logger.info("Deleting employee id: " + id);
		employeeService.deleteEmployee(id);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}	
	
	//Handle exceptions
	@ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleIOException(EmptyResultDataAccessException ex) {
        // prepare responseEntity
        return new ResponseEntity(new CustomErrorType("EmptyResultDataAccessException occured"),HttpStatus.NOT_FOUND); 
    }
    
}
