package com.vijays.springboot.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vijays.springboot.dao.EmployeeAuditDAO;
import com.vijays.springboot.entity.EmployeeAudit;

@Transactional
@Repository

public class EmployeeAuditDAOImpl implements EmployeeAuditDAO {
	
	@PersistenceContext	
	private EntityManager entityManager;	

	@Override
	public void addAudit(EmployeeAudit audit) {
		entityManager.persist(audit);
		
	}
	
}
