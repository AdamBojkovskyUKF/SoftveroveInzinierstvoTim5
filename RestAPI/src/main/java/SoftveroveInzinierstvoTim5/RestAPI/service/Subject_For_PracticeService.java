package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.List;

import SoftveroveInzinierstvoTim5.RestAPI.dto.Subject_For_PracticeDTO;

public interface Subject_For_PracticeService {
    Subject_For_PracticeDTO saveSubjectForPractice(Subject_For_PracticeDTO subject_for_practice);
    boolean deleteSubjectForPractice(final Integer idsubject_for_practice);
    List<Subject_For_PracticeDTO> getAllSubjectsForPractice();
    Subject_For_PracticeDTO getSubjectForPracticeById(final Integer idsubject_for_practice);
}
