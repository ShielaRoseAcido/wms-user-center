package com.sunlife.ph.workflowmanagementsystem;

import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import com.sunlife.ph.workflowmanagementsystem.service.WMSUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class WorkflowManagementSystemApplication implements ApplicationRunner {

	private static final Logger log =
			LoggerFactory.getLogger(WorkflowManagementSystemApplication.class);

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

				// ✅ IMPORTANT: role must not be null/blank
				// If role is an enum, replace "USER" with UserRole.USER (see note below)
				final String defaultRole = "USER";

				List<WMSUser> seedUsers = List.of(
						WMSUser.builder().employeeName("Shiela.Acido").employmentType("Contractual").role(defaultRole).build(),
						WMSUser.builder().employeeName("Xyrus.Acido").employmentType("Contractual").role(defaultRole).build(),
						WMSUser.builder().employeeName("Rohan.Acido").employmentType("Contractual").role(defaultRole).build(),
						WMSUser.builder().employeeName("Amirah.Acido").employmentType("Contractual").role(defaultRole).build()
				);

				for (WMSUser u : seedUsers) {
					try {
						wmsUserService.createWMSUser(u);
					} catch (Exception e) {
						// ✅ don’t crash container just because a seed insert failed
						log.warn("Skipping seed user {} due to: {}", u.getEmployeeName(), e.getMessage());
					}
				}

				log.info("Seed attempt completed.");
			}

		} catch (Exception e) {
			// ✅ also don’t crash container if DB is temporarily down
			log.warn("Startup seed skipped due to error:", e);
		}
	}
}