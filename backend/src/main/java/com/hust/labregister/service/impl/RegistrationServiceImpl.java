package com.hust.labregister.service.impl;

import com.hust.labregister.controller.bean.RegisterRequest;
import com.hust.labregister.controller.bean.ResponseBean;
import com.hust.labregister.model.*;
import com.hust.labregister.repository.*;
import com.hust.labregister.service.RegistrationService;
import com.hust.labregister.util.Constants;
import com.hust.labregister.util.SendMailFail;
import com.hust.labregister.util.SendMailSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Autowired
    RoomRegistrationRepository roomRegistrationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    EquipmentRegistrationRepository equipmentRegistrationRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomDivisionResultsRepository roomDivisionResultsRepository;

    @Autowired
    EquipmentDivisionResultRepository equipmentDivisionResultRepository;

    @Override
    public void register(ResponseBean bean, RegisterRequest request, Authentication authentication) throws Exception {

        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmail(email);
        Date now = new Date();

        if(request.getId() != null && !"".equals(request.getId())){
            RoomRegistration registration = roomRegistrationRepository.findById(request.getId()).orElse(null);
            if(registration != null){
                registration.setDate(request.getDate());
                registration.setEndTime(request.getEndTime());
                registration.setStartTime(request.getStartTime());
                registration.setRoomType(request.getRoomType());
                registration.setGroupSize(request.getGroupSize());
                registration.setUser(user);
                registration.setStatus(2);
                registration.setUpdatedAt(now);

                if(request.getEquipmentId() != null) {
                    List<EquipmentRegistration> equipmentRegistrations = registration.getEquipmentRegistration();

                    Equipment equipment = equipmentRepository.findById(request.getEquipmentId()).orElse(null);
                    EquipmentRegistration equipmentRegistration = new EquipmentRegistration();
                    equipmentRegistration.setEquipment(equipment);
                    equipmentRegistration.setAmount(request.getNumOfEquip());
                    equipmentRegistration.setRoomRegistration(registration);
                    equipmentRegistration.setCreatedAt(now);
                    equipmentRegistration.setUpdatedAt(now);

                    equipmentRegistrations.add(equipmentRegistration);

                    registration.setEquipmentRegistration(equipmentRegistrations);
                }else{
                    registration.setEquipmentRegistration(null);
                }

                roomRegistrationRepository.save(registration);

                bean.setErrorCode("0");
                bean.setMsg("Bạn đã cập nhật đăng kí thành công ! Vui lòng chờ email của hệ thống !");

            }else{
                bean.setErrorCode("1");
                bean.setMsg("Đăng kí không tồn tại !");
            }

            return;
        }

        List<RoomRegistration> existRegistration = roomRegistrationRepository.findByStatusAndUserId(user.getId());
        if(existRegistration.isEmpty()) {

            RoomRegistration roomRegistration = new RoomRegistration();
            roomRegistration.setId(UUID.randomUUID().toString());
            roomRegistration.setDate(request.getDate());
            roomRegistration.setEndTime(request.getEndTime());
            roomRegistration.setStartTime(request.getStartTime());
            roomRegistration.setRoomType(request.getRoomType());
            roomRegistration.setGroupSize(request.getGroupSize());
            roomRegistration.setUser(user);
            roomRegistration.setStatus(2);
            roomRegistration.setCreatedAt(now);
            roomRegistration.setUpdatedAt(now);

            if(request.getEquipmentId() != null) {
                Equipment equipment = equipmentRepository.findById(request.getEquipmentId()).orElse(null);
                EquipmentRegistration equipmentRegistration = new EquipmentRegistration();
                equipmentRegistration.setEquipment(equipment);
                equipmentRegistration.setAmount(request.getNumOfEquip());
                equipmentRegistration.setRoomRegistration(roomRegistration);
                equipmentRegistration.setCreatedAt(now);
                equipmentRegistration.setUpdatedAt(now);

                roomRegistration.setEquipmentRegistration(Arrays.asList(equipmentRegistration));
            }

            roomRegistrationRepository.save(roomRegistration);

            bean.setErrorCode("0");
            bean.setMsg("Bạn đã đăng kí thành công ! Vui lòng chờ email của hệ thống !");
        }else{
            bean.setErrorCode("1");
            bean.setMsg("Bạn đang có đăng kí chưa hoàn tất !");
        }
    }

    @Override
    public void viewByUserId(ResponseBean bean, Long userId) throws Exception {
        List<RoomRegistration> registrations = roomRegistrationRepository.findByUserId(userId);
        registrations = registrations.stream().map(e -> {
            e.setUser(null);
            return e;
        }).collect(Collectors.toList());
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("registrations", registrations);
        bean.setErrorCode("0");
        bean.setData(data);
        bean.setMsg("Successfully !");
    }

    @Override
    public void viewResult(ResponseBean responseBean, Long id, String regisId) throws Exception {
        RoomDivisionResults results = roomDivisionResultsRepository.findById(regisId).orElse(null);
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("result", results);
        if(results.getEquipmentDivisionResults().size()>0) {
            data.put("equip", results.getEquipmentDivisionResults().get(0));
        }else{
            data.put("equip", null);
        }
        responseBean.setErrorCode("0");
        responseBean.setData(data);
        responseBean.setMsg("Successfully !");
    }

    @Override
    public void cancel(ResponseBean responseBean, String id, Authentication authentication) throws Exception {
        RoomRegistration registration = roomRegistrationRepository.findById(id).orElse(null);
        if(registration != null) {
            registration.setStatus(-1);
            roomRegistrationRepository.save(registration);
            responseBean.setErrorCode("0");
            responseBean.setMsg("Bạn đã hủy đăng kí thành công !");
        }else{
            responseBean.setErrorCode("1");
            responseBean.setMsg("Đăng kí không tồn tại !");
        }
    }

    @Scheduled(cron = "1 * * * * *")
    //@Scheduled(cron = "0 0 * * SUN")
    public void handleRegistration(){
        LOGGER.info("Start handle register lab");
        //Lấy tất cả danh sách đăng kí ưu tiên ADMIN và thời gian
        List<RoomRegistration> registrations = roomRegistrationRepository.findUnHandle();
        LOGGER.info("Got " + registrations.size() + " registrations");
        //Xử lý từng đăng ký
        for(RoomRegistration registration : registrations){
            LOGGER.info("Start registrationId: " + registration.getId() + ", roomType: " + registration.getRoomType());
            if(registration.getRoomType() == Constants.TYPE_1){
                List<Room> rooms = roomRepository.findRoomAvailableType1(registration.getGroupSize(), registration.getDate(), registration.getStartTime(), registration.getEndTime());
                division(registration, rooms);
            }else if(registration.getRoomType() == Constants.TYPE_2){
                List<Room> rooms = roomRepository.findRoomAvailableType2(registration.getGroupSize(), registration.getDate(),
                        registration.getStartTime(), registration.getEndTime(), registration.getEquipmentRegistration().get(0).getEquipment().getId(),
                        registration.getEquipmentRegistration().get(0).getAmount());
                division(registration, rooms);
            }
        }
    }

    private void division(RoomRegistration registration, List<Room> rooms) {
        if(rooms != null && rooms.size() > 0){
            Room room = rooms.get(0);
            LOGGER.info("Found available room: " + room.getCode());
            RoomDivisionResults results = new RoomDivisionResults();
            results.setRoomRegisId(registration.getId());
            results.setRoom(room);
            results.setDate(registration.getDate());
            results.setEndTime(registration.getEndTime());
            results.setStartTime(registration.getStartTime());
            results.setStatus(1);
            results.setCreatedAt(new Date());
            results.setUpdatedAt(new Date());
            results.setGroupSize(registration.getGroupSize());

            // Luu ket qua chi phong
            roomDivisionResultsRepository.save(results);
            LOGGER.info("Saved result registrationId: " + registration.getId());

            // Luu ket qua thiet bi
            List<EquipmentRegistration> equipmentRegistrations = registration.getEquipmentRegistration();
            if(equipmentRegistrations != null && equipmentRegistrations.size() > 0){
                EquipmentRegistration equipmentRegistration = equipmentRegistrations.get(0);

                EquipmentDivisionResult equipmentDivisionResult = new EquipmentDivisionResult();
                equipmentDivisionResult.setEquipment(equipmentRegistration.getEquipment());
                equipmentDivisionResult.setAmount(equipmentRegistration.getAmount());
                equipmentDivisionResult.setCreatedAt(new Date());
                equipmentDivisionResult.setRoomDivisionResults(results);
                equipmentDivisionResult.setStatus(1);
                equipmentDivisionResult.setUpdatedAt(new Date());

                equipmentDivisionResultRepository.save(equipmentDivisionResult);
            }

            // Cap nhat thong tin bang dang ki
            registration.setStatus(1);

            // Gui mail thong bao dang ki thanh cong
            SendMailSuccess.send(results, registration.getUser());
        }else{
            LOGGER.info("Cannot find available room");
            registration.setStatus(0);
            // Khong co phong phu hop
            // Thong bao dang ki khong thanh cong

            // Gui mail thong bao dang ki that bai
            SendMailFail.send(registration.getUser(), registration);
        }
        roomRegistrationRepository.save(registration);
        LOGGER.info("Update info registration: " + registration.getId());
        LOGGER.info("End registrationId: " + registration.getId());
    }
}
