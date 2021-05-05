package com.hust.labregister.service;

import com.hust.labregister.controller.bean.ResponseBean;

import java.util.Date;

public interface RoomService {
    void fetch(ResponseBean bean) throws Exception;

    void findAvailableInRoom(ResponseBean bean, Date date, Double startTime, Double endTime) throws Exception;

}
