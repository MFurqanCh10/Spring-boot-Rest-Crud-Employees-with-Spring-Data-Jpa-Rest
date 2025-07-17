package com.luv2code.springboot.cruddemo.repository;

import com.luv2code.springboot.cruddemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//change the endpoint
//@RepositoryRestResource(path="members")
public interface EmployeeRepository  extends JpaRepository<Employee, Integer> {

    // thats it ... no need to write any code lol!
}
