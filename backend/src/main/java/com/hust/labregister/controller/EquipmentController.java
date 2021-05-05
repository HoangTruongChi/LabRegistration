package com.hust.labregister.controller;

import com.hust.labregister.controller.bean.ResponseBean;
import com.hust.labregister.model.User;
import com.hust.labregister.security.TokenAuthenticationService;
import com.hust.labregister.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/lab/equipment")
public class EquipmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentController.class);

    @Autowired
    EquipmentService equipmentService;

    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public ResponseEntity getEquipments(@RequestParam(name = "roomId", required = false) Long roomId) {
        ResponseBean responseBean = new ResponseBean();
        try {
            equipmentService.fetch(responseBean, roomId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            responseBean.setErrorCode("1");
        }
        return ResponseEntity.ok(responseBean);
    }
}
