package com.hust.labregister.service.impl;

import com.hust.labregister.controller.bean.ResponseBean;
import com.hust.labregister.model.Equipment;
import com.hust.labregister.repository.EquipmentOfRoomRepository;
import com.hust.labregister.repository.EquipmentRepository;
import com.hust.labregister.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    EquipmentOfRoomRepository equipmentOfRoomRepository;

    @Override
    public void fetch(ResponseBean bean, Long roomId) throws Exception {
        List equipments = null;
        if(roomId == null) {
         equipments = equipmentRepository.findAll();
        }else{
            equipments = equipmentOfRoomRepository.findByRoomId(roomId);
        }
        bean.setErrorCode("0");
        bean.setMsg("Successfully !");
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("equiments", equipments);
        bean.setData(data);
    }
}
