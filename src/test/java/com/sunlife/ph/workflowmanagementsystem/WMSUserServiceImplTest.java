package com.sunlife.ph.workflowmanagementsystem;

import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import com.sunlife.ph.workflowmanagementsystem.repository.WMSUserRepository;
import com.sunlife.ph.workflowmanagementsystem.service.WMSUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class WMSUserServiceImplTest {

    @Autowired
    private WMSUserService wmsUserService;

    @MockBean
    private WMSUserRepository wmsUserRepository;

    @Test
    public void addWMSUserExpectCreated(){
        WMSUser wmsUser = WMSUser.builder()
                .employeeName("Shiela.Acido")
                .employmentType("Contractual")
                .build();

        wmsUserService.createWMSUser(wmsUser);

        Mockito.verify(wmsUserRepository,Mockito.times(1)).save(wmsUser);
    }
}
