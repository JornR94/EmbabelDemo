package com.example.embabeldemo;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MassageTherapistRepository extends JpaRepository<MassageTherapist, Long> {
    @NotNull Page<MassageTherapist> findAll(@NotNull Pageable pageable);
}
