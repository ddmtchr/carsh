package com.brigada.carsh.repository;

import com.brigada.carsh.domain.insurance.Insurance;
import com.brigada.carsh.domain.insurance.InsuranceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    Optional<Insurance> findByInsuranceType(InsuranceType type);
}
