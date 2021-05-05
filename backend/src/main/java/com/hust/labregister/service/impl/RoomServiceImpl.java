package com.hust.labregister.service.impl;

import com.hust.labregister.controller.bean.ResponseBean;
import com.hust.labregister.model.Room;
import com.hust.labregister.repository.RoomRepository;
import com.hust.labregister.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Override
    public void fetch(ResponseBean bean) throws Exception {
        List<Room> rooms = roomRepository.findAll();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("rooms", rooms);
        bean.setErrorCode("0");
        bean.setData(data);
        bean.setMsg("Successfully !");
    }

    @Override
    public void findAvailableInRoom(ResponseBean bean, Date date, Double startTime, Double endTime) throws Exception {
        List<Room> rooms = roomRepository.findAvailableInRoom(date, startTime, endTime);
        bean.setErrorCode("0");
        bean.setMsg("Successfully !");
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("rooms", rooms);
        bean.setData(data);
    }
}
