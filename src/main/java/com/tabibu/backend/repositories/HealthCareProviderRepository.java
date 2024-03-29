package com.tabibu.backend.repositories;

import com.tabibu.backend.models.HealthCareProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthCareProviderRepository extends JpaRepository<HealthCareProvider, Long> { }
