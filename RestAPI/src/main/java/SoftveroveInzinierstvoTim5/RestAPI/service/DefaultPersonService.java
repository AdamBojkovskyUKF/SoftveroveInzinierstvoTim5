package SoftveroveInzinierstvoTim5.RestAPI.service;

import SoftveroveInzinierstvoTim5.RestAPI.model.Person;
import SoftveroveInzinierstvoTim5.RestAPI.dto.PersonDTO;
import SoftveroveInzinierstvoTim5.RestAPI.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Configurable
public class DefaultPersonService implements PersonService {

    @Autowired
    PersonRepository personRepository;
    
    @Override
    public PersonDTO savePerson(PersonDTO person) {
        Person personModel = populatePersonEntity(person);
        return populatePersonData(personRepository.save(personModel));
    }

    @Override
    public boolean deletePerson(Integer personId) {
        personRepository.deleteById(personId);
        return true;
    }

    @Override
    public List < PersonDTO > getAllPersons() {
            List < PersonDTO > persons = new ArrayList < > ();
            Iterable < Person > personList =  personRepository.findAll();
            personList.forEach(person -> {
                persons.add(populatePersonData(person));
            });
            return persons;
    }

    @Override
    public PersonDTO getPersonById(final Integer personId) {
        return populatePersonData(personRepository.findById(personId).orElseThrow(() -> new EntityNotFoundException("Person not found")));
    }

    private PersonDTO populatePersonData(final Person person) {
        PersonDTO personData = new PersonDTO();
        personData.setId_person(person.getId_person());
        personData.setAddress(person.getAddress());
        personData.setName(person.getName());
        personData.setSurname(person.getSurname());
        personData.setEmail(person.getEmail());
        personData.setPhone_number(person.getPhone_number());
        personData.setAddress(person.getAddress());
        return personData;
    }

    private Person populatePersonEntity(PersonDTO personData) {
        Person person = new Person();
        person.setId_person(personData.getId_person());
        person.setAddress(personData.getAddress());
        person.setName(personData.getName());
        person.setSurname(personData.getSurname());
        person.setEmail(personData.getEmail());
        person.setPhone_number(personData.getPhone_number());
        person.setAddress(personData.getAddress());
        return person;
    }
}