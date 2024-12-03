package com.brigada.carsh.service;

import com.brigada.carsh.domain.fine.Fine;
import com.brigada.carsh.domain.fine.FineStatus;
import com.brigada.carsh.domain.supportticket.SupportTicket;
import com.brigada.carsh.dto.request.FineRequestDTO;
import com.brigada.carsh.dto.response.FineResponseDTO;
import com.brigada.carsh.exception.FineNotFoundException;
import com.brigada.carsh.exception.InconsistentRequestException;
import com.brigada.carsh.exception.SupportTicketNotFoundException;
import com.brigada.carsh.exception.UserNotFoundException;
import com.brigada.carsh.mapper.FineMapper;
import com.brigada.carsh.repository.FineRepository;
import com.brigada.carsh.repository.SupportTicketRepository;
import com.brigada.carsh.security.entity.User;
import com.brigada.carsh.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FineService {
    private final FineRepository fineRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;

    public List<FineResponseDTO> getAllFinesByUser(Long userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(String.format("User with id=%s was not found", userId));
        return fineRepository.findAllByUser_Id(userId).stream().map(FineMapper.INSTANCE::toResponseDTO).toList();
    }

    public FineResponseDTO getFineBySupportTicket(Long ticketId) {
        if (!supportTicketRepository.existsById(ticketId)) throw new SupportTicketNotFoundException(String.format("Support ticket with id=%s was not found", ticketId));
        Optional<Fine> optional = fineRepository.findBySupportTicket_Id(ticketId);
        return optional.map(FineMapper.INSTANCE::toResponseDTO).orElse(null);
    }

    public FineResponseDTO createFine(Long userId, Long ticketId, FineRequestDTO fineRequestDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with id=%s not found", userId)));
        Fine fine = FineMapper.INSTANCE.toEntity(fineRequestDTO);
        if (ticketId != null) {
            SupportTicket supportTicket = supportTicketRepository.findById(ticketId).orElseThrow(() -> new SupportTicketNotFoundException(String.format("Support ticket with id=%s was not found", ticketId)));
            fine.setSupportTicket(supportTicket);
        }
        fine.setUser(user);
        fine.setStatus(FineStatus.PENDING);
        fine.setIssuedDate(LocalDateTime.now());
        return FineMapper.INSTANCE.toResponseDTO(fineRepository.save(fine));
    }

    public FineResponseDTO setPaid(Long id) {
        Fine fine = fineRepository.findById(id).orElseThrow(() -> new FineNotFoundException(String.format("Fine with id=%s not found", id)));
        if (fine.getStatus() == FineStatus.PAID) {
            throw new InconsistentRequestException(String.format("Fine with id=%s already paid", id));
        }
        fine.setStatus(FineStatus.PAID);
        return FineMapper.INSTANCE.toResponseDTO(fineRepository.save(fine));
    }

    public void deleteFine(Long id) {
        fineRepository.deleteById(id);
    }
}
