package com.demo.department_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.department_service.dao.DepartmentRepository;
import com.demo.department_service.dao.entity.Department;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository deptRepo;

    public List<Department> getAllDepartments(){
        return deptRepo.findAll();
    }

    public Optional<Department> getADepartment(int deptId){
        return deptRepo.findById(deptId);
    }

    public Department addDepartment(Department newDept) {
        return deptRepo.saveAndFlush(newDept);
    }

    public Department updateDepartment(Department editDept) {
        return deptRepo.save(editDept);
    }

    public void deleteDepartment(int deptId) {
        deptRepo.deleteById(deptId);
    }
}