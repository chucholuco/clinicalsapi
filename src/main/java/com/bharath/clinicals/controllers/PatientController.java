package com.bharath.clinicals.controllers;

import com.bharath.clinicals.model.ClinicalData;
import com.bharath.clinicals.model.Patient;
import com.bharath.clinicals.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PatientController {

    private final PatientRepository repository;
    Map<String, String> filters = new HashMap<>();

    @Autowired
    public PatientController(PatientRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/patients")
    public List<Patient> getPatients() {
        return repository.findAll();
    }

    @GetMapping("/patients/{id}")
    public Patient getPatient(@PathVariable("id") long id) {
        return repository.findById(id).get();
    }

    @PostMapping("/patients")
    public Patient savePatient(@RequestBody Patient patient) {
        return repository.save(patient);
    }

    @GetMapping("/patients/analize/{id}")
    public Patient analize(@PathVariable("id") Long id) {
        Patient patient = repository.findById(id).get();
        Set<ClinicalData> clinicalDatas = patient.getClinicalDatas();
        Set<ClinicalData> duplicateClinicalData = clinicalDatas.stream().collect(Collectors.toSet());
        duplicateClinicalData.stream()
                .forEach(x -> {
                    if (filters.containsKey(x.getComponentName())) {
                        clinicalDatas.remove(x);
                        return;
                    } else {
                        filters.put(x.getComponentName(), null);
                    }
                    if (x.getComponentName().equals("hw")) {
                        String[] heightAndWeight = x.getComponentValue().split("/");
                        if (heightAndWeight != null && heightAndWeight.length > 1) {
                            float heightInMeters = Float.parseFloat(heightAndWeight[0]) * 0.4536F;
                            float bmi = Float.parseFloat(heightAndWeight[1]) / (heightInMeters * heightInMeters);
                            ClinicalData bmiData = new ClinicalData();
                            bmiData.setComponentName("bmi");
                            bmiData.setComponentValue(Float.toString(bmi));
                            clinicalDatas.add(bmiData);
                        }
                    }
                });
        filters.clear();
        return patient;
    }
}
