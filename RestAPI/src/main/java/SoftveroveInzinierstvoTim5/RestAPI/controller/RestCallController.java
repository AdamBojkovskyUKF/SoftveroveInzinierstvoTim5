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
import SoftveroveInzinierstvoTim5.RestAPI.dto.ReportDTO;
import SoftveroveInzinierstvoTim5.RestAPI.dto.WorkDTO;
import SoftveroveInzinierstvoTim5.RestAPI.dto.CompanyDTO;
import SoftveroveInzinierstvoTim5.RestAPI.dto.OfferDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Person;
import SoftveroveInzinierstvoTim5.RestAPI.model.Work;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultAccountService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultPersonService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultReportService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultWorkService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultCompanyService;
import SoftveroveInzinierstvoTim5.RestAPI.service.DefaultOfferService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.catalina.valves.JsonAccessLogValve;
import org.json.*;

@RestController
public class RestCallController {
    @Autowired
    DefaultPersonService personService;
    @Autowired
    DefaultCompanyService companyService;
    @Autowired
    DefaultAccountService accountService;
    @Autowired
    DefaultWorkService workService;
    @Autowired
    DefaultOfferService offerService;
    @Autowired
    DefaultReportService reportService;

    String[] roly = {"admin","veduci_pracoviska","student","povereny_pracovnik","zastupca_firmy"};

    String[] institute = {"katedra_informatiky", "katedra_matematiky", "katedra_fyziky"};

    final String RESPONSECODE_OK = "200";
    final String RESPONSECODE_ERROR = "500";
    final String RESPONSECODE_PERMISSION_DENIED = "1002";
    final String RESPONSECODE_PERMISSION_GRANTED = "1001";

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
        //if (!personService.getAllPersons().isEmpty()) {
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
        //}
        //if(!accountService.getAllAccounts().isEmpty()){
            for (int i = 0; i < 20; i++) {
                AccountDTO acc = new AccountDTO();
                Faker faker = new Faker();
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String streetAddress = faker.address().streetAddress();

                acc.setEmail_address(firstName+lastName+"@gmail.com");
                acc.setInstitute(institute[faker.number().numberBetween(0, 2)]);
                acc.setPassword("password");
                acc.setStudy_level("placeholder");
                acc.setSignup_year(faker.number().numberBetween(2000, 2030)+"");
                acc.setRole(roly[faker.number().numberBetween(0, 4)]);
                accountService.saveAccount(acc);
            }
        //}
    
        return "Data Loaded";
    }


    /**
     * @apiNote prihlasovanie sa
     * @param RequestBody
     * @return
     */
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

    /**
     * @apiNote zobrazovanie firiem
     * @param requestString
     * @return
     */
    @GetMapping("/zobrazFirmy")
    public String zobrazFirmy(@RequestBody String requestString) {
        JSONObject json = new JSONObject(requestString);
        int id = json.getInt("account_id");
        AccountDTO acc = accountService.getAccountId(id);
        JSONObject responseObject = new JSONObject();

        List <CompanyDTO> companies = companyService.getAllCompanies();
        JSONArray companiesArray = new JSONArray();

        if(acc.getRole().equals("admin") || acc.getRole().equals("veduci_pracoviska") || acc.getRole().equals("povereny_pracovnik")) {
            for (CompanyDTO companyDTO : companies) {
                JSONObject companyJson = new JSONObject();
                PersonDTO representative = personService.getPersonById(companyDTO.getRepresentative_id_person());

                companyJson.put("Názov: ", companyDTO.getName());
                companyJson.put(" Adresa: ", companyDTO.getAddress());
                companyJson.put(" Meno: ", representative.getName());
                companyJson.put(" Priezvisko: ", representative.getSurname());

                companiesArray.put(companyJson);
            }

            return companiesArray.toString();
        } else {
            responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
            responseObject.put("responseMessage", "Nemáš prístup k zobrazeniu firiem.");

            return responseObject.toString();
        }
    }

    /**
     * @apiNote zobrazovanie studentov na praxi
     * @param requestString
     * @return
     */
    @GetMapping("/zobrazStudentovNaPraxi")
    public String zobrazStudentovNaPraxi(@RequestBody String requestString) {
        JSONObject json = new JSONObject(requestString);
        int id = json.getInt("account_id");
        AccountDTO acc = accountService.getAccountId(id);
        
        List<WorkDTO> works = workService.getAllWorks();
        JSONArray studentWorkArray = new JSONArray();

        JSONObject responseObject = new JSONObject();
        
        if(acc.getRole().equals("veduci_pracoviska")) {
            for (WorkDTO workDTO : works) {
                JSONObject studentWork = new JSONObject();
                AccountDTO studentAcc = accountService.getAccountId(workDTO.getAccount_id_account());
                PersonDTO studentPerson = personService.getPersonById(studentAcc.getPerson_id_person());
                OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());

                studentWork.put("Meno: ", studentPerson.getName());
                studentWork.put(" Priezvisko: ", studentPerson.getSurname());
                studentWork.put(" Firma: ", company.getName());
                studentWork.put(" Stav: ", workDTO.getState());
                                
                studentWorkArray.put(studentWork);
            }

            return studentWorkArray.toString();
        } else {
            responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
            responseObject.put("responseMessage", "Nemáš prístup k zobrazeniu študentov na praxi.");

            return responseObject.toString();
        }
    }
    /**
     * @apiNote zobrazovanie schvalenych a ukoncenych praxi
     * @param requestString
     * @return
     */
    @GetMapping("/zobrazSchvaleneUkoncenePraxe")
    public String zobrazSchvaleneUkoncenePraxe(@RequestBody String requestString) {
        JSONObject json = new JSONObject(requestString);
        int id = json.getInt("account_id");
        AccountDTO acc = accountService.getAccountId(id);

        List<WorkDTO> works = workService.getAllWorks();
        JSONArray studentWorkArray = new JSONArray();

        JSONObject responseObject = new JSONObject();

        if(acc.getRole().equals("veduci_pracoviska")) {
            for (WorkDTO workDTO : works) {
                JSONObject studentWork = new JSONObject();
                AccountDTO studentAcc = accountService.getAccountId(workDTO.getAccount_id_account());
                PersonDTO studentPerson = personService.getPersonById(studentAcc.getPerson_id_person());
                OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());

                if(workDTO.getState().equals("schvalena") || workDTO.getState().equals("ukoncena")) {
                    studentWork.put("Meno: ", studentPerson.getName());
                    studentWork.put(" Priezvisko: ", studentPerson.getSurname());
                    studentWork.put(" Firma: ", company.getName());
                    studentWork.put(" Stav: ", workDTO.getState());

                    studentWorkArray.put(studentWork);
                }
            }

            return studentWorkArray.toString();
        } else {
            responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
            responseObject.put("responseMessage", "Nemáš prístup k zobrazeniu schválených a ukončených praxí študentov.");

            return responseObject.toString();
        }
    }

    /**
     * @apiNote zobrazovanie studentov na praxi podla zvolenej katedry
     * @param requestString
     * @return
     */
    @GetMapping("/zobrazStudentovNaPraxiPodlaKatedry")
    public String zobrazStudentovNaPraxiPodlaKatedry(@RequestBody String requestString) {
        JSONObject json = new JSONObject(requestString);
        int id = json.getInt("account_id");
        String institut = json.getString("institute");
        AccountDTO acc = accountService.getAccountId(id);
        
        List<WorkDTO> works = workService.getAllWorks();
        JSONArray studentWorkArray = new JSONArray();

        JSONObject responseObject = new JSONObject();
        
        if(acc.getRole().equals("veduci_pracoviska")) {
            for (WorkDTO workDTO : works) {
                JSONObject studentWork = new JSONObject();
                AccountDTO studentAcc = accountService.getAccountId(workDTO.getAccount_id_account());
                PersonDTO studentPerson = personService.getPersonById(studentAcc.getPerson_id_person());
                OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());

                if(studentAcc.getInstitute().equals(institut)) {
                    studentWork.put("Meno: ", studentPerson.getName());
                    studentWork.put(" Priezvisko: ", studentPerson.getSurname());
                    studentWork.put(" Firma: ", company.getName());
                    studentWork.put(" Stav: ", workDTO.getState());
                    studentWork.put(" Katedra: ", studentAcc.getInstitute());
                    
                    studentWorkArray.put(studentWork);
                }
            }

            return studentWorkArray.toString();
        } else {
            responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
            responseObject.put("responseMessage", "Nemáš prístup k zobrazeniu študentov na praxi podľa zadanej katedry.");

            return responseObject.toString();
        }
    }

    /**
     * @apiNote vytvorenie reportu za pracovisko
     * @param requestString
     * @return
     */
    @PostMapping("/vytvorReportZaPracovisko")
    public String vytvorReportZaPracovisko(@RequestBody String requestString) {
        JSONObject json = new JSONObject(requestString);
        int accountId = json.getInt("account_id");
        AccountDTO acc = accountService.getAccountId(accountId);

        JSONObject responseObject = new JSONObject();

        if (acc.getRole().equals("veduci_pracoviska")) {
            JSONObject contentJson = json.getJSONObject("content");
            String timestamp = json.getString("timestamp");
            String type = json.getString("type");

            ReportDTO newReport = new ReportDTO();

            newReport.setCreatoraccount_id_account(accountId);
            newReport.setContent(contentJson.toString());
            newReport.setTimestamp(timestamp);
            newReport.setType(type);

            reportService.saveReport(newReport);

            responseObject.put("response_code", RESPONSECODE_OK);
            responseObject.put("responseMessage", "Report úspešne vytvorený.");

            return responseObject.toString();
        } else {
            responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
            responseObject.put("responseMessage", "Nemáš povolenie pre vytváranie reportu za pracovisko.");

            return responseObject.toString();
        }
    }

    /**
     * @apiNote zobrazovanie spatnych vazieb zastupcu zadanej firmy
     * @param requestString
     * @return
     */
    @GetMapping("/zobrazSpatneVazbyZastupcuFirmy")
    public String zobrazSpatneVazbyZastupcuFirmy(@RequestBody String requestString) {
        JSONObject json = new JSONObject(requestString);
        int id = json.getInt("account_id");
        String menoFirmy = json.getString("meno_firmy");
        AccountDTO acc = accountService.getAccountId(id);
        List<WorkDTO> works = workService.getAllWorks();

        JSONObject responseObject = new JSONObject();
        JSONArray feedbackCompanyArray = new JSONArray();

        if(acc.getRole().equals("veduci_pracoviska")) {
            for (WorkDTO workDTO : works) {
                JSONObject feedbackCompany = new JSONObject();
                OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());
                PersonDTO person = personService.getPersonById(company.getRepresentative_id_person());
                AccountDTO account = accountService.getAccountId(workDTO.getAccount_id_account());
                PersonDTO studentPerson = personService.getPersonById(account.getPerson_id_person());

                if(company.getName().equals(menoFirmy)) {
                    feedbackCompany.put(" Zástupca Meno: ", person.getName());
                    feedbackCompany.put(" Zástupca Priezvisko: ", person.getSurname());
                    feedbackCompany.put(" Študent Meno: ", studentPerson.getName());
                    feedbackCompany.put(" Študent priezvisko: ", studentPerson.getSurname());
                    feedbackCompany.put(" Spätná väzba: ", workDTO.getFeedback_company());

                    feedbackCompanyArray.put(feedbackCompany);
                }
            }

            return feedbackCompanyArray.toString();
        } else {
            responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
            responseObject.put("responseMessage", "Nemáš povolenie na zobrazenie spätných väzieb zástupcu firmy.");

            return responseObject.toString();
        }
    }
}
