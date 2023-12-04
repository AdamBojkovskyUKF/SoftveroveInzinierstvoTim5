package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.github.javafaker.Faker;

import SoftveroveInzinierstvoTim5.RestAPI.dto.*;
import SoftveroveInzinierstvoTim5.RestAPI.model.*;
import SoftveroveInzinierstvoTim5.RestAPI.service.*;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils.Null;
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
    @Autowired
    DefaultStudy_ProgramService study_ProgramService;
    @Autowired
    DefaultSubject_For_PracticeService subject_For_PracticeService;
    @Autowired
    DefaultCommunicationService communicationService;

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

        AccountDTO adminAcc = new AccountDTO();
        adminAcc.setEmail_address("admin@admin.com");
        adminAcc.setInstitute("YoMama");
        adminAcc.setPassword("admin");
        adminAcc.setRole("admin");
        adminAcc.setSignup_year("1234");
        adminAcc.setStudy_level("1234");
        accountService.saveAccount(adminAcc);
    
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
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

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
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
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
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);
            
            List<WorkDTO> works = workService.getAllWorks();
            JSONArray studentWorkArray = new JSONArray();
            
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
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
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
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            List<WorkDTO> works = workService.getAllWorks();
            JSONArray studentWorkArray = new JSONArray();

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
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
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
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            String institut = json.getString("institute");
            AccountDTO acc = accountService.getAccountId(id);
            
            List<WorkDTO> works = workService.getAllWorks();
            JSONArray studentWorkArray = new JSONArray();
            
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
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
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
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int accountId = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(accountId);

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
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
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
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            String menoFirmy = json.getString("meno_firmy");
            AccountDTO acc = accountService.getAccountId(id);
            List<WorkDTO> works = workService.getAllWorks();

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
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }

    /**
     * @apiNote priradovanie povereneho pracovnika pracoviska ku ponuke praxi
     * @param requestString
     * @return
     */
    @PostMapping("/priradPoverenehoPracovnika")
    public String priradPoverenehoPracovnika(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            int id_offer = json.getInt("offer_id");
            int overseer_id_person = json.getInt("person_id");

            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                OfferDTO offer = offerService.getOfferId(id_offer);
                if(offer.getOverseer_id_person() != null) {
                    responseObject.put("response_code", RESPONSECODE_ERROR);
                    responseObject.put("responseMessage", "Zadaná ponuka už má prideleného povereného pracovníka katedry.");

                    return responseObject.toString();
                } else {
                    offer.setOverseer_id_person(overseer_id_person);
                    offerService.saveOffer(offer);

                    responseObject.put("response_code", RESPONSECODE_OK);
                    responseObject.put("responseMessage", "Úspešne si priradil povereného pracovníka katedry danej pracovnej ponuke.");

                    return responseObject.toString();
                }
            } else {
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie priraďovať poverených pracovníkov katedier k ponukám práce.");

                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }

    /**
     * @apiNote zobrazovanie praxe v danej organizacii
     * @param requestString
     * @return
     */
    @GetMapping("/zobrazitPraxeVoSvojejOrganizacii")
    public String zobrazitPraxeVoSvojejOrganizacii(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);
            List<WorkDTO> works = workService.getAllWorks();

            JSONArray worksArray = new JSONArray();

            if(acc.getRole().equals("zastupca_firmy")) {
                for (WorkDTO workDTO : works) {
                    JSONObject work = new JSONObject();
                    OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                    CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());
                    AccountDTO student = accountService.getAccountId(workDTO.getAccount_id_account());
                    PersonDTO studentPerson = personService.getPersonById(student.getPerson_id_person());
                    PersonDTO reprePerson = personService.getPersonById(acc.getPerson_id_person());

                    if(reprePerson.getId_person() == company.getRepresentative_id_person()) {
                        work.put(" Kontrakt: ", workDTO.getContract());
                        work.put(" Stav: ", workDTO.getState());
                        work.put(" Popis práce: ", workDTO.getWork_log());
                        work.put(" Spätná väzba študent: ", workDTO.getFeedback_student());
                        work.put(" Spätná väzba firma: ", workDTO.getFeedback_company());
                        work.put(" Známka: ", workDTO.getMark());
                        work.put(" Rok dokončenia: ", workDTO.getCompletion_year());
                        work.put(" Meno študenta: ", studentPerson.getName());
                        work.put(" Priezvisko študenta: ", studentPerson.getSurname());

                        worksArray.put(work);
                    } 
                }

                return worksArray.toString();
            } else {
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazenie praxí v danej organizácií.");
                return responseObject.toString();
            }


        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }

    /**
     * @apiNote vytvrania spatnej vazby od firiem k pracam
     * @param requestString
     * @return
     */
    @PostMapping("/vytvorSpatnuVazbuPraxe")
    public String vytvorSpatnuVazbuPraxe(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            int id_work = json.getInt("work_id");
            String spatnaVazba = json.getString("spatna_vazba");

            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("zastupca_firmy")) {
                WorkDTO work = workService.getWorkById(id_work);
                OfferDTO offer = offerService.getOfferId(work.getOffer_id_offer());
                CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());
                PersonDTO reprePerson = personService.getPersonById(acc.getPerson_id_person());
                
                if(company.getRepresentative_id_person() == reprePerson.getId_person()) {
                    if(work.getFeedback_company() != null) {
                        responseObject.put("response_code", RESPONSECODE_ERROR);
                        responseObject.put("responseMessage", "Zadaná práca už má vytvorenú spätnú väzbu od firmy.");
                        return responseObject.toString();
                    } else {
                        work.setFeedback_company(spatnaVazba);
                        workService.saveWork(work);

                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", "Úspešne si vytvoril spätnú väzbu firmy pre zadanú prax.");

                        return responseObject.toString();
                    }
                } else {
                    responseObject.put("response_code", RESPONSECODE_ERROR);
                    responseObject.put("responseMessage", "Nemáš prístup k tejto praxi, pretože nie si zástupca firmy, v ktorej sa vykonáva.");

                    return responseObject.toString();
                }

            } else {
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na vytváranie spätných väzieb firiem.");
                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }

    @GetMapping("/odsuhlasitPracovnyVykaz")
    public String odsuhlasitPracovnyVykaz(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            int work = json.getInt("work_id");
            AccountDTO acc = accountService.getAccountId(id);
            WorkDTO workDTO = workService.getWorkById(work);

            if(acc.getRole().equals("zastupca_firmy")) {
                OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());
                PersonDTO reprePerson = personService.getPersonById(acc.getPerson_id_person());

                if(reprePerson.getId_person() == company.getRepresentative_id_person()) {
                    if(workDTO.getWork_log().length() > 100) {
                        responseObject.put("responseMessage", "Pracovný výkaz odsúhlasený.");
                        return responseObject.toString();
                    } else {
                        responseObject.put("responseMessage", "Pracovný výkaz zamietnutý.");
                        return responseObject.toString();
                    }
                } else {
                    responseObject.put("response_code", RESPONSECODE_ERROR);
                    responseObject.put("responseMessage", "Nemáš prístup k tejto práci, pretože nie si zástupca danej firmy.");
                    return responseObject.toString();
                }


            } else {
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenia odsúhlasovať pracovné výkazy.");
                return responseObject.toString();
            }
            
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }


    }

    /**
     * @apiNote Direct create method for each class in DB
     * @param entity request JSON
     * @param classType type of class we want to create using CRUD
     * @return error/success/permision_denied JSON
     */
    @PostMapping(value="/adminCRUD/create/{classType}")
    public String createClass(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if(acc.getRole().equals("admin")){
                switch (classType) {
                    case "account":
                        AccountDTO accountDTO = new AccountDTO();
                        accountDTO.setCompany_id_company(requestJsonObject.getInt("company_id"));
                        accountDTO.setEmail_address(requestJsonObject.getString("email"));
                        accountDTO.setInstitute(requestJsonObject.getString("institute"));
                        accountDTO.setPassword(requestJsonObject.getString("password"));
                        accountDTO.setPerson_id_person(requestJsonObject.getInt("person_id"));
                        accountDTO.setRole(requestJsonObject.getString("role"));
                        accountDTO.setSignup_year(requestJsonObject.getString("signup_year"));
                        accountDTO.setStudy_level(requestJsonObject.getString("study_level"));
                        accountDTO.setStudy_program_idstudy_program(requestJsonObject.getInt("studyProgram_id"));
                        accountService.saveAccount(accountDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Account.class.getName() + " instance created");
                        return responseObject.toString();
                    case "company":
                        CompanyDTO companyDTO = new CompanyDTO();
                        companyDTO.setAddress(requestJsonObject.getString("address"));
                        companyDTO.setName(requestJsonObject.getString("name"));
                        companyDTO.setRepresentative_id_person(requestJsonObject.getInt("representative_id"));
                        companyService.saveCompany(companyDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Company.class.getName() + " instance created");
                        return responseObject.toString();
                    case "offer":
                        OfferDTO offerDTO = new OfferDTO();
                        offerDTO.setCompany_id_company(requestJsonObject.getInt("offer_id"));
                        offerDTO.setContract_type(requestJsonObject.getString("offer_type"));
                        offerDTO.setDescription(requestJsonObject.getString("description"));
                        offerDTO.setOverseer_id_person(requestJsonObject.getInt("overseer_id"));
                        offerDTO.setPosition(requestJsonObject.getString("position"));
                        offerService.saveOffer(offerDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Offer.class.getName() + " instance created");
                        return responseObject.toString();
                    case "person":
                        PersonDTO personDTO = new PersonDTO();
                        personDTO.setAddress(requestJsonObject.getString("address"));
                        personDTO.setEmail(requestJsonObject.getString("email"));
                        personDTO.setName(requestJsonObject.getString("name"));
                        personDTO.setPhone_number(requestJsonObject.getString("phone_number"));
                        personDTO.setSurname(requestJsonObject.getString("surname"));
                        personService.savePerson(personDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Person.class.getName() + " instance created");
                        return responseObject.toString();
                    case "report":
                        ReportDTO reportDTO = new ReportDTO();
                        reportDTO.setContent(requestJsonObject.getString("content"));
                        reportDTO.setCreatoraccount_id_account(requestJsonObject.getInt("creator_id"));
                        reportDTO.setTimestamp(requestJsonObject.getString("timestamp"));
                        reportDTO.setType(requestJsonObject.getString("type"));
                        reportService.saveReport(reportDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Report.class.getName() + " instance created");
                        return responseObject.toString();
                    case "study_program":
                        Study_ProgramDTO study_ProgramDTO = new Study_ProgramDTO();
                        study_ProgramDTO.setName(requestJsonObject.getString("name"));
                        study_ProgramService.saveStudyProgram(study_ProgramDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Study_Program.class.getName() + " instance created");
                        return responseObject.toString();
                    case "subject_for_practice":
                        Subject_For_PracticeDTO subject_For_PracticeDTO = new Subject_For_PracticeDTO();
                        subject_For_PracticeDTO.setCredits(requestJsonObject.getInt("credits"));
                        subject_For_PracticeDTO.setName(requestJsonObject.getString("name"));
                        subject_For_PracticeDTO.setStudy_program_idstudy_program(requestJsonObject.getInt("study_program_id"));
                        subject_For_PracticeService.saveSubjectForPractice(subject_For_PracticeDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Subject_for_Practice.class.getName() + " instance created");
                        return responseObject.toString();
                    case "work":
                        WorkDTO workDTO = new WorkDTO();
                        workDTO.setAccount_id_account(requestJsonObject.getInt("work_account_id"));
                        workDTO.setCompletion_year(requestJsonObject.getString("completion_year"));
                        workDTO.setContract(requestJsonObject.getString("contract"));
                        workDTO.setFeedback_company(requestJsonObject.getString("feedback_company"));
                        workDTO.setFeedback_student(requestJsonObject.getString("feedback_student"));
                        workDTO.setMark(requestJsonObject.getString("mark"));
                        workDTO.setOffer_id_offer(requestJsonObject.getInt("offer_id"));
                        workDTO.setState(requestJsonObject.getString("state"));
                        workDTO.setWork_log(requestJsonObject.getString("work_log"));
                        workService.saveWork(workDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Work.class.getName() + " instance created");
                        return responseObject.toString();
                    case "communication":
                        CommunicationDTO communicationDTO =  new CommunicationDTO();
                        communicationDTO.setAccount_id_account(requestJsonObject.getInt("account_1"));
                        communicationDTO.setAccount_id_account1(requestJsonObject.getInt("account_2"));
                        communicationDTO.setMessages(requestJsonObject.getString("messages"));
                        communicationService.saveCommunication(communicationDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Communication.class.getName() + " instance created");
                        return responseObject.toString();
                    default:
                        responseObject.put("response_code", RESPONSECODE_ERROR);
                        responseObject.put("responseMessage", "Invalid class type");
                        return responseObject.toString();
                }
            }else{
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na vytváranie tried touto metódou");

                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }

    /**
     * @apiNote Direct read method for each class in DB
     * @param entity request JSON
     * @param classType type of class we want to read using CRUD
     * @return error/permission_denied JSON ... if successful will return JSON of class instance
     */
    @GetMapping(value="/adminCRUD/read/{classType}")
    public String readClass(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if(acc.getRole().equals("admin")){
                switch (classType) {
                    case "account":
                        AccountDTO accountDTO = accountService.getAccountId(requestJsonObject.getInt("id"));
                        JSONObject responseAccount = new JSONObject(accountDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("object", responseAccount);
                        return responseObject.toString();
                    case "company":
                        CompanyDTO companyDTO = companyService.getCompanyId(requestJsonObject.getInt("id"));
                        JSONObject responseCompany = new JSONObject(companyDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("object", responseCompany);
                        return responseObject.toString();
                    case "offer":
                        OfferDTO offerDTO = offerService.getOfferId(requestJsonObject.getInt("id"));
                        JSONObject responseOffer = new JSONObject(offerDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("object", responseOffer);
                        return responseObject.toString();
                    case "person":
                        PersonDTO personDTO = personService.getPersonById(requestJsonObject.getInt("id"));
                        JSONObject responsePerson = new JSONObject(personDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("object", responsePerson);
                        return responseObject.toString();
                    case "report":
                        ReportDTO reportDTO = reportService.getReportById(requestJsonObject.getInt("id"));
                        JSONObject responseReport = new JSONObject(reportDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("object", responseReport);
                        return responseObject.toString();
                    case "study_program":
                        Study_ProgramDTO study_ProgramDTO = study_ProgramService.getStudyProgramById(requestJsonObject.getInt("id"));
                        JSONObject responseStudyProgram = new JSONObject(study_ProgramDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("object", responseStudyProgram);
                        return responseObject.toString();
                    case "subject_for_practice":
                        Subject_For_PracticeDTO subject_For_PracticeDTO = subject_For_PracticeService.getSubjectForPracticeById(requestJsonObject.getInt("id"));
                        JSONObject responseSubjectForPractice = new JSONObject(subject_For_PracticeDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("object", responseSubjectForPractice);
                        return responseObject.toString();
                    case "work":
                        WorkDTO workDTO = workService.getWorkById(requestJsonObject.getInt("id"));
                        JSONObject responseWork = new JSONObject(workDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("object", responseWork);
                        return responseObject.toString();
                    case "communication":
                        CommunicationDTO communicationDTO = communicationService.getCommunicationId(requestJsonObject.getInt("id"));
                        JSONObject responseCommunication = new JSONObject(communicationDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("object", responseCommunication);
                        return responseObject.toString();
                    default:
                        responseObject.put("response_code", RESPONSECODE_ERROR);
                        responseObject.put("responseMessage", "Invalid class type");
                        return responseObject.toString();
                }
            }else{
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na čítanie tried touto metódou");

                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }
    
    /**
     * @apiNote Direct update method for each class in DB
     * @param entity request JSON
     * @param classType type of update we want to update using CRUD
     * @return error/success/permision_denied JSON
     */
    @PostMapping(value="/adminCRUD/update/{classType}")
    public String updateClass(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if(acc.getRole().equals("admin")){
                switch (classType) {
                    case "account":
                        AccountDTO accountDTO = accountService.getAccountId(requestJsonObject.getInt("id"));
                        accountDTO.setCompany_id_company(requestJsonObject.getInt("company_id"));
                        accountDTO.setEmail_address(requestJsonObject.getString("email"));
                        accountDTO.setInstitute(requestJsonObject.getString("institute"));
                        accountDTO.setPassword(requestJsonObject.getString("password"));
                        accountDTO.setPerson_id_person(requestJsonObject.getInt("person_id"));
                        accountDTO.setRole(requestJsonObject.getString("role"));
                        accountDTO.setSignup_year(requestJsonObject.getString("signup_year"));
                        accountDTO.setStudy_level(requestJsonObject.getString("study_level"));
                        accountDTO.setStudy_program_idstudy_program(requestJsonObject.getInt("studyProgram_id"));
                        accountService.saveAccount(accountDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Account.class.getName() + " instance updated");
                        return responseObject.toString();
                    case "company":
                        CompanyDTO companyDTO = companyService.getCompanyId(requestJsonObject.getInt("id"));
                        companyDTO.setAddress(requestJsonObject.getString("address"));
                        companyDTO.setName(requestJsonObject.getString("name"));
                        companyDTO.setRepresentative_id_person(requestJsonObject.getInt("representative_id"));
                        companyService.saveCompany(companyDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Company.class.getName() + " instance updated");
                        return responseObject.toString();
                    case "offer":
                        OfferDTO offerDTO = offerService.getOfferId(requestJsonObject.getInt("id"));
                        offerDTO.setCompany_id_company(requestJsonObject.getInt("offer_id"));
                        offerDTO.setContract_type(requestJsonObject.getString("offer_type"));
                        offerDTO.setDescription(requestJsonObject.getString("description"));
                        offerDTO.setOverseer_id_person(requestJsonObject.getInt("overseer_id"));
                        offerDTO.setPosition(requestJsonObject.getString("position"));
                        offerService.saveOffer(offerDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Offer.class.getName() + " instance updated");
                        return responseObject.toString();
                    case "person":
                        PersonDTO personDTO = personService.getPersonById(requestJsonObject.getInt("id"));
                        personDTO.setAddress(requestJsonObject.getString("address"));
                        personDTO.setEmail(requestJsonObject.getString("email"));
                        personDTO.setName(requestJsonObject.getString("name"));
                        personDTO.setPhone_number(requestJsonObject.getString("phone_number"));
                        personDTO.setSurname(requestJsonObject.getString("surname"));
                        personService.savePerson(personDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Person.class.getName() + " instance updated");
                        return responseObject.toString();
                    case "report":
                        ReportDTO reportDTO = reportService.getReportById(requestJsonObject.getInt("id"));
                        reportDTO.setContent(requestJsonObject.getString("content"));
                        reportDTO.setCreatoraccount_id_account(requestJsonObject.getInt("creator_id"));
                        reportDTO.setTimestamp(requestJsonObject.getString("timestamp"));
                        reportDTO.setType(requestJsonObject.getString("type"));
                        reportService.saveReport(reportDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Report.class.getName() + " instance updated");
                        return responseObject.toString();
                    case "study_program":
                        Study_ProgramDTO study_ProgramDTO = study_ProgramService.getStudyProgramById(requestJsonObject.getInt("id"));
                        study_ProgramDTO.setName(requestJsonObject.getString("name"));
                        study_ProgramService.saveStudyProgram(study_ProgramDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Study_Program.class.getName() + " instance updated");
                        return responseObject.toString();
                    case "subject_for_practice":
                        Subject_For_PracticeDTO subject_For_PracticeDTO = subject_For_PracticeService.getSubjectForPracticeById(requestJsonObject.getInt("id"));
                        subject_For_PracticeDTO.setCredits(requestJsonObject.getInt("credits"));
                        subject_For_PracticeDTO.setName(requestJsonObject.getString("name"));
                        subject_For_PracticeDTO.setStudy_program_idstudy_program(requestJsonObject.getInt("study_program_id"));
                        subject_For_PracticeService.saveSubjectForPractice(subject_For_PracticeDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Subject_for_Practice.class.getName() + " instance updated");
                        return responseObject.toString();
                    case "work":
                        WorkDTO workDTO = workService.getWorkById(requestJsonObject.getInt("id"));
                        workDTO.setAccount_id_account(requestJsonObject.getInt("work_account_id"));
                        workDTO.setCompletion_year(requestJsonObject.getString("completion_year"));
                        workDTO.setContract(requestJsonObject.getString("contract"));
                        workDTO.setFeedback_company(requestJsonObject.getString("feedback_company"));
                        workDTO.setFeedback_student(requestJsonObject.getString("feedback_student"));
                        workDTO.setMark(requestJsonObject.getString("mark"));
                        workDTO.setOffer_id_offer(requestJsonObject.getInt("offer_id"));
                        workDTO.setState(requestJsonObject.getString("state"));
                        workDTO.setWork_log(requestJsonObject.getString("work_log"));
                        workService.saveWork(workDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Work.class.getName() + " instance updated");
                        return responseObject.toString();
                    case "communication":
                        CommunicationDTO communicationDTO = communicationService.getCommunicationId(requestJsonObject.getInt("id"));
                        communicationDTO.setAccount_id_account(requestJsonObject.getInt("account_1"));
                        communicationDTO.setAccount_id_account1(requestJsonObject.getInt("account_2"));
                        communicationDTO.setMessages(requestJsonObject.getString("messages"));
                        communicationService.saveCommunication(communicationDTO);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Communication.class.getName() + " instance updated");
                        return responseObject.toString();
                    default:
                        responseObject.put("response_code", RESPONSECODE_ERROR);
                        responseObject.put("responseMessage", "Invalid class type");
                        return responseObject.toString();
                }
            }else{
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na aktualizovanie tried touto metódou");

                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }
    
    /**
     * @apiNote Direct delete method for each class in DB
     * @param entity request JSON
     * @param classType type of update we want to delete using CRUD
     * @return error/success/permision_denied JSON
     */
    @GetMapping(value="/adminCRUD/delete/{classType}")
    public String deleteClass(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if(acc.getRole().equals("admin")){
                switch (classType) {
                    case "account":
                        accountService.deleteAccount(requestJsonObject.getInt("id"));
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Account.class.getName() + " instance deleted");
                        return responseObject.toString();
                    case "company":
                        companyService.deleteCompany(requestJsonObject.getInt("id"));
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Company.class.getName() + " instance deleted");
                        return responseObject.toString();
                    case "offer":
                        offerService.deleteOffer(requestJsonObject.getInt("id"));
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Offer.class.getName() + " instance deleted");
                        return responseObject.toString();
                    case "person":
                        personService.deletePerson(requestJsonObject.getInt("id"));
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Person.class.getName() + " instance deleted");
                        return responseObject.toString();
                    case "report":
                        reportService.deleteReport(requestJsonObject.getInt("id"));
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Report.class.getName() + " instance deleted");
                        return responseObject.toString();
                    case "study_program":
                        study_ProgramService.deleteStudyProgram(requestJsonObject.getInt("id"));
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Study_Program.class.getName() + " instance deleted");
                        return responseObject.toString();
                    case "subject_for_practice":
                        subject_For_PracticeService.deleteSubjectForPractice(requestJsonObject.getInt("id"));
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Subject_for_Practice.class.getName() + " instance deleted");
                        return responseObject.toString();
                    case "work":
                        workService.deleteWork(requestJsonObject.getInt("id"));
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Work.class.getName() + " instance deleted");
                        return responseObject.toString();
                    case "communication":
                        communicationService.deleteCommunication(requestJsonObject.getInt("id"));
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("responseMessage", Communication.class.getName() + " instance deleted");
                        return responseObject.toString();
                    default:
                        responseObject.put("response_code", RESPONSECODE_ERROR);
                        responseObject.put("responseMessage", "Invalid class type");
                        return responseObject.toString();
                }
            }else{
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na vymazávanie tried touto metódou");

                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }

    @GetMapping(value = "/adminReport/{classType}")
    public String reportGenerator(@RequestBody String entity, @PathVariable String classType){
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if(acc.getRole().equals("admin")){
                switch (classType) {
                    case "account":
                        List <AccountDTO> accountDTOs = accountService.getAllAccounts();
                        JSONArray accountJsonArray = new JSONArray(accountDTOs);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("objectArray", accountJsonArray);
                        return responseObject.toString();
                    case "company":
                        List <CompanyDTO> companyDTOs = companyService.getAllCompanies();
                        JSONArray companyJsonArray = new JSONArray(companyDTOs);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("objectArray", companyJsonArray);
                        return responseObject.toString();
                    case "offer":
                        List <OfferDTO> offerDTOs = offerService.getAllOffers();
                        JSONArray offerJsonArray = new JSONArray(offerDTOs);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("objectArray", offerJsonArray);
                        return responseObject.toString();
                    case "person":
                        List <PersonDTO> personDTOs = personService.getAllPersons();
                        JSONArray personJsonArray = new JSONArray(personDTOs);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("objectArray", personJsonArray);
                        return responseObject.toString();
                    case "report":
                        List <ReportDTO> reportDTOs = reportService.getAllReports();
                        JSONArray reportJsonArray = new JSONArray(reportDTOs);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("objectArray", reportJsonArray);
                        return responseObject.toString();
                    case "study_program":
                        List <Study_ProgramDTO> study_ProgramDTOs = study_ProgramService.getAllStudyPrograms();
                        JSONArray studyProgramJsonArray = new JSONArray(study_ProgramDTOs);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("objectArray", studyProgramJsonArray);
                        return responseObject.toString();
                    case "subject_for_practice":
                        List <Subject_For_PracticeDTO> subject_For_PracticeDTOs = subject_For_PracticeService.getAllSubjectsForPractice();
                        JSONArray subjectForPracticeJsonArray= new JSONArray(subject_For_PracticeDTOs);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("objectArray", subjectForPracticeJsonArray);
                        return responseObject.toString();
                    case "work":
                        List <WorkDTO> workDTOs = workService.getAllWorks();
                        JSONArray workJsonArray = new JSONArray(workDTOs);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("objectArray", workJsonArray);
                        return responseObject.toString();
                    case "communication":
                        List <CommunicationDTO> communicationDTOs = communicationService.getAllCommunications();
                        JSONArray communicationJsonArray = new JSONArray(communicationDTOs);
                        responseObject.put("response_code", RESPONSECODE_OK);
                        responseObject.put("objectArray", communicationJsonArray);
                        return responseObject.toString();
                    default:
                        responseObject.put("response_code", RESPONSECODE_ERROR);
                        responseObject.put("responseMessage", "Invalid class type");
                        return responseObject.toString();
                }
            }else{
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na generovanie reportov touto metódou");

                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }
    
    @GetMapping(value = "/student/zobrazponuky")
    public String viewOffers(@RequestBody String entity, @PathVariable String classType){
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if(acc.getRole().equals("student")){
                List <OfferDTO> offerDTOs = offerService.getAllOffers();
                    JSONArray offerJsonArray = new JSONArray(offerDTOs);
                    responseObject.put("response_code", RESPONSECODE_OK);
                    responseObject.put("objectArray", offerJsonArray);
                    return responseObject.toString();
            }else{
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazovanie ponúk touto metódou");

                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }
    
    @GetMapping(value = "/student/vytvoritRobotuzPonuky")
    public String createWorkFromOffer(@RequestBody String entity, @PathVariable String classType){
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if(acc.getRole().equals("student")){
                WorkDTO workDTO = new WorkDTO();
                workDTO.setAccount_id_account(acc_id);
                workDTO.setOffer_id_offer(requestJsonObject.getInt("offer_id"));
                workDTO.setContract(requestJsonObject.getString("contract"));
                workDTO.setState(requestJsonObject.getString("state"));
                workDTO.setWork_log(requestJsonObject.getString("work_log"));
                if (!requestJsonObject.getString("completion_year").isEmpty() && requestJsonObject.getString("completion_year") != null) {
                    workDTO.setCompletion_year(requestJsonObject.getString("completion_year"));
                }
                workService.saveWork(workDTO);
                responseObject.put("response_code", RESPONSECODE_OK);
                responseObject.put("responseMessage", Work.class.getName() + " instance created");
                return responseObject.toString();
            }else{
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazovanie ponúk touto metódou");

                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }
    @GetMapping(value = "/student/vybratProgram")
    public String chooseStudyProgram(@RequestBody String entity, @PathVariable String classType){
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if(acc.getRole().equals("student")){
                AccountDTO accountDTO = accountService.getAccountId(acc_id);
                if(accountDTO.getStudy_program_idstudy_program() == null || accountDTO.getStudy_program_idstudy_program() == 0){
                    accountDTO.setStudy_program_idstudy_program(requestJsonObject.getInt("study_program_id"));
                    accountService.saveAccount(accountDTO);
                    responseObject.put("response_code", RESPONSECODE_OK);
                    responseObject.put("responseMessage", Account.class.getName() + " instance updated");
                    return responseObject.toString();
                }else{
                    responseObject.put("response_code", RESPONSECODE_ERROR);
                    responseObject.put("responseMessage", "Účet už má priradený študijný program");
                    return responseObject.toString();
                }
                
            }else{
                responseObject.put("response_code", RESPONSECODE_PERMISSION_DENIED);
                responseObject.put("responseMessage", "Nemáš povolenie na pridavanie študijných programov touto metódou");
                return responseObject.toString();
            }
        } catch (Exception e) {
            responseObject.put("response_code", RESPONSECODE_ERROR);
            responseObject.put("responseMessage", e.getMessage());
            return responseObject.toString();
        }
    }
}
