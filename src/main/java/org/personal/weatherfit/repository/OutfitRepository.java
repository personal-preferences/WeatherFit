package org.personal.weatherfit.repository;

import org.personal.weatherfit.aggregate.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Integer> {
    Optional<Outfit> findByOutfitDate(String date);
}
