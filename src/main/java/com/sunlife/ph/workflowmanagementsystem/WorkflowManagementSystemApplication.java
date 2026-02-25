package com.sunlife.ph.workflowmanagementsystem;

import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import com.sunlife.ph.workflowmanagementsystem.service.WMSUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class WorkflowManagementSystemApplication implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(WorkflowManagementSystemApplication.class);

	private final WMSUserService wmsUserService;

	@Value("${app.seed.enabled:false}")
	private boolean seedEnabled;

	public WorkflowManagementSystemApplication(WMSUserService wmsUserService) {
		this.wmsUserService = wmsUserService;
	}

	public static void main(String[] args) {
		SpringApplication.run(WorkflowManagementSystemApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		if (!seedEnabled) {
			log.info("Startup seed is disabled (app.seed.enabled=false).");
			return;
		}

		try {
			List<WMSUser> wmsUsers = wmsUserService.findAllWMSUsers();
			if (wmsUsers != null && wmsUsers.isEmpty()) {

				// ✅ IMPORTANT: set role (adjust based on your role type)
				// If role is String:
				final String defaultRole = "USER";

				WMSUser w1 = WMSUser.builder().employeeName("Shiela.Acido").employmentType("Contractual").role(defaultRole).build();
				WMSUser w2 = WMSUser.builder().employeeName("Xyrus.Acido").employmentType("Contractual").role(defaultRole).build();
				WMSUser w3 = WMSUser.builder().employeeName("Rohan.Acido").employmentType("Contractual").role(defaultRole).build();
				WMSUser w4 = WMSUser.builder().employeeName("Amirah.Acido").employmentType("Contractual").role(defaultRole).build();

				Arrays.asList(w1, w2, w3, w4).forEach(wmsUserService::createWMSUser);
				log.info("Seeded initial WMS users.");
			}
		} catch (Exception e) {
			// ✅ Don’t kill ECS task
			log.warn("Startup seed failed. App will continue running.", e);
		}
	}
}