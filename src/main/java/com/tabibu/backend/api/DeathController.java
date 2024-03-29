package com.tabibu.backend.api;

import com.tabibu.backend.exceptions.ResourceNotFoundException;
import com.tabibu.backend.models.Death;
import com.tabibu.backend.models.DeathDTO;
import com.tabibu.backend.repositories.DeathRepository;
import com.tabibu.backend.repositories.DiseaseRepository;
import com.tabibu.backend.repositories.HealthCareProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/v1")
public class DeathController {
    private final DeathRepository deathRepository;
    private final DiseaseRepository diseaseRepository;
    private final HealthCareProviderRepository healthCareProviderRepository;

    @Autowired
    public DeathController(DeathRepository deathRepository,
                           DiseaseRepository diseaseRepository,
                           HealthCareProviderRepository healthCareProviderRepository) {
        this.deathRepository = deathRepository;
        this.diseaseRepository = diseaseRepository;
        this.healthCareProviderRepository = healthCareProviderRepository;
    }


    @GetMapping("/deaths")
    public List<DeathDTO> getAllDeaths(DeathSpec deathSpec) {
        return deathRepository.findAll(deathSpec).stream().map(Death::convertToDTO).collect(Collectors.toList());
    }


    @GetMapping("/deaths/{id}")
    public ResponseEntity<DeathDTO> getDeathsById(@PathVariable(value = "id") Long Id)
            throws ResourceNotFoundException {
        Death death =
                deathRepository
                        .findById(Id)
                        .orElseThrow(() -> new ResourceNotFoundException("Death not found on id: " + Id));
        return ResponseEntity.ok().body(death.convertToDTO());
    }


    @PostMapping("/deaths")
    public DeathDTO createDeath(@Valid @RequestBody DeathDTO deathDTO) throws ResourceNotFoundException, ParseException {
        Death death = this.convertToEntity(deathDTO);
        deathRepository.save(death);
        return death.convertToDTO();
    }

    @DeleteMapping("/deaths/{id}")
    public Map<String, Boolean> deleteDeath(@PathVariable(value = "id") Long Id) throws Exception {
        Death death =
                deathRepository
                        .findById(Id)
                        .orElseThrow(() -> new ResourceNotFoundException("Death not found on id: " + Id));

        deathRepository.delete(death);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    private Death convertToEntity(DeathDTO deathDTO) throws ResourceNotFoundException, ParseException {
        Death death = new Death();
        death.setHealthCareProvider(healthCareProviderRepository.findById(deathDTO.getHealthCareProvider().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Could not find provider at id: " + deathDTO.getHealthCareProvider())));
        death.setCorpseAge(deathDTO.getCorpseAge());
        death.setDisease(diseaseRepository.findById(deathDTO.getDisease().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Could not find disease at id: " + deathDTO.getDisease())));
        death.setDeathDate(new SimpleDateFormat("yyyy-MM-dd").parse(deathDTO.getDeathDate()));
        return death;
    }
}
