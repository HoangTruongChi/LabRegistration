package com.hust.labregister.repository;


import com.hust.labregister.model.RoomDivisionResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomDivisionResultsRepository extends JpaRepository<RoomDivisionResults, String> {
}
