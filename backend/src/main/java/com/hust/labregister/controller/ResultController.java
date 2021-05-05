package com.hust.labregister.controller;

import com.hust.labregister.controller.bean.ResponseBean;
import com.hust.labregister.model.User;
import com.hust.labregister.repository.UserRepository;
import com.hust.labregister.security.TokenAuthenticationService;
import com.hust.labregister.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/lab/result")
public class ResultController {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity viewResult(@RequestParam("id") String regisId,  HttpServletRequest servletRequest){
        ResponseBean responseBean = new ResponseBean();
        try {
            Authentication authentication = TokenAuthenticationService.getAuthentication(servletRequest);
            String email = authentication.getPrincipal().toString();
            User user = userRepository.findByEmail(email);
            registrationService.viewResult(responseBean, user.getId(), regisId);
        } catch (Exception e) {
            responseBean.setErrorCode("1");
        }
        return ResponseEntity.ok(responseBean);
    }
}
