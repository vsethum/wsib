package com.vijays.springboot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.vijays.springboot.entity.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
	
	
}
