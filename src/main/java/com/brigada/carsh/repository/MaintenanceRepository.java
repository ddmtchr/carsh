package com.brigada.carsh.repository;

import com.brigada.carsh.domain.maintenance.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findAllByCarId(Long carId);
}
