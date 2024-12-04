-- Таблица users
INSERT INTO users (username, role, email, password, name, last_name, phone, experience, birth_date, rating, payment_card) VALUES
('john_doe', 'ROLE_USER', 'john.doe@example.com', '$2a$10$m3Of7zuzdw7lrVCeCYCp7OlFQ5fL5reHAynTvPnRNkMksFBFRvT0y', 'John', 'Doe', '123456789012', 5, '1985-02-20', 4.7, '4111111111111111'),
('jane_smith', 'ROLE_ADMIN', 'jane.smith@example.com', '$2a$10$VefyLRIkTLiS8QNc6kBIX.1q.LuoQ6RlmerhY22tyIKRNUjYaSnlm', 'Jane', 'Smith', '234567890123', 3, '1990-06-15', 4.9, '4222222222222222'),
('sam_brown', 'ROLE_ACCIDENT_COMMISSAR', 'sam.brown@example.com', '$2a$10$04CKsa2KnKn8bA.fV/PUwuNMnyqOvFCFaRrW1Sxo4R5vYlrR7SmAu', 'Sam', 'Brown', '345678901234', 2, '1988-09-30', 5.0, '4333333333333333'),
('lucas_white', 'ROLE_USER', 'lucas.white@example.com', '$2a$10$L0JvHTMH9F7W/yPeVeyYMeB5wSOCYAmt3pQZKia7voD8r7vZwtGf6', 'Lucas', 'White', '456789012345', 7, '1982-01-10', 4.5, '4444444444444444'),
('alice_green', 'ROLE_USER', 'alice.green@example.com', '$2a$10$N.CnWoS5E75FSRXrbuUPGOgQ0cAHCCbkilwfrOtliD7sIWbroBiim', 'Alice', 'Green', '567890123456', 4, '1993-03-25', 4.8, '4555555555555555');

-- Таблица location
INSERT INTO location (latitude, longitude) VALUES
(59.882700, 30.156774),
(59.994416, 30.293833),
(60.020413, 30.371516),
(60.035076, 30.353419),
(59.969769, 30.356828),
(60.075946, 30.223966),
(60.080816, 30.300947),
(59.963905, 30.391251);

-- Таблица insurance
INSERT INTO insurance (insurance_type, coverage, cost_ratio) VALUES
('OSAGO', 'Covers third-party liability', 1.05),
('KASKO', 'Almost full coverage', 1.15);

-- Таблица car
INSERT INTO car (registration_number, model, car_class, fuel_level, minute_price, location_id, status) VALUES
('E333EE763', 'Toyota Prius', 'COMFORT', 80.0, 21.02, 1, 'AVAILABLE'),
('O787KP797', 'BMW X5', 'ELITE', 65.0, 49.81, 2, 'MAINTENANCE'),
('T166XO799', 'Renault Kaptur', 'ECONOMY', 90.0, 29.39, 3, 'AVAILABLE'),
('A547BT178', 'Mercedes C-Class', 'BUSINESS', 55.0, 38.83, 4, 'RENTED'),
('X228YE198', 'Lada 2107', 'SUPER_ECONOMY', 75.0, 15.1, 5, 'AVAILABLE'),
('B004KO23', 'Lada 2106', 'SUPER_ECONOMY', 15.0, 16.2, 6, 'AVAILABLE'),
('T888TT09', 'Lada 2102', 'SUPER_ECONOMY', 34.3, 14.35, 7, 'AVAILABLE');

-- Таблица booking
INSERT INTO booking (user_id, car_id, start_time, end_time, status, tariff, rental_cost, distance, start_location_id, end_location_id, insurance_id) VALUES
(1, 1, '2024-11-01 10:00:00', '2024-11-01 11:30:00', 'COMPLETED', 'MINUTES', NULL, 12.5, 1, 2, 1),
(2, 3, '2024-11-02 15:00:00', NULL, 'ACTIVE', 'HOURS', NULL, NULL, 3, NULL, 2),
(3, 7, '2024-11-03 09:00:00', NULL, 'ACTIVE', 'FIX', NULL, NULL, 2, NULL, 1),
(4, 6, '2024-11-04 14:30:00', '2024-11-04 16:30:00', 'COMPLETED', 'HOURS', NULL, 18.0, 4, 5, 2),
(5, 5, '2024-11-05 12:00:00', NULL, 'ACTIVE', 'MINUTES', NULL, NULL, 7, NULL, 1),
(4, 1, '2024-11-05 12:00:00', NULL, 'ACTIVE', 'MINUTES', NULL, NULL, 2, NULL, 2);

-- Таблица payment
INSERT INTO payment (booking_id, amount, payment_date) VALUES
(1, 25.50, '2024-11-01 12:00:00'),
(2, 15.75, '2024-11-02 16:00:00'),
(3, 45.00, '2024-11-03 10:00:00'),
(4, 35.50, '2024-11-04 17:00:00'),
(5, 20.00, '2024-11-05 13:00:00'),
(6, 22.22, '2024-11-05 13:15:00');

-- Таблица feedback
INSERT INTO feedback (booking_id, rating, comment, date) VALUES
(1, 4.5, 'Smooth ride, clean car', '2024-11-01 13:00:00');

-- Таблица support_ticket
INSERT INTO support_ticket (user_id, booking_id, issue_type, description, status, created_at) VALUES
(1, 1, 'ACCOUNT', 'Issue with payment details', 'OPEN', '2024-11-01 12:00:00'),
(2, 2, 'COMPENSATION', 'Car was not clean', 'IN_PROGRESS', '2024-11-02 18:00:00'),
(3, 3, 'ACCIDENT', 'Minor accident during rental', 'CLOSED', '2024-11-03 12:00:00'),
(4, 4, 'FINES', 'Received an unjustified fine', 'OPEN', '2024-11-04 18:30:00'),
(5, 5, 'RENT', 'Unable to start the car', 'IN_PROGRESS', '2024-11-05 14:30:00');

-- Таблица fines
INSERT INTO fine (user_id, support_ticket_id, amount, issued_date, status, reason) VALUES
(1, null, 50.00, '2024-11-01 11:00:00', 'PENDING', 'Speeding'),
(2, null, 30.00, '2024-11-02 15:30:00', 'PAID', 'Parking violation'),
(3, 3, 45.00, '2024-11-03 09:30:00', 'PENDING', 'Traffic signal violation'),
(4, 2, 25.00, '2024-11-04 14:45:00', 'PAID', 'Improper lane change'),
(5, null, 40.00, '2024-11-05 12:15:00', 'PENDING', 'Not wearing seatbelt');

-- Таблица accident_report
INSERT INTO accident_report (ticket_id, report_date, report_details, is_guilty) VALUES
(3, '2024-11-03 12:00:00', 'Minor dent on front bumper', TRUE);

-- Таблица maintenance
INSERT INTO maintenance (car_id, start_date, end_date, maintenance_type, status) VALUES
(2, '2024-11-01 09:00:00', '2024-11-02 17:00:00', 'Routine check', 'COMPLETED'),
(3, '2024-11-04 10:00:00', NULL, 'Tire replacement', 'PENDING'),
(4, '2024-11-05 08:00:00', '2024-11-05 15:00:00', 'Oil change', 'COMPLETED'),
(5, '2024-11-06 12:00:00', NULL, 'Brake inspection', 'PENDING');

-- Таблица document_verification
INSERT INTO document_verification (user_id, document_type, status, verification_date) VALUES
(1, 'PASSPORT', 'VERIFIED', '2024-11-01 10:00:00'),
(2, 'DRIVING_LICENSE', 'PENDING', NULL),
(3, 'PASSPORT', 'REJECTED', '2024-11-02 14:00:00'),
(4, 'DRIVING_LICENSE', 'VERIFIED', '2024-11-03 15:30:00'),
(5, 'PASSPORT', 'VERIFIED', '2024-11-04 11:00:00');
