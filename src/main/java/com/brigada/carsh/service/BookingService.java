package com.brigada.carsh.service;

import com.brigada.carsh.domain.booking.Booking;
import com.brigada.carsh.domain.booking.BookingStatus;
import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.domain.insurance.Insurance;
import com.brigada.carsh.dto.request.BookingStartDTO;
import com.brigada.carsh.dto.response.BookingResponseDTO;
import com.brigada.carsh.exception.BookingNotFoundException;
import com.brigada.carsh.exception.CarNotFoundException;
import com.brigada.carsh.exception.InconsistentRequestException;
import com.brigada.carsh.exception.UserNotFoundException;
import com.brigada.carsh.mapper.BookingMapper;
import com.brigada.carsh.repository.BookingRepository;
import com.brigada.carsh.repository.CarRepository;
import com.brigada.carsh.repository.InsuranceRepository;
import com.brigada.carsh.security.entity.User;
import com.brigada.carsh.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final InsuranceRepository insuranceRepository;

    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id=%s was not found", id)));
        return BookingMapper.INSTANCE.toResponseDTO(booking);
    }

    public List<BookingResponseDTO> getAllBookingsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("User with id=%s was not found", userId));
        }

        return bookingRepository.findAllByUser_Id(userId).stream().map(BookingMapper.INSTANCE::toResponseDTO).toList();
    }

    public List<BookingResponseDTO> getAllBookingsByStatus(BookingStatus status) {
        return bookingRepository.findAllByStatus(status).stream().map(BookingMapper.INSTANCE::toResponseDTO).toList();
    }

    public List<BookingResponseDTO> getAllBookingsByUserAndStatus(Long userId, BookingStatus status) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("User with id=%s was not found", userId));
        }

        return bookingRepository.findAllByStatusAndUser_Id(status, userId).stream().map(BookingMapper.INSTANCE::toResponseDTO).toList();
    }

    public BookingResponseDTO bookCar(BookingStartDTO dto, Long carId, Long userId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(String.format("Car with id=%s was not found", carId)));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with id=%s was not found", userId)));
        Insurance insurance = insuranceRepository.findByInsuranceType(dto.getInsurance())
                .orElseThrow(() -> new UserNotFoundException(String.format("Insurance with type %s was not found", dto.getInsurance())));
        Booking booking = Booking.builder()
                .car(car)
                .user(user)
                .startTime(LocalDateTime.now())
                .status(BookingStatus.WAITING)
                .tariff(dto.getTariff())
                .insurance(insurance)
                .build();
        return BookingMapper.INSTANCE.toResponseDTO(bookingRepository.save(booking));
    }

    public BookingResponseDTO startBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id=%s was not found", bookingId)));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with id=%s was not found", userId)));

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be started", bookingId));
        }
        if (!Objects.equals(user.getId(), booking.getUser().getId())) {
            throw new InconsistentRequestException(String.format("Booking with id=%s was started from another user", bookingId));
        }

        booking.setStatus(BookingStatus.ACTIVE);
        return BookingMapper.INSTANCE.toResponseDTO(bookingRepository.save(booking));
    }

    public BookingResponseDTO cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id=%s was not found", bookingId)));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with id=%s was not found", userId)));

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be cancelled", bookingId));
        }
        if (!Objects.equals(user.getId(), booking.getUser().getId())) {
            throw new InconsistentRequestException(String.format("Booking with id=%s was started from another user", bookingId));
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return BookingMapper.INSTANCE.toResponseDTO(bookingRepository.save(booking));
    }

    public BookingResponseDTO endBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id=%s was not found", bookingId)));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with id=%s was not found", userId)));

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be ended", bookingId));
        }
        if (!Objects.equals(user.getId(), booking.getUser().getId())) {
            throw new InconsistentRequestException(String.format("Booking with id=%s was started from another user", bookingId));
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setEndTime(LocalDateTime.now());
        bookingRepository.save(booking);
        return BookingMapper.INSTANCE.toResponseDTO(bookingRepository.findById(booking.getId()).orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id=%s was not found", bookingId))));
    }
}
