package com.linasdeli.api.repository;

import com.linasdeli.api.domain.Platter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatterRepository extends JpaRepository<Platter, Integer> {
    Optional<Platter> findByPlatterName(String platterName);
}