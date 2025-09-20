
package com.micdog.openflag.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.micdog.openflag.model.Flag;

import java.util.Optional;

public interface FlagRepository extends JpaRepository<Flag, Long> {
    Optional<Flag> findByFlagKey(String flagKey);
}
