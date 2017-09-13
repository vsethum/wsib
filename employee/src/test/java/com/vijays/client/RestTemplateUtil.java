package com.vijays.client;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.vijays.springboot.entity.Employee;


public class RestTemplateUtil {
	
    public void getEmployeeByIdDemo() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
	String url = "http://localhost:8080/SpringBootEmployeeRestAPI/api/employees/{id}";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        ResponseEntity<Employee> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Employee.class, 1);
        Employee employee = responseEntity.getBody();
        System.out.println("Id:"+employee.getEmployeeId()+", First Name:"+employee.getFirstName()
                 +", Last Name:"+employee.getLastName());      
    }
    
    public void getAllEmployeesDemo() {
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
	String url = "http://localhost:8080/SpringBootEmployeeRestAPI/api/employees";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        ResponseEntity<Employee[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Employee[].class);
        Employee[] employees = responseEntity.getBody();
        for(Employee employee : employees) {
        	System.out.println("Id:"+employee.getEmployeeId()+", First Name:"+employee.getFirstName()
            +", Last Name:"+employee.getLastName());   
        }
    }
    
    public void addemployeeDemo() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/SpringBootEmployeeRestAPI/api/employees";
	
		Employee objemployee = new Employee();
		objemployee.setTitle("Sr. Engineer");
		objemployee.setFirstName("John");
		objemployee.setLastName("Doe");
		objemployee.setDepartment("Engineering");
		try {
			objemployee.setHiredDate(new java.sql.Date(new SimpleDateFormat("mm/dd/yyyy").parse("06/27/2007").getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objemployee.setSupervisorEmployeeId(100);
        HttpEntity<Employee> requestEntity = new HttpEntity<Employee>(objemployee, headers);
        URI uri = restTemplate.postForLocation(url, requestEntity);
        System.out.println(uri.getPath());    	
    }
    
    public void updateemployeeDemo() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/SpringBootEmployeeRestAPI/api/employees/{id}";
        Employee objemployee = new Employee();
        objemployee.setTitle("QA Manager");
		objemployee.setFirstName("Jane");
		objemployee.setLastName("Doe");
		objemployee.setDepartment("Quality Assurance");		
		objemployee.setSupervisorEmployeeId(100);
        HttpEntity<Employee> requestEntity = new HttpEntity<Employee>(objemployee, headers);
        ResponseEntity<Employee> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Employee.class, 1);
        Employee employee = responseEntity.getBody();
        System.out.println("Id:"+employee.getEmployeeId()+", First Name:"+employee.getFirstName()
        +", Last Name:"+employee.getLastName());   
        
        //restTemplate.put(url, requestEntity);
    }
    
    public void deleteemployeeDemo() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/SpringBootEmployeeRestAPI/api/employees/{id}";
        HttpEntity<Employee> requestEntity = new HttpEntity<Employee>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Employee.class, 1);        
    }
    
    public static void main(String args[]) {
    	RestTemplateUtil util = new RestTemplateUtil();
        //util.getEmployeeByIdDemo();
    	//util.getAllEmployeesDemo();
    	util.addemployeeDemo();
    	//util.updateemployeeDemo();
    	//util.deleteemployeeDemo();
    }    
} 