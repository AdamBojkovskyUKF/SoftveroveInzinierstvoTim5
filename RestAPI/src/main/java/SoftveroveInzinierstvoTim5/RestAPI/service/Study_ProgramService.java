package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.List;

import SoftveroveInzinierstvoTim5.RestAPI.dto.Study_ProgramDTO;

public interface Study_ProgramService {
    Study_ProgramDTO saveStudyProgram(Study_ProgramDTO study_program);
    boolean deleteStudyProgram(final Integer idstudy_program);
    List<Study_ProgramDTO> getAllStudyPrograms();
    Study_ProgramDTO getStudyProgramById(final Integer idstudy_program);
}
