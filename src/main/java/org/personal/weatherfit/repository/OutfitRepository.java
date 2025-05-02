package org.personal.weatherfit.repository;

import org.personal.weatherfit.aggregate.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Integer> {
    Optional<Outfit> findByOutfitDateAndCity(String date, String city);
}
