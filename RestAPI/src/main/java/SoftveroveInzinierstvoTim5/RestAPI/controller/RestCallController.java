package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import SoftveroveInzinierstvoTim5.RestAPI.dto.PersonDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Person;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultPersonService;
import java.util.List;

@RestController
public class RestCallController {
    @Autowired
    DefaultPersonService personService;

    @GetMapping("/test")
    public String handleTestRequest() {
        PersonDTO p = new PersonDTO();
        p.setId_person(0);
        p.setName("name");
        personService.savePerson(p);
        List <PersonDTO> persons = personService.getAllPersons();
        String firstPerson = persons.get(0).getName();
        return firstPerson;
    }
    @GetMapping("/dataSeed")
    public String handleDataSeedRequest(){
        return "Data Loaded";
    }
}
