package com.sunlife.ph.workflowmanagementsystem;

import com.sunlife.ph.workflowmanagementsystem.config.UserRole;
import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import com.sunlife.ph.workflowmanagementsystem.service.WMSUserService;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkflowManagementSystemApplication implements ApplicationRunner {

	@Autowired
	private WMSUserService wmsUserService;

	public static void main(String[] args) {
		SpringApplication.run(WorkflowManagementSystemApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<WMSUser> wmsUsers = wmsUserService.findAllWMSUsers();

		if(wmsUsers != null && wmsUsers.isEmpty()) {
			WMSUser wmsUser1 = WMSUser.builder().employeeName("Shiela.Acido").employmentType("Contractual").role(UserRole.Admin.name()).build();
			WMSUser wmsUser2 = WMSUser.builder().employeeName("Xyrus.Acido").employmentType("Contractual").role(UserRole.Admin.name()).build();
			WMSUser wmsUser3 = WMSUser.builder().employeeName("Rohan.Acido").employmentType("Contractual").role(UserRole.Admin.name()).build();
			WMSUser wmsUser4 = WMSUser.builder().employeeName("Amirah.Acido").employmentType("Contractual").role(UserRole.Admin.name()).build();

			Arrays.asList(wmsUser1, wmsUser2, wmsUser3, wmsUser4).forEach(b -> wmsUserService.createWMSUser(b));

			System.out.println("New WMS User added in database");
		}
	}

}
