package com.brigada.carsh.dto.request;

import com.brigada.carsh.domain.booking.BookingTariff;
import com.brigada.carsh.domain.insurance.InsuranceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingStartDTO {
    private BookingTariff tariff;
    private InsuranceType insurance;
}
