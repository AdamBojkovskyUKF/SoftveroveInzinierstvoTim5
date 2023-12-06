package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import SoftveroveInzinierstvoTim5.RestAPI.dto.Study_ProgramDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Study_Program;
import SoftveroveInzinierstvoTim5.RestAPI.repository.Study_ProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Subgraph;

@Service
@Configurable
public class DefaultStudy_ProgramService implements Study_ProgramService{

    @Autowired
    Study_ProgramRepository study_ProgramRepository;

    @Override
    public Study_ProgramDTO saveStudyProgram(Study_ProgramDTO study_program) {
        Study_Program studyProgramModel = populateStudyProgramEntity(study_program);
        return populateStudyProgramData(study_ProgramRepository.save(studyProgramModel));
    }

    @Override
    public boolean deleteStudyProgram(Integer idstudy_program) {
        study_ProgramRepository.deleteById(idstudy_program);
        return true;   
    }
    
    @Override
    public List<Study_ProgramDTO> getAllStudyPrograms() {
        List<Study_ProgramDTO> study_programs = new ArrayList<>();
        Iterable <Study_Program> study_programList = study_ProgramRepository.findAll();
        study_programList.forEach(study_program -> {
            study_programs.add(populateStudyProgramData(study_program));
        });
        return study_programs;
    }

    @Override
    public Study_ProgramDTO getStudyProgramById(Integer idstudy_program) {
        return populateStudyProgramData(study_ProgramRepository.findById(idstudy_program).orElseThrow(() -> new EntityNotFoundException("Study Program not found")));
    }

    private Study_ProgramDTO populateStudyProgramData(final Study_Program study_program) {
        Study_ProgramDTO study_programData = new Study_ProgramDTO();
        study_programData.setIdstudy_program(study_program.getIdstudy_program());
        study_programData.setName(study_program.getName());
        return study_programData;
    }

    private Study_Program populateStudyProgramEntity(Study_ProgramDTO study_programData) {
        Study_Program study_program = new Study_Program();
        study_program.setIdstudy_program(study_programData.getIdstudy_program());
        study_program.setName(study_programData.getName());
        return study_program;
    }
    
}
