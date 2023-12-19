package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.List;

import SoftveroveInzinierstvoTim5.RestAPI.dto.WorkDTO;

public interface WorkService {
    WorkDTO saveWork(WorkDTO work);
    boolean deleteWork(final Integer id_work);
    List<WorkDTO> getAllWorks();
    WorkDTO getWorkById(final Integer id_work);
}
