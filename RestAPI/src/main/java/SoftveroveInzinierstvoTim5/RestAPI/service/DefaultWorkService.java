package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import SoftveroveInzinierstvoTim5.RestAPI.dto.WorkDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Work;
import SoftveroveInzinierstvoTim5.RestAPI.repository.WorkRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Configurable
public class DefaultWorkService implements WorkService{

    @Autowired
    WorkRepository workRepository;

    @Override
    public WorkDTO saveWork(WorkDTO work) {
        Work workModel = populateWorkEntity(work);
        return populateWorkData(workRepository.save(workModel));
    }

    @Override
    public boolean deleteWork(Integer id_work) {
        workRepository.deleteById(id_work);
        return true;
    }

    @Override
    public List<WorkDTO> getAllWorks() {
        List<WorkDTO> works = new ArrayList<>();
        Iterable<Work> workList = workRepository.findAll();
        workList.forEach(work -> {
            works.add(populateWorkData(work));
        });
        return works;
    }

    @Override
    public WorkDTO getWorkById(Integer id_work) {
        return populateWorkData(workRepository.findById(id_work).orElseThrow(() -> new EntityNotFoundException("Work not found")));
    }

    private WorkDTO populateWorkData(final Work work) {
        WorkDTO workData = new WorkDTO();
        workData.setId_work(work.getId_work());
        workData.setOffer_id_offer(work.getOffer_id_offer());
        workData.setAccount_id_account(work.getAccount_id_account());
        workData.setContract(work.getContract());
        workData.setWork_log(work.getWork_log());
        workData.setState(work.getState());
        workData.setFeedback_student(work.getFeedback_student());
        workData.setFeedback_company(work.getFeedback_company());
        workData.setMark(work.getMark());
        workData.setCompletion_year(work.getCompletion_year());
        return workData;
    }

    private Work populateWorkEntity(WorkDTO workData) {
        Work work = new Work();
        work.setId_work(workData.getId_work());
        work.setOffer_id_offer(workData.getOffer_id_offer());
        work.setAccount_id_account(workData.getAccount_id_account());
        work.setContract(workData.getContract());
        work.setWork_log(workData.getWork_log());
        work.setState(workData.getState());
        work.setFeedback_student(workData.getFeedback_student());
        work.setFeedback_company(workData.getFeedback_company());
        work.setMark(workData.getMark());
        work.setCompletion_year(workData.getCompletion_year());
        return work;
    }
    
}
