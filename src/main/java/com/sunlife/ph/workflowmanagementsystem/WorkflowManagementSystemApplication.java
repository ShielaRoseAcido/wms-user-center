package com.sunlife.ph.workflowmanagementsystem;

import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import com.sunlife.ph.workflowmanagementsystem.service.WMSUserService;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkflowManagementSystemApplication implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(WorkflowManagementSystemApplication.class);

	private final WMSUserService wmsUserService;

	public WorkflowManagementSystemApplication(WMSUserService wmsUserService) {
		this.wmsUserService = wmsUserService;
	}

	public static void main(String[] args) {
		SpringApplication.run(WorkflowManagementSystemApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		try {
			List<WMSUser> wmsUsers = wmsUserService.findAllWMSUsers();

			if (wmsUsers != null && wmsUsers.isEmpty()) {

				// IMPORTANT: set role because validation requires it
				// If role is an enum in your entity, change this to UserRole.USER (or your enum)
				final String defaultRole = "USER";

				WMSUser wmsUser1 = WMSUser.builder()
						.employeeName("Shiela.Acido")
						.employmentType("Contractual")
						.role(defaultRole)
						.build();

				WMSUser wmsUser2 = WMSUser.builder()
						.employeeName("Xyrus.Acido")
						.employmentType("Contractual")
						.role(defaultRole)
						.build();

				WMSUser wmsUser3 = WMSUser.builder()
						.employeeName("Rohan.Acido")
						.employmentType("Contractual")
						.role(defaultRole)
						.build();

				WMSUser wmsUser4 = WMSUser.builder()
						.employeeName("Amirah.Acido")
						.employmentType("Contractual")
						.role(defaultRole)
						.build();

				Arrays.asList(wmsUser1, wmsUser2, wmsUser3, wmsUser4)
						.forEach(wmsUserService::createWMSUser);

				log.info("New WMS Users added in database");
			}
		} catch (Exception e) {
			// Do not crash ECS on seed failure
			log.warn("Startup seed skipped", e);
		}
	}
}