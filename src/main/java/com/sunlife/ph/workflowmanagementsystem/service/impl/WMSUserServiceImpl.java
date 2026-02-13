package com.sunlife.ph.workflowmanagementsystem.service.impl;

import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import com.sunlife.ph.workflowmanagementsystem.repository.WMSUserRepository;
import com.sunlife.ph.workflowmanagementsystem.service.WMSUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WMSUserServiceImpl implements WMSUserService {

    @Autowired
    private WMSUserRepository wmsUserRepository;

    @Override
    public Page<WMSUser> getWMSUsersPaginated(Pageable pageable) {
        return wmsUserRepository.findAll(pageable);
    }



    @Override
    public List<WMSUser> findAllWMSUsers() {
        return wmsUserRepository.findAll();
    }

    @Override
    public WMSUser createWMSUser(WMSUser wmsUser) {
        System.out.println("*** Create a WMS User ***");
        return wmsUserRepository.save(wmsUser);
    }

    @Override
    public void updateWMSUser(WMSUser wmsUser) {
        wmsUserRepository.save(wmsUser);
    }

    @Override
    public void deleteWMSUser(Long id) {
        wmsUserRepository.deleteById(id);
    }

    @Override
    public Optional<WMSUser> findWMSUserById(Long id) {
        return wmsUserRepository.findById(id);
    }

    @Override
    public Optional<WMSUser> findWMSUserByIdLeft(Long id) {
        return Optional.empty();
    }

}
