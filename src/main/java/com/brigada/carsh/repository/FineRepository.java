package com.brigada.carsh.repository;

import com.brigada.carsh.domain.fine.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findAllByUser_Id(Long userId);
    Optional<Fine> findBySupportTicket_Id(Long ticketId);
}
