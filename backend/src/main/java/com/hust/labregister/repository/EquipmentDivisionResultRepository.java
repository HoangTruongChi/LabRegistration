package com.hust.labregister.repository;


import com.hust.labregister.model.EquipmentDivisionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentDivisionResultRepository extends JpaRepository<EquipmentDivisionResult, Long> {
}
