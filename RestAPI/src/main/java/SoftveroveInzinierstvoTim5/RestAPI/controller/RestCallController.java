package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.github.javafaker.Faker;

import SoftveroveInzinierstvoTim5.RestAPI.dto.AccountDTO;
import SoftveroveInzinierstvoTim5.RestAPI.dto.PersonDTO;
import SoftveroveInzinierstvoTim5.RestAPI.dto.WorkDTO;
import SoftveroveInzinierstvoTim5.RestAPI.dto.CompanyDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Person;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultAccountService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultPersonService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultWorkService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultCompanyService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.*;

@RestController
public class RestCallController {
    @Autowired
    DefaultPersonService personService;
    @Autowired
    DefaultCompanyService companyService;

    @Autowired
    DefaultAccountService accountService;

    String[] roly = {"Admin","VeduciPracoviska","Student","PoverenyPracovnik","Zastupca"};

    final String RESPONSECODE_OK = "200";
    final String RESPONSECODE_ERROR = "500";

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
        if (!personService.getAllPersons().isEmpty()) {
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
        }
        if(!accountService.getAllAccounts().isEmpty()){
            for (int i = 0; i < 20; i++) {
                AccountDTO acc = new AccountDTO();
                Faker faker = new Faker();
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String streetAddress = faker.address().streetAddress();

                acc.setEmail_address(firstName+lastName+"@gmail.com");
                acc.setInstitute("placeholder");
                acc.setPassword("password");
                acc.setStudy_level("placeholder");
                acc.setSignup_year(faker.number().numberBetween(2000, 2030)+"");
                acc.setRole(roly[faker.number().numberBetween(0, 4)]);
                accountService.saveAccount(acc);
            }
        }
    
        return "Data Loaded";
    }
    @GetMapping(value = "/login")
    public String handleLoginRequest(@RequestBody String RequestBody){
        JSONObject json = new JSONObject(RequestBody);
        String password = json.getString("password");
        String email = json.getString("email");
        JSONObject responseObject = new JSONObject();
        List <AccountDTO> accounts = accountService.getAllAccounts();
            for (AccountDTO accountDTO : accounts) {
                if(accountDTO.getEmail_address().equals(email) && accountDTO.getPassword().equals(password))
                {
                    responseObject.put("response_code", RESPONSECODE_OK);
                    responseObject.put("reponseMessage", "Login Successful");
                    JSONObject reponseValues = new JSONObject();
                    reponseValues.put("account_id", accountDTO.getId_account());
                    responseObject.put("responseJSON", reponseValues);
                    return responseObject.toString();
                }
            }
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("reponseMessage", "Login Failed -> Username + Password Combination not found");
        return responseObject.toString();
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
