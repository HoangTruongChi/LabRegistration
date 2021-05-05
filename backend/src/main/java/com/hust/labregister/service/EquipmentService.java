package com.hust.labregister.service;

import com.hust.labregister.controller.bean.ResponseBean;

public interface EquipmentService {

    void fetch(ResponseBean bean, Long roomId) throws Exception;
}
