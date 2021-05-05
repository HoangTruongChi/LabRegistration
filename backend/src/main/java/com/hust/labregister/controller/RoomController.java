package com.hust.labregister.controller;

import com.hust.labregister.controller.bean.ResponseBean;
import com.hust.labregister.service.EquipmentService;
import com.hust.labregister.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin("*")
@RestController
@RequestMapping("/lab/room")
public class RoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    RoomService roomService;

    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public ResponseEntity getRooms() {
        ResponseBean responseBean = new ResponseBean();
        try {
            roomService.fetch(responseBean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            responseBean.setErrorCode("1");
        }
        return ResponseEntity.ok(responseBean);
    }

    @RequestMapping(value = "/fetchAvailableInRoom", method = RequestMethod.GET)
    public ResponseEntity getAvailableInRooms(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                                              @RequestParam("startTime") Double startTime,
                                              @RequestParam("endTime") Double endTime) {
        ResponseBean responseBean = new ResponseBean();
        try {
            roomService.findAvailableInRoom(responseBean, date, startTime, endTime);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            responseBean.setErrorCode("1");
        }
        return ResponseEntity.ok(responseBean);
    }
}
