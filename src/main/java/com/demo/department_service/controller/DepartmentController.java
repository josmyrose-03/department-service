package com.demo.department_service.controller;

import java.util.List;
import java.util.Optional;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.demo.department_service.dao.entity.Department;
import com.demo.department_service.pojo.DepartmentEmployeePojo;
import com.demo.department_service.pojo.EmployeePojo;
import com.demo.department_service.service.DepartmentService;

@RestController
@RequestMapping("/api")
public class DepartmentController {

    @Autowired
    DepartmentService deptService;

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments(){
        return new ResponseEntity<List<Department>>(deptService.getAllDepartments(), HttpStatus.OK);
    }
    @CircuitBreaker(name="ciremp",fallbackMethod = "fallbackEmployee")
    @GetMapping("/departments/{did}")
    public ResponseEntity<DepartmentEmployeePojo> getADepartment(@PathVariable("did") int deptId){
        RestClient restClient = RestClient.create();
        List<EmployeePojo> allEmps = restClient
                .get()
                .uri("http://employee-cntr:8082/api/employees/dept/"+deptId)
                .retrieve()
                .body(List.class);
        Optional<Department> department = deptService.getADepartment(deptId);
        DepartmentEmployeePojo deptEmpPojo = new DepartmentEmployeePojo();
        if(department.isPresent()) {
            deptEmpPojo.setDepartmentId(department.get().getDepartmentId());
            deptEmpPojo.setDepartmentName(department.get().getDepartmentName());
            deptEmpPojo.setAllEmployees(allEmps);
        }
        return new ResponseEntity<DepartmentEmployeePojo>(deptEmpPojo, HttpStatus.OK);
    }

    public ResponseEntity<DepartmentEmployeePojo> fallbackEmployee(){
        return new ResponseEntity<DepartmentEmployeePojo>(new DepartmentEmployeePojo(-1,null,null), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> addDepartment(@RequestBody Department newDept) {
        return new ResponseEntity<Department>(deptService.addDepartment(newDept),HttpStatus.OK);
    }

    @PutMapping("/departments")
    public ResponseEntity<Department> updateDepartment(@RequestBody Department editDept) {
        return new ResponseEntity<Department>(deptService.updateDepartment(editDept),HttpStatus.OK);
    }

    @DeleteMapping("/departments/{did}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("did") int deptId) {
        deptService.deleteDepartment(deptId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}