package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import SoftveroveInzinierstvoTim5.RestAPI.dto.CommunicationDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Communication;
import SoftveroveInzinierstvoTim5.RestAPI.repository.CommunicationRepository;
import jakarta.persistence.EntityNotFoundException;

public class DefaultCommunicationService implements CommunicationService{

    @Autowired
    CommunicationRepository communicationRepository;

    @Override
    public CommunicationDTO saveCommunication(CommunicationDTO communication) {
        Communication communicationModel = populateCommunicationEntity(communication);
        return populateCommunicationData(communicationRepository.save(communicationModel));
    }

    @Override
    public boolean deleteCommunication(Integer id_communication) {
        communicationRepository.deleteById(id_communication);
        return true;
    }

    @Override
    public List<CommunicationDTO> getAllCommunications() {
        List<CommunicationDTO> communications = new ArrayList<>();
        Iterable<Communication> communicationList = communicationRepository.findAll();
        communicationList.forEach(communication -> {
            communications.add(populateCommunicationData(communication));
        });
        return communications;
    }

    @Override
    public CommunicationDTO getCommunicationId(Integer id_communication) {
        return populateCommunicationData(communicationRepository.findById(id_communication).orElseThrow(() -> new EntityNotFoundException("Communication not found")));
    }

    private CommunicationDTO populateCommunicationData(final Communication communication) {
        CommunicationDTO communicationData = new CommunicationDTO();
        communicationData.setAccount_id_account(communication.getAccount_id_account());
        communicationData.setAccount_id_account1(communication.getAccount_id_account1());
        communicationData.setMessages(communication.getMessages());
        return communicationData;
    }

    private Communication populateCommunicationEntity(CommunicationDTO communicationData) {
        Communication communication = new Communication();
        communication.setAccount_id_account(communicationData.getAccount_id_account());
        communication.setAccount_id_account1(communicationData.getAccount_id_account1());
        communication.setMessages(communicationData.getMessages());
        return communication;
    }
    
}
