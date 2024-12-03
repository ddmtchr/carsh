package com.brigada.carsh.controller;

import com.brigada.carsh.dto.request.AccidentReportRequestDTO;
import com.brigada.carsh.dto.response.AccidentReportResponseDTO;
import com.brigada.carsh.service.AccidentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accident-reports")
@RequiredArgsConstructor
public class AccidentReportController {
    private final AccidentReportService accidentReportService;

    @GetMapping
    public ResponseEntity<List<AccidentReportResponseDTO>> getAllAccidentReports() {
        return ResponseEntity.ok(accidentReportService.getAllAccidentReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccidentReportResponseDTO> getAccidentReportById(@PathVariable Long id) {
        return ResponseEntity.ok(accidentReportService.getAccidentReportById(id));
    }

    @PostMapping
    public ResponseEntity<AccidentReportResponseDTO> createAccidentReport(@RequestParam Long ticketId, @RequestBody AccidentReportRequestDTO accidentReportRequestDTO) {
        return new ResponseEntity<>(accidentReportService.createAccidentReport(accidentReportRequestDTO, ticketId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccidentReportResponseDTO> editAccidentReport(@PathVariable Long id, @RequestBody AccidentReportRequestDTO accidentReportRequestDTO) {
        return ResponseEntity.ok(accidentReportService.editAccidentReport(id, accidentReportRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccidentReport(@PathVariable Long id) {
        accidentReportService.deleteAccidentReport(id);
        return ResponseEntity.noContent().build();
    }
}
