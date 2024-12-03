package com.brigada.carsh.repository;

import com.brigada.carsh.domain.supportticket.SupportTicket;
import com.brigada.carsh.domain.supportticket.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findAllByStatus(TicketStatus status);
    List<SupportTicket> findAllByUser_Id(Long userId);
}
