package com.offsidegaming.measurer.repository;

import com.offsidegaming.measurer.entity.Measurement;
import com.offsidegaming.measurer.entity.MeasurementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long>,
        QuerydslPredicateExecutor<Measurement> {

    @Query("select m.id from Measurement m where (m.endDate >= ?1 AND m.startDate <= ?2) and m.username=?3 and m.measurementType=?4")
    List<Long> findAllWithConflictDatesForGivenUsernameAndMeasurements(LocalDate startDate,
                                                                       LocalDate endDate,
                                                                       String username,
                                                                       MeasurementType measurementType);
}
