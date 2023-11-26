package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.github.javafaker.Faker;

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
        List <PersonDTO> persons = personService.getAllPersons();
        String people = "";
        for (PersonDTO personDTO : persons) {
            people += "Person: " + personDTO.getName() + " "+ personDTO.getSurname() + 
            " Email: " + personDTO.getEmail() + " Adress: "+ personDTO.getAddress() + "\n";
        }
        return people;
    }
    @GetMapping("/dataSeed")
    public String handleDataSeedRequest(){
    for (int i = 0; i < 20; i++) {
        PersonDTO p = new PersonDTO();
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String streetAddress = faker.address().streetAddress();
        p.setEmail(firstName+lastName+"@gmail.com");
        p.setName(firstName);
        p.setSurname(lastName);
        p.setAddress(streetAddress);
        p.setPhone_number("123456789");
        personService.savePerson(p);
    }
        return "Data Loaded";
    }
    @GetMapping("/login")
    public String handleLoginRequest(){
        
        return "Login Successful";
    }

    
}
