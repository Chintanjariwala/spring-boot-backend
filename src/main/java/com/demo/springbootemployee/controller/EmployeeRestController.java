package com.demo.springbootemployee.controller;

import com.demo.springbootemployee.exception.ResourceNotFoundException;
import com.demo.springbootemployee.model.Employee;
import com.demo.springbootemployee.repository.EmployeeRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/rest")
public class EmployeeRestController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeRestController.class);

    @Inject
    private EmployeeRepository employeeRepository;

    /**
     * GET /employees : Get all employees.
     */
    @ApiOperation(value="Get all the employees item, returns an array of employee")
    @GetMapping("/employees")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        LOG.debug("REST request to get all Employees");
        return new ResponseEntity<>(employeeRepository.findAll(), HttpStatus.OK);
    }

    /**
     * GET /employees/{id} : Get an employee by id.
     */
    @ApiOperation(value="Get one employee by id, returns a employee")
    @GetMapping("/employees/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        LOG.debug("REST request to get one Employee by Id: {}", employeeId);
        return Optional.ofNullable(employeeRepository.findOneById(employeeId))
                .map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
    }

    /**
     * POST /employees : Creates a new employee.
     */
    @ApiOperation(value="Create a employee, add the employee in request body, returns void")
    @PostMapping("/employees")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody Employee employee) throws URISyntaxException {
        LOG.debug("REST request to save Employee : {}", employee);

        if (employee.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new author cannot already have an ID").build();
        }

        Employee result = employeeRepository.save(employee);
        return ResponseEntity.created(new URI("/api/rest/employees/" + result.getId())).build();
    }

    /**
     * PUT /employees : Update an existing employee.
     */
    @ApiOperation(value="Update an existing employee, add employee for update in the request body, returns updated employee")
    @PutMapping("/employees/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException  {
        LOG.debug("REST request to update Employee : {}", employeeDetails);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmailId(employeeDetails.getEmailId());
        employee.setPhoneNumber(employeeDetails.getPhoneNumber());
        employee.setAge(employeeDetails.getAge());
        employee.setBaseSalary(employeeDetails.getBaseSalary());
        employee.setMartialStatus(employeeDetails.getMartialStatus());
        employee.setFedralAllowances(employeeDetails.getFedralAllowances());
        employee.setHealthInsurance(employeeDetails.getHealthInsurance());
        employee.setVisionInsurance(employeeDetails.getVisionInsurance());
        employee.setRetirement401K(employeeDetails.getRetirement401K());
        final Employee updateEmployee = employeeRepository.save(employee);

        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }

    /**
     * DELETE /employees/{id} : Delete an employee by id.
     */
    @ApiOperation(value="Delete a employee by id, returns deleted true or false")
    @DeleteMapping("/employees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) throws ResourceNotFoundException  {
        LOG.debug("REST request to delete Employee with ID : {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

