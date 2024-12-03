package com.brigada.carsh.service;

import com.brigada.carsh.domain.booking.Booking;
import com.brigada.carsh.domain.supportticket.SupportTicket;
import com.brigada.carsh.domain.supportticket.TicketStatus;
import com.brigada.carsh.dto.request.SupportTicketRequestDTO;
import com.brigada.carsh.dto.response.SupportTicketResponseDTO;
import com.brigada.carsh.exception.BookingNotFoundException;
import com.brigada.carsh.exception.InconsistentRequestException;
import com.brigada.carsh.exception.SupportTicketNotFoundException;
import com.brigada.carsh.exception.UserNotFoundException;
import com.brigada.carsh.mapper.SupportTicketMapper;
import com.brigada.carsh.repository.BookingRepository;
import com.brigada.carsh.repository.SupportTicketRepository;
import com.brigada.carsh.security.entity.User;
import com.brigada.carsh.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketService {
    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public List<SupportTicketResponseDTO> getAllSupportTickets() {
        return supportTicketRepository.findAll().stream().map(SupportTicketMapper.INSTANCE::toResponseDTO).toList();
    }

    public List<SupportTicketResponseDTO> getAllSupportTicketsByStatus(TicketStatus status) {
        return supportTicketRepository.findAllByStatus(status).stream().map(SupportTicketMapper.INSTANCE::toResponseDTO).toList();
    }

    public List<SupportTicketResponseDTO> getAllSupportTicketsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("User with id=%s was not found", userId));
        }
        return supportTicketRepository.findAllByUser_Id(userId).stream().map(SupportTicketMapper.INSTANCE::toResponseDTO).toList();
    }

    public SupportTicketResponseDTO getSupportTicketById(Long id) {
        SupportTicket supportTicket = supportTicketRepository.findById(id).orElseThrow(() -> new SupportTicketNotFoundException(String.format("Support ticket with id=%s was not found", id)));
        return SupportTicketMapper.INSTANCE.toResponseDTO(supportTicket);
    }

    public SupportTicketResponseDTO createSupportTicket(SupportTicketRequestDTO supportTicketRequestDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with id=%s was not found", userId)));
        Booking booking = null;
        if (supportTicketRequestDTO.getBookingId() != null) {
            booking = bookingRepository.findById(supportTicketRequestDTO.getBookingId()).orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id=%s was not found", supportTicketRequestDTO.getBookingId())));
        }
        SupportTicket supportTicket = SupportTicketMapper.INSTANCE.toEntity(supportTicketRequestDTO);

        supportTicket.setCreatedAt(LocalDateTime.now());
        supportTicket.setStatus(TicketStatus.OPEN);
        supportTicket.setUser(user);
        if (booking != null) {
            supportTicket.setBooking(booking);
        }
        return SupportTicketMapper.INSTANCE.toResponseDTO(supportTicketRepository.save(supportTicket));
    }

    public SupportTicketResponseDTO acceptSupportTicket(Long id) {
        SupportTicket supportTicket = supportTicketRepository.findById(id).orElseThrow(() -> new SupportTicketNotFoundException(String.format("Support ticket with id=%s was not found", id)));
        if (supportTicket.getStatus() != TicketStatus.OPEN) {
            throw new InconsistentRequestException(String.format("Ticket with id=%s cannot be accepted", id));
        }
        supportTicket.setStatus(TicketStatus.IN_PROGRESS);
        return SupportTicketMapper.INSTANCE.toResponseDTO(supportTicketRepository.save(supportTicket));
    }

    public SupportTicketResponseDTO closeSupportTicket(Long id) {
        SupportTicket supportTicket = supportTicketRepository.findById(id).orElseThrow(() -> new SupportTicketNotFoundException(String.format("Support ticket with id=%s was not found", id)));
        if (supportTicket.getStatus() == TicketStatus.CLOSED) {
            throw new InconsistentRequestException(String.format("Ticket with id=%s cannot be closed", id));
        }
        supportTicket.setStatus(TicketStatus.CLOSED);
        return SupportTicketMapper.INSTANCE.toResponseDTO(supportTicketRepository.save(supportTicket));
    }

    public void deleteSupportTicket(Long id) {
        supportTicketRepository.deleteById(id);
    }
}
