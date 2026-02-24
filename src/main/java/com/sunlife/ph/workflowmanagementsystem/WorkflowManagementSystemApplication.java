package com.sunlife.ph.workflowmanagementsystem;

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
	public void run(ApplicationArguments args) {
		try {
			List<WMSUser> wmsUsers = wmsUserService.findAllWMSUsers();

			if (wmsUsers != null && wmsUsers.isEmpty()) {

				// TODO: Replace this with your actual role value/enum
				// Example options:
				//   .role("ADMIN")
				//   .role(UserRole.ADMIN)
				//   .role("USER")
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

				System.out.println("New WMS Users added in database");
			}
		} catch (Exception e) {
			// IMPORTANT for ECS: don’t let seed failures kill the container
			System.err.println("Startup seed skipped due to error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}