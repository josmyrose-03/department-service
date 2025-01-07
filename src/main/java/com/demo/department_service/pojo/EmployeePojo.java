package com.demo.department_service.pojo;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeePojo {
    private int empId;
    private String empName;
    private String empDesignation;
    private int empDeptId;
}