package com.luv2code.springboot.cruddemo.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.luv2code.springboot.cruddemo.dao.EmployeeDAO;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private EmployeeService employeeService;

    private ObjectMapper objectMapper;

    //quick and dirty:inject employee dao (use constructor injection)

    @Autowired
    public EmployeeRestController(EmployeeService theEmployeeService, ObjectMapper theObjectMapper){
        employeeService = theEmployeeService;
        objectMapper = theObjectMapper;
    }


    //expose "/employees" and return a list od employees
    @GetMapping("/employees")
    public List<Employee> findAll(){

        return employeeService.findAll();
    }
    //add mapping for get /employees/{employeeId})
    @GetMapping("/employees/{employeeId}")

    public  Employee getEmployee(@PathVariable int employeeId){
        Employee theEmployee = employeeService.findById(employeeId);

        if (theEmployee == null){
            throw new RuntimeException("Employee id not found - " + employeeId);
        }

        return theEmployee;
    }
    //ADD MAPPING FOR POST /EMPLOYEE - ADD NEW EMPLOYEES
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee theEmployee){

        //also just in case they pass an id in jason ... set id to o
        //this is to force a save of new item .. instead of update

        theEmployee.setId(0);

        Employee dbEmployee = employeeService.save(theEmployee);

        return dbEmployee;
    }
    // add mapping for put /employees -update existing employees
    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee){

        Employee dbEmployee = employeeService.save(theEmployee);

        return dbEmployee;
    }

    // add mapping for delete /employees/empoyeeid
    @DeleteMapping("/employees/{employeeId}")

    public String deleteEmployee(@PathVariable int employeeId){

        Employee tempEmployee = employeeService.findById(employeeId);

        //throw exception if null
        if (tempEmployee == null){
            throw  new RuntimeException("employee id not found " + employeeId);

        }
        employeeService.deleteById(employeeId);

        return "Deleted employee id " + employeeId;
    }




    // add mapping for patch  /employees/{employeeid} -partial update

    @PatchMapping("/employees/{employeeId}")

    public Employee patchEmployee(@PathVariable int employeeId,
    @RequestBody Map<String, Object> patchPayload){
        Employee tempEmployee = employeeService.findById(employeeId);

        //throw exception if null
        if (tempEmployee == null){
            throw new RuntimeException("Employee id is not found - " + employeeId);

        }

        // throw exception  if request body contains id
        if (patchPayload.containsKey("id")){
            throw  new RuntimeException("Employee id not allowed in request body -" + employeeId);
        }
        Employee patchedEmployee = apply(patchPayload,tempEmployee);

        Employee dbEmployee = employeeService.save(patchedEmployee);
        return dbEmployee;
        
    }

    private Employee apply(Map<String, Object> patchPayload, Employee tempEmployee) {

        // Converting employee object to json object node
        ObjectNode employeeNode = objectMapper.convertValue(tempEmployee,ObjectNode.class);

        //convert the patchpayload map to a jason object node
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        //merge the patch update into the employee node
        employeeNode.setAll(patchNode);

        return objectMapper.convertValue(employeeNode,Employee.class);
    }


}