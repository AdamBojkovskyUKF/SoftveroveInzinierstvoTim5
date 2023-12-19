package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import SoftveroveInzinierstvoTim5.RestAPI.dto.Subject_For_PracticeDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Subject_for_Practice;
import SoftveroveInzinierstvoTim5.RestAPI.repository.Subject_For_PracticeRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Configurable
public class DefaultSubject_For_PracticeService implements Subject_For_PracticeService{

    @Autowired
    Subject_For_PracticeRepository subject_For_PracticeRepository;

    @Override
    public Subject_For_PracticeDTO saveSubjectForPractice(Subject_For_PracticeDTO subject_for_practice) {
        Subject_for_Practice subjectForPracticeModel = populateSubjectForPracticeEntity(subject_for_practice);
        return populateSubjectForPracticeData(subject_For_PracticeRepository.save(subjectForPracticeModel));
    }

    @Override
    public boolean deleteSubjectForPractice(Integer idsubject_for_practice) {
        subject_For_PracticeRepository.deleteById(idsubject_for_practice);
        return true;
    }

    @Override
    public List<Subject_For_PracticeDTO> getAllSubjectsForPractice() {
        List<Subject_For_PracticeDTO> subjectsForPractice = new ArrayList<>();
        Iterable<Subject_for_Practice> subjectForPracticeList = subject_For_PracticeRepository.findAll();
        subjectForPracticeList.forEach(subject_for_practice -> {
            subjectsForPractice.add(populateSubjectForPracticeData(subject_for_practice));
        });
        return subjectsForPractice;
    }

    @Override
    public Subject_For_PracticeDTO getSubjectForPracticeById(Integer idsubject_for_practice) {
        return populateSubjectForPracticeData(subject_For_PracticeRepository.findById(idsubject_for_practice).orElseThrow(() -> new EntityNotFoundException("Subject for Practice not found")));
    }

    private Subject_For_PracticeDTO populateSubjectForPracticeData (final Subject_for_Practice subject_for_practice) {
        Subject_For_PracticeDTO subjectForPracticeData = new Subject_For_PracticeDTO();
        subjectForPracticeData.setIdsubject_for_practice(subject_for_practice.getIdsubject_for_practice());
        subjectForPracticeData.setStudy_program_idstudy_program(subject_for_practice.getStudy_program_idstudy_program());
        subjectForPracticeData.setName(subject_for_practice.getName());
        subjectForPracticeData.setCredits(subject_for_practice.getCredits());
        return subjectForPracticeData;
    }

    private Subject_for_Practice populateSubjectForPracticeEntity(Subject_For_PracticeDTO subjectForPracticeData) {
        Subject_for_Practice subject_for_practice = new Subject_for_Practice();
        subject_for_practice.setIdsubject_for_practice(subjectForPracticeData.getIdsubject_for_practice());
        subject_for_practice.setStudy_program_idstudy_program(subjectForPracticeData.getStudy_program_idstudy_program());
        subject_for_practice.setName(subjectForPracticeData.getName());
        subject_for_practice.setCredits(subjectForPracticeData.getCredits());
        return subject_for_practice;
    }
    
}
