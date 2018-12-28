package com.demo.springbootemployee.repository;

import com.demo.springbootemployee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findOneById(Long id);
}
