package com.brigada.carsh.repository;

import com.brigada.carsh.domain.accidentreport.AccidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccidentReportRepository extends JpaRepository<AccidentReport, Long> {
}
