package com.hba.reservationservice.web;

import com.hba.reservationservice.domain.employee.Department;
import com.hba.reservationservice.domain.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

  private final EmployeeService employeeService;

  @GetMapping("/department")
  List<Department> getDepartments() {
    log.info("Getting department info");
    return employeeService.getDepartments();
  }
}