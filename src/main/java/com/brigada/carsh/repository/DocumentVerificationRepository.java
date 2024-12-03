package com.brigada.carsh.repository;

import com.brigada.carsh.domain.documentverification.DocumentVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, Long> {
    List<DocumentVerification> findAllByUser_Id(Long userId);
}
