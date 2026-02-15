package com.sunlife.ph.workflowmanagementsystem.controller;
import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import com.sunlife.ph.workflowmanagementsystem.service.WMSUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wmsUser")
public class WMSUserRestController {

    @Autowired
    private WMSUserService wmsUserService;

    @GetMapping("/all")
    public ResponseEntity<List<WMSUser>> getAllWMSUsers(){
        List<WMSUser> wmsUsers = wmsUserService.findAllWMSUsers();
        return new ResponseEntity<>(wmsUsers, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Long> addNewWMSUser(@RequestBody WMSUser wmsUser){
        WMSUser insertedtWMSUser = wmsUserService.createWMSUser(wmsUser);

        if(insertedtWMSUser == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(insertedtWMSUser.getId(),HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity updateWMSUser(@RequestBody WMSUser wmsUser){
        wmsUserService.updateWMSUser(wmsUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteWMSUser(@PathVariable("id") Long id){
        wmsUserService.deleteWMSUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
