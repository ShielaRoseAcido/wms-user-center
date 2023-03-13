package com.sunlife.ph.workflowmanagementsystem.repository;

import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WMSUserRepository extends JpaRepository<WMSUser,Long> {
}
