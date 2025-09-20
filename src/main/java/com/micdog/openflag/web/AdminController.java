
package com.micdog.openflag.web;

import com.micdog.openflag.model.Flag;
import com.micdog.openflag.repo.FlagRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flags")
public class AdminController {

    private final FlagRepository repo;
    public AdminController(FlagRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Flag> list() { return repo.findAll(); }

    @PostMapping
    public Flag create(@Valid @RequestBody Flag f) { return repo.save(f); }

    @PutMapping("/{id}")
    public ResponseEntity<Flag> update(@PathVariable Long id, @RequestBody Flag f) {
        return repo.findById(id).map(existing -> {
            existing.setFlagKey(f.getFlagKey());
            existing.setEnabled(f.isEnabled());
            existing.setPercentage(f.getPercentage());
            existing.setIncludeIds(f.getIncludeIds());
            existing.setExcludeIds(f.getExcludeIds());
            existing.setAttributeKey(f.getAttributeKey());
            existing.setAttributeAllowedCsv(f.getAttributeAllowedCsv());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return repo.findById(id).map(f -> { repo.delete(f); return ResponseEntity.noContent().build(); })
                .orElse(ResponseEntity.notFound().build());
    }
}
