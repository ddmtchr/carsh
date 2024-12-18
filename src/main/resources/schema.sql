DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS accident_report CASCADE;
DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS car CASCADE;
DROP TABLE IF EXISTS document_verification CASCADE;
DROP TABLE IF EXISTS feedback CASCADE;
DROP TABLE IF EXISTS fine CASCADE;
DROP TABLE IF EXISTS insurance CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS maintenance CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS support_ticket CASCADE;

DROP INDEX IF EXISTS idx_cars_status;
DROP INDEX IF EXISTS idx_cars_class;
DROP INDEX IF EXISTS idx_bookings_status;
DROP INDEX IF EXISTS idx_support_tickets_status;

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(30) CHECK (role IN ('ROLE_ADMIN', 'ROLE_ACCIDENT_COMMISSAR', 'ROLE_USER')),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    last_name VARCHAR(100),
    phone CHAR(12),
    experience SMALLINT,
    birth_date DATE,
    rating DECIMAL(3, 2) DEFAULT 5.0,
    payment_card VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS location (
    id BIGSERIAL PRIMARY KEY,
    latitude DECIMAL(9, 6),
    longitude DECIMAL(9, 6)
);

CREATE TABLE IF NOT EXISTS insurance (
    id BIGSERIAL PRIMARY KEY,
    insurance_type VARCHAR(50),
    coverage TEXT,
    cost_ratio DECIMAL(5, 2)
);

CREATE TABLE IF NOT EXISTS car (
    id BIGSERIAL PRIMARY KEY,
    registration_number VARCHAR(20) UNIQUE NOT NULL,
    model VARCHAR(50),
    car_class VARCHAR(50) CHECK (car_class IN ('SUPER_ECONOMY', 'ECONOMY', 'COMFORT', 'BUSINESS', 'ELITE')),
    fuel_level DECIMAL(5, 2) CHECK (fuel_level >= 0 AND fuel_level <= 100),
	minute_price DECIMAL(5, 2) CHECK (minute_price > 0),
    location_id BIGINT REFERENCES location (id),
    status VARCHAR(20) CHECK (status IN ('AVAILABLE', 'RENTED', 'MAINTENANCE', 'OUT_OF_SERVICE'))
);

CREATE TABLE IF NOT EXISTS booking (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    car_id BIGINT REFERENCES car(id) ON DELETE SET NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('WAITING', 'ACTIVE', 'COMPLETED', 'CANCELLED')),
	tariff VARCHAR(20) CHECK (tariff IN ('MINUTES', 'FIX', 'HOURS')),
    rental_cost DECIMAL(10, 2),
    distance DECIMAL(10, 3),
    start_location_id BIGINT REFERENCES location(id),
    end_location_id BIGINT REFERENCES location(id),
    insurance_id BIGINT REFERENCES insurance(id)
);

CREATE TABLE IF NOT EXISTS payment (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT REFERENCES booking(id) ON DELETE CASCADE,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS feedback (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT REFERENCES booking(id) ON DELETE CASCADE,
    rating DECIMAL(2, 1) CHECK (rating >= 0 AND rating <= 5),
    comment TEXT,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS support_ticket (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    booking_id BIGINT REFERENCES booking(id),
    issue_type VARCHAR(20) CHECK (issue_type IN ('ACCOUNT', 'COMPENSATION', 'ACCIDENT', 'FINES', 'COMPLAIN', 'RENT')),
    description TEXT,
    status VARCHAR(20) CHECK (status IN ('OPEN', 'IN_PROGRESS', 'CLOSED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS fine (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    support_ticket_id BIGINT REFERENCES support_ticket(id),
    amount DECIMAL(10, 2),
    issued_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('PENDING', 'PAID')),
    reason TEXT
);

CREATE TABLE IF NOT EXISTS accident_report (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT REFERENCES support_ticket(id) ON DELETE CASCADE,
    report_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    report_details TEXT,
    is_guilty BOOLEAN
);

CREATE TABLE IF NOT EXISTS maintenance (
    id BIGSERIAL PRIMARY KEY,
    car_id BIGINT REFERENCES car(id) ON DELETE CASCADE,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    maintenance_type VARCHAR(50),
    status VARCHAR(20) CHECK (status IN ('PENDING', 'COMPLETED'))
);

CREATE TABLE IF NOT EXISTS document_verification (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    number CHAR(10) NOT NULL,
    document_type VARCHAR(20) CHECK (document_type IN ('PASSPORT', 'DRIVING_LICENSE')),
    status VARCHAR(20) CHECK (status IN ('PENDING', 'VERIFIED', 'REJECTED')),
    verification_date TIMESTAMP
);

-- подсчет расстояния между 2 точками на карте
CREATE OR REPLACE FUNCTION calculate_distance(
	lat1 DECIMAL, lng1 DECIMAL, lat2 DECIMAL, lng2 DECIMAL
) RETURNS DOUBLE PRECISION AS '
BEGIN
	RETURN 6371 * acos( sind(lat1) * sind(lat2) + cosd(lat1) * cosd(lat2) * cosd(lng1 - lng2) );
END;
' LANGUAGE plpgsql;

-- доступные машины по расстоянию
CREATE OR REPLACE FUNCTION find_available_cars(
    user_lat DECIMAL, user_lon DECIMAL, search_radius_km INT
) RETURNS TABLE (
    id BIGINT, registration_number VARCHAR, model VARCHAR,
    car_class VARCHAR, fuel_level DECIMAL, minute_price DECIMAL,
	latitude DECIMAL, longitude DECIMAL, status VARCHAR
) AS '
BEGIN
    RETURN QUERY
    SELECT c.id, c.registration_number, c.model, c.car_class, c.fuel_level, c.minute_price, l.latitude, l.longitude, c.status
    FROM car c
    JOIN location l ON c.location_id = l.id
    WHERE c.status = ''AVAILABLE''
    AND calculate_distance(user_lat, user_lon, l.latitude, l.longitude) <= search_radius_km;
END;
' LANGUAGE plpgsql;

-- Проверка доступности автомобиля перед началом бронирования
CREATE OR REPLACE FUNCTION check_car_availability()
RETURNS TRIGGER AS '
BEGIN
    -- Проверка, доступна ли машина
    IF (SELECT status FROM car WHERE id = NEW.car_id) != ''AVAILABLE'' THEN
        RAISE EXCEPTION ''The car with id % is not available for booking.'', NEW.car_id;
    END IF;

    IF EXISTS (SELECT 1
               FROM (SELECT status
                     FROM document_verification dv
                     WHERE dv.user_id = NEW.user_id AND dv.verification_date IS NULL
                        LIMIT 1) sub
               WHERE sub.status != ''VERIFIED'') THEN
        RAISE EXCEPTION ''The user with id % has unverified passport or driving license.'', NEW.user_id;
    END IF;

    -- Если доступна, продолжаем процесс создания бронирования
    RETURN NEW;
END;
' LANGUAGE plpgsql;

-- Триггер для проверки доступности автомобиля
CREATE OR REPLACE TRIGGER trig_check_car_availability
BEFORE INSERT ON booking
FOR EACH ROW
EXECUTE FUNCTION check_car_availability();

-- начало аренды, координаты старта
CREATE OR REPLACE FUNCTION set_start_location_and_car_status()
RETURNS TRIGGER AS '
BEGIN
    UPDATE booking
    SET start_location_id = (SELECT location_id FROM car WHERE id = NEW.car_id)
    WHERE id = NEW.id;

    UPDATE car
    SET status = ''RENTED''
    WHERE id = NEW.car_id;

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trig_set_start_location_and_car_status
AFTER INSERT ON booking
FOR EACH ROW
WHEN (NEW.status = 'WAITING')
EXECUTE FUNCTION set_start_location_and_car_status();

-- отмена аренды
CREATE OR REPLACE FUNCTION cancel_booking()
    RETURNS TRIGGER AS '
    BEGIN
        UPDATE booking
        SET end_location_id = start_location_id
        WHERE id = NEW.id;

        UPDATE car
        SET status = ''AVAILABLE''
        WHERE id = NEW.car_id;

        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trig_cancel_booking
    AFTER UPDATE OF status ON booking
    FOR EACH ROW
    WHEN (NEW.status = 'CANCELLED')
EXECUTE FUNCTION cancel_booking();

-- конец аренды, координаты окончания передаем в booking вместе со статусом COMPLETED
CREATE OR REPLACE FUNCTION set_end_location_and_car_status_and_distance()
RETURNS TRIGGER AS '
DECLARE
    new_location_id BIGINT;
    old_latitude NUMERIC;
    old_longitude NUMERIC;
    new_latitude NUMERIC;
    new_longitude NUMERIC;
    new_distance DOUBLE PRECISION;
    rental_duration INTERVAL;
    new_rental_cost NUMERIC;
BEGIN
    SELECT l.id, l.latitude, l.longitude INTO new_location_id, new_latitude, new_longitude
    FROM location l JOIN car ON l.id = car.location_id WHERE car.id = NEW.car_id;

    SELECT l.latitude, l.longitude INTO old_latitude, old_longitude
    FROM location l WHERE l.id = NEW.start_location_id;

    new_distance := calculate_distance(old_latitude, old_longitude, new_latitude, new_longitude);
    rental_duration := NEW.end_time - NEW.start_time;
    IF NEW.tariff = ''MINUTES'' THEN
        new_rental_cost := EXTRACT(EPOCH FROM rental_duration) / 60 *
                           (SELECT minute_price
                            FROM car
                            WHERE id = NEW.car_id) *
                           (SELECT cost_ratio
                            FROM insurance
                            WHERE id = NEW.insurance_id);

    ELSIF NEW.tariff = ''HOURS'' THEN
        new_rental_cost := ceil((EXTRACT(EPOCH FROM rental_duration) / 60) / 60.0) *
                           (SELECT minute_price
                            FROM car
                            WHERE id = NEW.car_id) * 60 * 0.95 *
                           (SELECT cost_ratio
                            FROM insurance
                            WHERE id = NEW.insurance_id);

    ELSIF NEW.tariff = ''FIX'' THEN
        new_rental_cost := NEW.distance * 4 * (SELECT minute_price
                                               FROM car
                                               WHERE id = NEW.car_id) *
                           (SELECT cost_ratio
                            FROM insurance
                            WHERE id = NEW.insurance_id);

    END IF;

    UPDATE booking
    SET end_location_id = new_location_id, distance = new_distance, rental_cost = new_rental_cost
    WHERE id = NEW.id;

    UPDATE car
    SET status = ''AVAILABLE''
    WHERE id = NEW.car_id;

    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trig1_set_end_location_and_car_status_and_distance
AFTER UPDATE OF status ON booking
FOR EACH ROW
WHEN (NEW.status = 'COMPLETED')
EXECUTE FUNCTION set_end_location_and_car_status_and_distance();

-- -- подсчет стоимости
-- CREATE OR REPLACE FUNCTION calculate_rental_cost()
-- RETURNS TRIGGER AS '
-- DECLARE
--     rental_duration INTERVAL;
-- BEGIN
--     rental_duration := NEW.end_time - NEW.start_time;
--
--     IF NEW.tariff = ''MINUTES'' THEN
--         NEW.rental_cost := EXTRACT(EPOCH FROM rental_duration) / 60 *
--             (SELECT minute_price FROM car WHERE id = NEW.car_id) *
--             (SELECT cost_ratio FROM insurance WHERE id = NEW.insurance_id);
--
--     ELSIF NEW.tariff = ''HOURS'' THEN
--         NEW.rental_cost := ceil((EXTRACT(EPOCH FROM rental_duration) / 60) / 60.0) *
--             (SELECT minute_price FROM car WHERE id = NEW.car_id) * 60 * 0.95 *
--             (SELECT cost_ratio FROM insurance WHERE id = NEW.insurance_id);
--
--     ELSIF NEW.tariff = ''FIX'' THEN
--         NEW.rental_cost := NEW.distance * 4 * (SELECT minute_price FROM car WHERE id = NEW.car_id) *
--             (SELECT cost_ratio FROM insurance WHERE id = NEW.insurance_id);
--
--     END IF;
--
--     DELETE FROM location
--     WHERE id NOT IN (
--         SELECT start_location_id FROM booking
--         UNION
--         SELECT end_location_id FROM booking
--         UNION
--         SELECT location_id FROM car
--     );
--
--     RETURN NEW;
-- END;
-- ' LANGUAGE plpgsql;
--
-- CREATE OR REPLACE TRIGGER trig3_calculate_rental_cost
-- AFTER UPDATE OF status ON booking
-- FOR EACH ROW
-- WHEN (NEW.status = 'COMPLETED')
-- EXECUTE FUNCTION calculate_rental_cost();


CREATE INDEX IF NOT EXISTS idx_cars_status ON car(status);

CREATE INDEX IF NOT EXISTS idx_cars_class ON car(car_class);

CREATE INDEX IF NOT EXISTS idx_bookings_status ON booking(status);

CREATE INDEX IF NOT EXISTS idx_support_tickets_status ON support_ticket(status);