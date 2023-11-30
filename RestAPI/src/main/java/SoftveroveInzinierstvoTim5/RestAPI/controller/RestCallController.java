package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.github.javafaker.Faker;

import SoftveroveInzinierstvoTim5.RestAPI.dto.CompanyDTO;
import SoftveroveInzinierstvoTim5.RestAPI.dto.PersonDTO;
import SoftveroveInzinierstvoTim5.RestAPI.dto.WorkDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Person;
import SoftveroveInzinierstvoTim5.RestAPI.model.Work;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultPersonService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultWorkService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultCompanyService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class RestCallController {
    @Autowired
    DefaultPersonService personService;
    @Autowired
    DefaultCompanyService companyService;

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

        p.setName(firstName);
        p.setSurname(lastName);
        p.setAddress(streetAddress);
        p.setPhone_number("+421912345678");
        p.setEmail(firstName+lastName+"@gmail.com");
        personService.savePerson(p);
    }
        return "Data Loaded";
    }

    @GetMapping("/login")
    public String handleLoginRequest(){
        
        return "Login Successful";
    }

    @GetMapping("/zobrazFirmy")
    public String zobrazFirmy() {
        List <CompanyDTO> companies = companyService.getAllCompanies();
        StringBuilder companiesString = new StringBuilder();
        for (CompanyDTO companyDTO : companies) {
            PersonDTO representative = personService.getPersonById(companyDTO.getRepresentative_id_person());

            companiesString.append("Názov: ").append(companyDTO.getName())
                    .append(" Adresa: ").append(companyDTO.getAddress())
                    .append(" Zástupca firmy: ").append(representative != null ? representative.getName() + " " + representative.getSurname() : "Neznámy zástupca firmy")
                    .append("\n");
        }
        
        return companiesString.toString();
    }

}
