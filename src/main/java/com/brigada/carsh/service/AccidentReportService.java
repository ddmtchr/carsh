package com.brigada.carsh.service;

import com.brigada.carsh.domain.accidentreport.AccidentReport;
import com.brigada.carsh.domain.supportticket.IssueType;
import com.brigada.carsh.domain.supportticket.SupportTicket;
import com.brigada.carsh.dto.request.AccidentReportRequestDTO;
import com.brigada.carsh.dto.response.AccidentReportResponseDTO;
import com.brigada.carsh.exception.AccidentReportNotFoundException;
import com.brigada.carsh.exception.InconsistentRequestException;
import com.brigada.carsh.exception.SupportTicketNotFoundException;
import com.brigada.carsh.mapper.AccidentReportMapper;
import com.brigada.carsh.repository.AccidentReportRepository;
import com.brigada.carsh.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccidentReportService {
    private final AccidentReportRepository accidentReportRepository;
    private final SupportTicketRepository supportTicketRepository;

    public List<AccidentReportResponseDTO> getAllAccidentReports() {
        return accidentReportRepository.findAll().stream().map(AccidentReportMapper.INSTANCE::toResponseDTO).toList();
    }

    public AccidentReportResponseDTO getAccidentReportById(Long id) {
        AccidentReport accidentReport = accidentReportRepository.findById(id).orElseThrow(() -> new AccidentReportNotFoundException(String.format("Accident report with id=%s was not found", id)));
        return AccidentReportMapper.INSTANCE.toResponseDTO(accidentReport);
    }

    public AccidentReportResponseDTO createAccidentReport(AccidentReportRequestDTO requestDTO, Long ticketId) {
        SupportTicket supportTicket = supportTicketRepository.findById(ticketId).orElseThrow(() -> new SupportTicketNotFoundException(String.format("Support ticket with id=%s was not found", ticketId)));
        AccidentReport accidentReport = AccidentReportMapper.INSTANCE.toEntity(requestDTO);
        if (supportTicket.getIssueType() != IssueType.ACCIDENT) {
            throw new InconsistentRequestException(String.format("Support ticket with id=%s is not about an accident", ticketId));
        }
        accidentReport.setSupportTicket(supportTicket);
        accidentReport.setReportDate(LocalDateTime.now());
        return AccidentReportMapper.INSTANCE.toResponseDTO(accidentReportRepository.save(accidentReport));
    }

    public AccidentReportResponseDTO editAccidentReport(Long id, AccidentReportRequestDTO requestDTO) {
        AccidentReport accidentReport = accidentReportRepository.findById(id).orElseThrow(() -> new AccidentReportNotFoundException(String.format("Accident report with id=%s was not found", id)));
        AccidentReportMapper.INSTANCE.updateAccidentReport(requestDTO, accidentReport);
        return AccidentReportMapper.INSTANCE.toResponseDTO(accidentReportRepository.save(accidentReport));
    }

    public void deleteAccidentReport(Long id) {
        accidentReportRepository.deleteById(id);
    }
}
