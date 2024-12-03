package com.brigada.carsh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccidentReportRequestDTO {
    private String reportDetails;
    private Boolean isGuilty;
}
