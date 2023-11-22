package SoftveroveInzinierstvoTim5.RestAPI.service;

import SoftveroveInzinierstvoTim5.RestAPI.dto.PersonDTO;

import java.util.List;

public interface PersonService {

    PersonDTO savePerson(PersonDTO person);
    boolean deletePerson(final Integer personId);
    List<PersonDTO> getAllPersons();
    PersonDTO getPersonById(final Integer personId);
}