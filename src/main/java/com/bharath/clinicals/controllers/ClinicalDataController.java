package com.bharath.clinicals.controllers;

import com.bharath.clinicals.dto.ClinicalDataRequest;
import com.bharath.clinicals.model.ClinicalData;
import com.bharath.clinicals.model.Patient;
import com.bharath.clinicals.repositories.ClinicalDataRepository;
import com.bharath.clinicals.repositories.PatientRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ClinicalDataController {

    private final PatientRepository patientRepository;
    private final ClinicalDataRepository clinicalDataRepository;

    public ClinicalDataController(PatientRepository patientRepository, ClinicalDataRepository clinicalDataRepository) {
        this.patientRepository = patientRepository;
        this.clinicalDataRepository = clinicalDataRepository;
    }


    @PostMapping("/clinicals")
    public ClinicalData saveClinicalData(@RequestBody ClinicalDataRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId()).get();
        ClinicalData clinicalData = new ClinicalData();
        clinicalData.setComponentName(request.getComponentName());
        clinicalData.setComponentValue(request.getComponentValue());
        clinicalData.setPatient(patient);

        return clinicalDataRepository.save(clinicalData);
    }

}
