package com.demo.springbootemployee.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "EMPLOYEE")
public class Employee implements Serializable {

    private static final long serialVersionUID = 5756128887126179776L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String emailId;

    @Column(name = "phone")
    private Integer phoneNumber;

    @Column(name = "age")
    private Integer age;

    @Column(name = "base_salary")
    private Double baseSalary;

    @Column(name = "marital_status")
    private String martialStatus;

    @Column(name = "fedral_allowances")
    private Integer fedralAllowances;

    @Column(name = "health_insurance")
    private Integer healthInsurance;

    @Column(name = "vision_insurance")
    private Integer visionInsurance;

    @Column(name = "retirement_401k")
    private Integer retirement401K;
}
