package io.lynx.oebs.repositories;

import io.lynx.oebs.entities.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;
@Repository
public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {
    Optional<OperatingHours> findByDayAndOpeningTimeAndClosingTime(DayOfWeek day, String openingTime, String closingTime);
}
