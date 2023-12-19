package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.List;

import SoftveroveInzinierstvoTim5.RestAPI.dto.CommunicationDTO;

public interface CommunicationService {
    CommunicationDTO saveCommunication(CommunicationDTO communication);
    boolean deleteCommunication(final Integer id_communication);
    List<CommunicationDTO> getAllCommunications();
    CommunicationDTO getCommunicationId(final Integer id_communication);
}
