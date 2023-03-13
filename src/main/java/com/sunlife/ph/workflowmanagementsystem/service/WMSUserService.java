package com.sunlife.ph.workflowmanagementsystem.service;
import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WMSUserService {
    Page<WMSUser> getWMSUsersPaginated(Pageable pageable);

    List<WMSUser> findAllWMSUsers();

    WMSUser createWMSUser(WMSUser wmsUser);

    void updateWMSUser(WMSUser wmsUser);

    void deleteWMSUser(Long id);

    Optional<WMSUser> findWMSUserById(Long id);

    @Query("SELECT u FROM WMSUser u LEFT JOIN u.roles r WHERE u.id = :id")
    Optional<WMSUser> findWMSUserByIdLeft(Long id);



}
