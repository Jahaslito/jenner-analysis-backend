package com.tabibu.backend.api;

import com.tabibu.backend.exceptions.ResourceNotFoundException;
import com.tabibu.backend.models.HealthCareProvider;
import com.tabibu.backend.repositories.HealthCareProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthCareProviderController {

    private final HealthCareProviderRepository repository;

    @Autowired
    public HealthCareProviderController(HealthCareProviderRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all health care providers list.
     *
     * @return the list
     */
    @GetMapping("/providers")
    public List<HealthCareProvider> getAllProviders() {
        return repository.findAll();
    }

    /**
     * Gets providers by id.
     *
     * @param providerId the user id
     * @return the providers by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/providers/{id}")
    public ResponseEntity<HealthCareProvider> getProvidersById(@PathVariable(value = "id") Long providerId)
            throws ResourceNotFoundException {
        HealthCareProvider provider =
                repository
                        .findById(providerId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + providerId));
        return ResponseEntity.ok().body(provider);
    }

    /**
     * Create provider.
     *
     * @param provider the provider
     * @return the user
     */
    @PostMapping("/providers")
    public HealthCareProvider createProvider(@Valid @RequestBody HealthCareProvider provider) {
        return repository.save(provider);
    }

    /**
     * Update provider response entity.
     *
     * @param providerId the provider id
     * @param providerDetails the provider details
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/providers/{id}")
    public ResponseEntity<HealthCareProvider> updateProvider(
            @PathVariable(value = "id") Long providerId, @Valid @RequestBody HealthCareProvider providerDetails)
            throws ResourceNotFoundException {

        HealthCareProvider provider =
                repository
                        .findById(providerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Provider not found on :: " + providerId));

        provider.setName(providerDetails.getName());
        provider.setEmail(providerDetails.getEmail());
        provider.setLocation(providerDetails.getLocation());
        provider.setPhone(providerDetails.getPhone());
        final HealthCareProvider updatedProvider = repository.save(providerDetails);
        return ResponseEntity.ok(updatedProvider);
    }

    /**
     * Delete provider map.
     *
     * @param providerId the user id
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("/providers/{id}")
    public Map<String, Boolean> deleteProvider(@PathVariable(value = "id") Long providerId) throws Exception {
        HealthCareProvider provider =
                repository
                        .findById(providerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Provider not found on :: " + providerId));

        repository.delete(provider);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}