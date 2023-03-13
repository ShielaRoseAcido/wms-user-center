package com.sunlife.ph.workflowmanagementsystem.controller;

import com.sunlife.ph.workflowmanagementsystem.entity.WMSUser;
import com.sunlife.ph.workflowmanagementsystem.service.WMSUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class WMSUserController {

    @Autowired
    private WMSUserService wmsUserService;

    @GetMapping("/")
    public String getAllWMSUsers(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<WMSUser> usersPage = wmsUserService.getWMSUsersPaginated(PageRequest.of(page, 5)); // 5 = page size
        model.addAttribute("wmsUsers", usersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        return "index"; // returns index.html
    }

//    @PostMapping("/addWMSUser")
//    public String addNewWMSUser(WMSUser wmsUser){
//        WMSUser result = wmsUserService.createWMSUser(wmsUser);
//        if(result == null){
//            return "redirect:/";
//        }
//        return "redirect:/";
//    }


    @PostMapping("/addWMSUser")
    public String addNewWMSUser(@Valid @ModelAttribute("wmsUser") WMSUser wmsUser,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("wmsUser", wmsUser);
            System.out.println("Has Error=" + bindingResult.hasErrors());
            return "add-edit-wmsUser"; // Return to form if errors
        }

        wmsUserService.createWMSUser(wmsUser);
        return "redirect:/";
    }

    @RequestMapping({"/edit", "/edit/{id}"})
    public String editWMSUser(Model model, @PathVariable("id") Optional<Long> id) {
        {
            if (id.isPresent()) {
                Optional<WMSUser> wmsUser = wmsUserService.findWMSUserById(id.get());
                if (wmsUser.isPresent())
                    model.addAttribute("wmsUser", wmsUser);
            } else {
                model.addAttribute("wmsUser", new WMSUser());
            }
            return "add-edit-wmsUser";
        }
    }

    @RequestMapping("/delete/{id}")
    public String deleteWMSUser(@PathVariable("id") Long id){
        wmsUserService.deleteWMSUser(id);
        return "redirect:/";
    }
}
