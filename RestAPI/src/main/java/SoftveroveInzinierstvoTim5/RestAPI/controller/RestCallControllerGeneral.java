package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;

import SoftveroveInzinierstvoTim5.RestAPI.APISchemas.LoginCredentialsSchemaAfterLogin;
import SoftveroveInzinierstvoTim5.RestAPI.APISchemas.LoginCredentialsSchemaBeforeLogin;
import SoftveroveInzinierstvoTim5.RestAPI.dto.*;
import SoftveroveInzinierstvoTim5.RestAPI.model.Account;
import SoftveroveInzinierstvoTim5.RestAPI.model.Communication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.*;


@RestController
public class RestCallControllerGeneral extends GeneralController {

    @GetMapping("/test")
    public ResponseEntity<?> handleTestRequest() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Seeder for initializing Database")
    @ApiResponse(responseCode = "200", description = "Database initalized with seed data")
    @RequestMapping(value = "/dataSeed", method = {RequestMethod.GET})
    public ResponseEntity<?> handleDataSeedRequest(){
        //if (!personService.getAllPersons().isEmpty()) {
            for (int i = 0; i < 100; i++) {
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
            List<PersonDTO> persons =  personService.getAllPersons();
            for (PersonDTO personDTO : persons) {
                AccountDTO acc = new AccountDTO();
                Faker faker = new Faker();
                acc.setPerson_id_person(personDTO.getId_person());

                acc.setEmail_address(personDTO.getEmail());
                acc.setInstitute(institute[faker.number().numberBetween(0, 3)]);
                acc.setPassword("password");
                acc.setStudy_level("placeholder");
                acc.setSignup_year(faker.number().numberBetween(2000, 2031)+"");
                acc.setRole(roly[faker.number().numberBetween(0, 5)]);
                accountService.saveAccount(acc);
            }
        //}
        List<AccountDTO> accountDTOs = accountService.getAllAccounts();

        AccountDTO adminAcc = new AccountDTO();
        adminAcc.setEmail_address("admin@admin.com");
        adminAcc.setInstitute("YoMama");
        adminAcc.setPassword("admin");
        adminAcc.setRole("admin");
        adminAcc.setSignup_year("1234");
        adminAcc.setStudy_level("1234");
        accountService.saveAccount(adminAcc);
        
        for (AccountDTO accountDTO : accountDTOs) {
            Faker faker = new Faker();
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setName(faker.company().name());
            companyDTO.setAddress(faker.address().streetAddress());
            if ("zastupca_firmy".equals(accountDTO.getRole())) {
                companyDTO.setRepresentative_id_person(accountDTO.getPerson_id_person());
                companyDTO = companyService.saveCompany(companyDTO);
                accountDTO.setCompany_id_company(companyDTO.getId_company());
                accountService.saveAccount(accountDTO);
            }
            
        }
        List<CompanyDTO> companyDTOs = companyService.getAllCompanies();
        for (CompanyDTO companyDTO : companyDTOs) {
            Faker faker = new Faker();
            for (int i = 0; i < 5; i++) {
                OfferDTO offerDTO = new OfferDTO();
                offerDTO.setCompany_id_company(companyDTO.getId_company());
                offerDTO.setContract_type(contractTypes[faker.number().numberBetween(0, 4)]);
                offerDTO.setDescription("Nejaky popis prace");
                offerDTO.setPosition(positions[faker.number().numberBetween(0, 2)]);
                offerService.saveOffer(offerDTO);
            }
        }
        List<OfferDTO> offerDTOs = offerService.getAllOffers();
        int iter = 0;
        Iterator<AccountDTO> iterator = accountDTOs.iterator();
        for (OfferDTO offerDTO : offerDTOs) {
            iter++;
            if((iter % 5) == 0){
            Faker faker = new Faker();
            WorkDTO workDTO = new WorkDTO();
            workDTO.setOffer_id_offer(offerDTO.getId_offer());
            workDTO.setState(workStates[faker.number().numberBetween(0, 2)]);
            workDTO.setWork_log("");
            workDTO.setContract("Contract velmi silny".getBytes());
            AccountDTO accountStudent;
            AccountDTO confirmedStudentDTO = new AccountDTO();
            if(iterator.hasNext()){
                accountStudent = iterator.next();
                if("student".equals(accountStudent.getRole())){
                    confirmedStudentDTO = accountStudent;
                }
            }
            workDTO.setAccount_id_account(confirmedStudentDTO.getId_account());
            workService.saveWork(workDTO);
            }
        }
        for (int i = 0; i < 10; i++) {
                Faker faker = new Faker();
                Study_ProgramDTO study_ProgramDTO = new Study_ProgramDTO();
                study_ProgramDTO.setName(programNames[faker.number().numberBetween(0, 2)]);
                study_ProgramService.saveStudyProgram(study_ProgramDTO);
            }
            List<Study_ProgramDTO> study_ProgramDTOs = study_ProgramService.getAllStudyPrograms();
        for (Study_ProgramDTO study_ProgramDTO : study_ProgramDTOs) {
            Faker faker = new Faker();
            Subject_For_PracticeDTO subject_For_PracticeDTO = new Subject_For_PracticeDTO();
            subject_For_PracticeDTO.setName(sfps[faker.number().numberBetween(0, 4)]);
            subject_For_PracticeDTO.setStudy_program_idstudy_program(study_ProgramDTO.getIdstudy_program());
            subject_For_PracticeDTO.setCredits(faker.number().numberBetween(3, 6));
            subject_For_PracticeService.saveSubjectForPractice(subject_For_PracticeDTO);
        }

        for (AccountDTO accountDTO : accountDTOs) {
            if("zastupca_firmy".equals(accountDTO.getRole()) || "povereny_pracovnik".equals(accountDTO.getRole()) || "veduci_pracoviska".equals(accountDTO.getRole())){
            Faker faker = new Faker();
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setCreatoraccount_id_account(accountDTO.getId_account());
            JSONObject report = new JSONObject();
            report.put("var1", "var1_value");
            report.put("var2", "var2_value");
            report.put("var3", "var3_value");
            report.put("var4", "var4_value");
            reportDTO.setContent(report.toString());
            reportDTO.setTimestamp(new Date().toString());
            reportDTO.setType(reportTypes[faker.number().numberBetween(0, 3)]);
            reportService.saveReport(reportDTO);
            }
        }


        return new ResponseEntity<>("Data Loaded", HttpStatus.OK);
    }

    @Operation(summary = "Login of a user.")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Login Successful")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Login Failed -> Username + Password Combination not found")
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaBeforeLogin.class}))
    })
    @GetMapping(value = "/login")
    public ResponseEntity<?> handleLoginRequest(@RequestBody String RequestBody) {
        JSONObject json = new JSONObject(RequestBody);
        String password = json.getString("password");
        String email = json.getString("email");
        JSONObject responseObject = new JSONObject();
        List<AccountDTO> accounts = accountService.getAllAccounts();
        for (AccountDTO accountDTO : accounts) {
            if (accountDTO.getEmail_address().equals(email) && accountDTO.getPassword().equals(password)) {
                responseObject.put("reponseMessage", "Login Successful");
                JSONObject reponseValues = new JSONObject();
                reponseValues.put("account_id", accountDTO.getId_account());
                responseObject.put("responseJSON", reponseValues);
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            }
        }
        responseObject.put("reponseMessage", "Login Failed -> Username + Password Combination not found");
        return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Zmena hesla")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Úspešne si si zmenil heslo.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Heslo sa nepodarilo zmeniť, pretože prihlasovacie údaje sú nesprávne.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(anyOf = {Account.class}))
    })
    @PostMapping("/zmenaHesla")
    public ResponseEntity<?> zmenaHesla(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            String email = json.getString("email");
            String password = json.getString("password");
            String newPassword = json.getString("new_password");
            List<AccountDTO> accounts = accountService.getAllAccounts();
            for (AccountDTO accountDTO : accounts) {
                if (accountDTO.getEmail_address().equals(email) && accountDTO.getPassword().equals(password)) {
                    accountDTO.setPassword(newPassword);
                    accountService.saveAccount(accountDTO);
                    responseObject.put("responseMessage", "Úspešne si si zmenil heslo.");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                } else {
                    responseObject.put("responseMessage",
                            "Heslo sa nepodarilo zmeniť, pretože prihlasovacie údaje sú nesprávne.");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("responeMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Vytvoriť novú komunikáciu medzi 2 usermi")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Komunikácia bola vytvorená")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na čítanie organizácií touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class, Communication.class}))
    })
    @PutMapping(value = "/zacatNovuKomunikaciu")
    public ResponseEntity<?> createCommunication(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student") || acc.getRole().equals("povereny_pracovnik")) {
                CommunicationDTO communicationDTO = new CommunicationDTO();
                communicationDTO.setAccount_id_account(acc_id);
                communicationDTO.setAccount_id_account1(requestJsonObject.getInt("account_2"));
                communicationDTO.setMessages(requestJsonObject.getString("messages"));
                communicationService.saveCommunication(communicationDTO);
                responseObject.put("responseMessage", "Komunikácia bola vytvorená");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na čítanie organizácií touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Uložiť komunikáciu medzi 2 usermi")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Vaša správa bola pridaná")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na ukladanie správ touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class, Communication.class}))
    })
    @PostMapping(value = "/pridatDoKomunikacie")
    public ResponseEntity<?> addToCommunication(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student") || acc.getRole().equals("povereny_pracovnik")) {
                CommunicationDTO communicationDTO = communicationService
                        .getCommunicationId(requestJsonObject.getInt("id"));
                communicationDTO.setMessages(requestJsonObject.getString("messages"));
                communicationService.saveCommunication(communicationDTO);
                responseObject.put("responseMessage", "Vaša správa bola pridaná");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na ukladanie správ touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Zobrazenie údajov prihláseného účtu")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Poslané údaje o účte")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "HttpStatus.UNAUTHORIZED")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class}))
    })
    @GetMapping(value = "/zobrazMojeUdaje")
    public ResponseEntity<?> showPersonalData(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc != null) {
                JSONObject accountJSON = new JSONObject(acc);
                responseObject.put("account", accountJSON);
                PersonDTO person = personService.getPersonById(acc.getPerson_id_person());
                JSONObject personJSON = new JSONObject(person);
                responseObject.put("person", personJSON);
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš účet");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
