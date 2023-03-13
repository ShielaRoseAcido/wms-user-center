package com.sunlife.ph.workflowmanagementsystem;

import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import com.sunlife.ph.workflowmanagementsystem.service.WMSUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class WMSUserRepositoryTest {

    @Autowired
    private WMSUserService wmsUserService;
    @Test
    public void GivenCreateWMSUserThenExpectSameWMSUser(){
        WMSUser wmsUser = WMSUser.builder()
                .employeeName("Shiela.Acido")
                .employmentType("Contractual")
                .build();

        WMSUser actual = wmsUserService.createWMSUser(wmsUser);

        assertEquals(wmsUser.getEmployeeName(),actual.getEmployeeName());
        assertEquals(wmsUser.getEmploymentType(),actual.getEmploymentType());

    }
/*
    @Test
    public void testGetEmployeeById() {
          when(employeeService.getById(1)).thenReturn(new Employee(1, "John"));
        Employee emp = employeeController.getEmployee(1);
        assertEquals("John", emp.getName());
    }*/

}
