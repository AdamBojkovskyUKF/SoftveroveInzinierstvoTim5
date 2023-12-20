package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SoftveroveInzinierstvoTim5.RestAPI.APISchemas.LoginCredentialsSchemaAfterLogin;
import SoftveroveInzinierstvoTim5.RestAPI.dto.*;
import SoftveroveInzinierstvoTim5.RestAPI.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

import org.json.*;


@RestController
public class RestCallControllerAdmin extends GeneralController {

    @Operation(summary = "Zobrazovanie informácií o firmách")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Pole firiem")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup k zobrazeniu firiem.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class}))
    })
    @GetMapping("/zobrazFirmy")
    public ResponseEntity<?> zobrazFirmy(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            List<CompanyDTO> companies = companyService.getAllCompanies();
            JSONArray companiesArray = new JSONArray();

            if (acc.getRole().equals("admin") || acc.getRole().equals("veduci_pracoviska")
                    || acc.getRole().equals("povereny_pracovnik")) {
                for (CompanyDTO companyDTO : companies) {
                    JSONObject companyJson = new JSONObject();
                    PersonDTO representative = personService.getPersonById(companyDTO.getRepresentative_id_person());

                    companyJson.put("Názov: ", companyDTO.getName());
                    companyJson.put(" Adresa: ", companyDTO.getAddress());
                    companyJson.put(" Meno: ", representative.getName());
                    companyJson.put(" Priezvisko: ", representative.getSurname());

                    companiesArray.put(companyJson);
                }

                return new ResponseEntity<>(companiesArray.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš prístup k zobrazeniu firiem.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Priama metóda na vytvorenie inštancie pre každú triedu v DB")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Inštancia požadovanej triedy vytvorená")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "classType", description = "parameter na špecifikáciu triedy ktorú admin chce vytvoriť", required = true)
    @Parameter (name = "entity", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(oneOf = {Account.class,Communication.class,Company.class,Offer.class,Person.class,Report.class,Study_Program.class,Subject_for_Practice.class,Work.class}, allOf = {LoginCredentialsSchemaAfterLogin.class}))
    })
    @PutMapping(value = "/adminCRUD/create/{classType}")
    public ResponseEntity<?> createClass(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("admin")) {
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
                        responseObject.put("responseMessage", Account.class.getName() + " instance created");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "company":
                        CompanyDTO companyDTO = new CompanyDTO();
                        companyDTO.setAddress(requestJsonObject.getString("address"));
                        companyDTO.setName(requestJsonObject.getString("name"));
                        companyDTO.setRepresentative_id_person(requestJsonObject.getInt("representative_id"));
                        companyService.saveCompany(companyDTO);
                        responseObject.put("responseMessage", Company.class.getName() + " instance created");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "offer":
                        OfferDTO offerDTO = new OfferDTO();
                        offerDTO.setCompany_id_company(requestJsonObject.getInt("offer_id"));
                        offerDTO.setContract_type(requestJsonObject.getString("offer_type"));
                        offerDTO.setDescription(requestJsonObject.getString("description"));
                        offerDTO.setOverseer_id_person(requestJsonObject.getInt("overseer_id"));
                        offerDTO.setPosition(requestJsonObject.getString("position"));
                        offerService.saveOffer(offerDTO);
                        responseObject.put("responseMessage", Offer.class.getName() + " instance created");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "person":
                        PersonDTO personDTO = new PersonDTO();
                        personDTO.setAddress(requestJsonObject.getString("address"));
                        personDTO.setEmail(requestJsonObject.getString("email"));
                        personDTO.setName(requestJsonObject.getString("name"));
                        personDTO.setPhone_number(requestJsonObject.getString("phone_number"));
                        personDTO.setSurname(requestJsonObject.getString("surname"));
                        personService.savePerson(personDTO);
                        responseObject.put("responseMessage", Person.class.getName() + " instance created");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "report":
                        ReportDTO reportDTO = new ReportDTO();
                        reportDTO.setContent(requestJsonObject.getString("content"));
                        reportDTO.setCreatoraccount_id_account(requestJsonObject.getInt("creator_id"));
                        reportDTO.setTimestamp(requestJsonObject.getString("timestamp"));
                        reportDTO.setType(requestJsonObject.getString("type"));
                        reportService.saveReport(reportDTO);
                        responseObject.put("responseMessage", Report.class.getName() + " instance created");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "study_program":
                        Study_ProgramDTO study_ProgramDTO = new Study_ProgramDTO();
                        study_ProgramDTO.setName(requestJsonObject.getString("name"));
                        study_ProgramService.saveStudyProgram(study_ProgramDTO);
                        responseObject.put("responseMessage", Study_Program.class.getName() + " instance created");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "subject_for_practice":
                        Subject_For_PracticeDTO subject_For_PracticeDTO = new Subject_For_PracticeDTO();
                        subject_For_PracticeDTO.setCredits(requestJsonObject.getInt("credits"));
                        subject_For_PracticeDTO.setName(requestJsonObject.getString("name"));
                        subject_For_PracticeDTO
                                .setStudy_program_idstudy_program(requestJsonObject.getInt("study_program_id"));
                        subject_For_PracticeService.saveSubjectForPractice(subject_For_PracticeDTO);
                        responseObject.put("responseMessage",
                                Subject_for_Practice.class.getName() + " instance created");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "work":
                        WorkDTO workDTO = new WorkDTO();
                        workDTO.setAccount_id_account(requestJsonObject.getInt("work_account_id"));
                        workDTO.setCompletion_year(requestJsonObject.getString("completion_year"));
                        workDTO.setContract((byte[])requestJsonObject.get("contract"));
                        workDTO.setFeedback_company(requestJsonObject.getString("feedback_company"));
                        workDTO.setFeedback_student(requestJsonObject.getString("feedback_student"));
                        workDTO.setMark(requestJsonObject.getString("mark"));
                        workDTO.setOffer_id_offer(requestJsonObject.getInt("offer_id"));
                        workDTO.setState(requestJsonObject.getString("state"));
                        workDTO.setWork_log(requestJsonObject.getString("work_log"));
                        workService.saveWork(workDTO);
                        responseObject.put("responseMessage", Work.class.getName() + " instance created");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "communication":
                        CommunicationDTO communicationDTO = new CommunicationDTO();
                        communicationDTO.setAccount_id_account(requestJsonObject.getInt("account_1"));
                        communicationDTO.setAccount_id_account1(requestJsonObject.getInt("account_2"));
                        communicationDTO.setMessages(requestJsonObject.getString("messages"));
                        communicationService.saveCommunication(communicationDTO);
                        responseObject.put("responseMessage", Communication.class.getName() + " instance created");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    default:
                        responseObject.put("responseMessage", "Invalid class type");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.BAD_REQUEST);
                }
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vytváranie tried touto metódou");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Priama metóda na čítanie inštancie pre každú triedu v DB")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Inštancia požadovanej triedy poslaná")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "classType", description = "parameter na špecifikáciu triedy ktorú admin chce čítať", required = true)
    @Parameter (name = "entity", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(oneOf = {Account.class,Communication.class,Company.class,Offer.class,Person.class,Report.class,Study_Program.class,Subject_for_Practice.class,Work.class}, allOf = {LoginCredentialsSchemaAfterLogin.class}))
    })
    @GetMapping(value = "/adminCRUD/read/{classType}")
    public ResponseEntity<?> readClass(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("admin")) {
                switch (classType) {
                    case "account":
                        AccountDTO accountDTO = accountService.getAccountId(requestJsonObject.getInt("id"));
                        JSONObject responseAccount = new JSONObject(accountDTO);
                        responseObject.put("object", responseAccount);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "company":
                        CompanyDTO companyDTO = companyService.getCompanyId(requestJsonObject.getInt("id"));
                        JSONObject responseCompany = new JSONObject(companyDTO);
                        responseObject.put("object", responseCompany);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "offer":
                        OfferDTO offerDTO = offerService.getOfferId(requestJsonObject.getInt("id"));
                        JSONObject responseOffer = new JSONObject(offerDTO);
                        responseObject.put("object", responseOffer);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "person":
                        PersonDTO personDTO = personService.getPersonById(requestJsonObject.getInt("id"));
                        JSONObject responsePerson = new JSONObject(personDTO);
                        responseObject.put("object", responsePerson);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "report":
                        ReportDTO reportDTO = reportService.getReportById(requestJsonObject.getInt("id"));
                        JSONObject responseReport = new JSONObject(reportDTO);
                        responseObject.put("object", responseReport);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "study_program":
                        Study_ProgramDTO study_ProgramDTO = study_ProgramService
                                .getStudyProgramById(requestJsonObject.getInt("id"));
                        JSONObject responseStudyProgram = new JSONObject(study_ProgramDTO);
                        responseObject.put("object", responseStudyProgram);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "subject_for_practice":
                        Subject_For_PracticeDTO subject_For_PracticeDTO = subject_For_PracticeService
                                .getSubjectForPracticeById(requestJsonObject.getInt("id"));
                        JSONObject responseSubjectForPractice = new JSONObject(subject_For_PracticeDTO);
                        responseObject.put("object", responseSubjectForPractice);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "work":
                        WorkDTO workDTO = workService.getWorkById(requestJsonObject.getInt("id"));
                        JSONObject responseWork = new JSONObject(workDTO);
                        responseObject.put("object", responseWork);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "communication":
                        CommunicationDTO communicationDTO = communicationService
                                .getCommunicationId(requestJsonObject.getInt("id"));
                        JSONObject responseCommunication = new JSONObject(communicationDTO);
                        responseObject.put("object", responseCommunication);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    default:
                        responseObject.put("responseMessage", "Invalid class type");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.BAD_REQUEST);
                }
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na čítanie tried touto metódou");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Priama metóda na aktualizovanie inštancie pre každú triedu v DB")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Inštancia požadovanej triedy aktualizovaná")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "classType", description = "parameter na špecifikáciu triedy ktorú admin chce aktualizovať", required = true)
    @Parameter (name = "entity", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(oneOf = {Account.class,Communication.class,Company.class,Offer.class,Person.class,Report.class,Study_Program.class,Subject_for_Practice.class,Work.class}, allOf = {LoginCredentialsSchemaAfterLogin.class}))
    })
    @PostMapping(value = "/adminCRUD/update/{classType}")
    public ResponseEntity<?> updateClass(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("admin")) {
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
                        responseObject.put("responseMessage", Account.class.getName() + " instance updated");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "company":
                        CompanyDTO companyDTO = companyService.getCompanyId(requestJsonObject.getInt("id"));
                        companyDTO.setAddress(requestJsonObject.getString("address"));
                        companyDTO.setName(requestJsonObject.getString("name"));
                        companyDTO.setRepresentative_id_person(requestJsonObject.getInt("representative_id"));
                        companyService.saveCompany(companyDTO);
                        responseObject.put("responseMessage", Company.class.getName() + " instance updated");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "offer":
                        OfferDTO offerDTO = offerService.getOfferId(requestJsonObject.getInt("id"));
                        offerDTO.setCompany_id_company(requestJsonObject.getInt("offer_id"));
                        offerDTO.setContract_type(requestJsonObject.getString("offer_type"));
                        offerDTO.setDescription(requestJsonObject.getString("description"));
                        offerDTO.setOverseer_id_person(requestJsonObject.getInt("overseer_id"));
                        offerDTO.setPosition(requestJsonObject.getString("position"));
                        offerService.saveOffer(offerDTO);
                        responseObject.put("responseMessage", Offer.class.getName() + " instance updated");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "person":
                        PersonDTO personDTO = personService.getPersonById(requestJsonObject.getInt("id"));
                        personDTO.setAddress(requestJsonObject.getString("address"));
                        personDTO.setEmail(requestJsonObject.getString("email"));
                        personDTO.setName(requestJsonObject.getString("name"));
                        personDTO.setPhone_number(requestJsonObject.getString("phone_number"));
                        personDTO.setSurname(requestJsonObject.getString("surname"));
                        personService.savePerson(personDTO);
                        responseObject.put("responseMessage", Person.class.getName() + " instance updated");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "report":
                        ReportDTO reportDTO = reportService.getReportById(requestJsonObject.getInt("id"));
                        reportDTO.setContent(requestJsonObject.getString("content"));
                        reportDTO.setCreatoraccount_id_account(requestJsonObject.getInt("creator_id"));
                        reportDTO.setTimestamp(requestJsonObject.getString("timestamp"));
                        reportDTO.setType(requestJsonObject.getString("type"));
                        reportService.saveReport(reportDTO);
                        responseObject.put("responseMessage", Report.class.getName() + " instance updated");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "study_program":
                        Study_ProgramDTO study_ProgramDTO = study_ProgramService
                                .getStudyProgramById(requestJsonObject.getInt("id"));
                        study_ProgramDTO.setName(requestJsonObject.getString("name"));
                        study_ProgramService.saveStudyProgram(study_ProgramDTO);
                        responseObject.put("responseMessage", Study_Program.class.getName() + " instance updated");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "subject_for_practice":
                        Subject_For_PracticeDTO subject_For_PracticeDTO = subject_For_PracticeService
                                .getSubjectForPracticeById(requestJsonObject.getInt("id"));
                        subject_For_PracticeDTO.setCredits(requestJsonObject.getInt("credits"));
                        subject_For_PracticeDTO.setName(requestJsonObject.getString("name"));
                        subject_For_PracticeDTO
                                .setStudy_program_idstudy_program(requestJsonObject.getInt("study_program_id"));
                        subject_For_PracticeService.saveSubjectForPractice(subject_For_PracticeDTO);
                        responseObject.put("responseMessage",
                                Subject_for_Practice.class.getName() + " instance updated");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "work":
                        WorkDTO workDTO = workService.getWorkById(requestJsonObject.getInt("id"));
                        workDTO.setAccount_id_account(requestJsonObject.getInt("work_account_id"));
                        workDTO.setCompletion_year(requestJsonObject.getString("completion_year"));
                        workDTO.setContract((byte[])requestJsonObject.get("contract"));
                        workDTO.setFeedback_company(requestJsonObject.getString("feedback_company"));
                        workDTO.setFeedback_student(requestJsonObject.getString("feedback_student"));
                        workDTO.setMark(requestJsonObject.getString("mark"));
                        workDTO.setOffer_id_offer(requestJsonObject.getInt("offer_id"));
                        workDTO.setState(requestJsonObject.getString("state"));
                        workDTO.setWork_log(requestJsonObject.getString("work_log"));
                        workService.saveWork(workDTO);
                        responseObject.put("responseMessage", Work.class.getName() + " instance updated");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "communication":
                        CommunicationDTO communicationDTO = communicationService
                                .getCommunicationId(requestJsonObject.getInt("id"));
                        communicationDTO.setAccount_id_account(requestJsonObject.getInt("account_1"));
                        communicationDTO.setAccount_id_account1(requestJsonObject.getInt("account_2"));
                        communicationDTO.setMessages(requestJsonObject.getString("messages"));
                        communicationService.saveCommunication(communicationDTO);
                        responseObject.put("responseMessage", Communication.class.getName() + " instance updated");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    default:
                        responseObject.put("responseMessage", "Invalid class type");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.BAD_REQUEST);
                }
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na aktualizovanie tried touto metódou");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Priama metóda na vymazanie inštancie pre každú triedu v DB")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Inštancia požadovanej triedy vymazaná")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "classType", description = "parameter na špecifikáciu triedy ktorú admin chce vymazať", required = true)
    @Parameter (name = "entity", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(oneOf = {Account.class,Communication.class,Company.class,Offer.class,Person.class,Report.class,Study_Program.class,Subject_for_Practice.class,Work.class}, allOf = {LoginCredentialsSchemaAfterLogin.class}))
    })
    @DeleteMapping(value = "/adminCRUD/delete/{classType}")
    public ResponseEntity<?> deleteClass(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("admin")) {
                switch (classType) {
                    case "account":
                        accountService.deleteAccount(requestJsonObject.getInt("id"));
                        responseObject.put("responseMessage", Account.class.getName() + " instance deleted");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "company":
                        companyService.deleteCompany(requestJsonObject.getInt("id"));
                        responseObject.put("responseMessage", Company.class.getName() + " instance deleted");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "offer":
                        offerService.deleteOffer(requestJsonObject.getInt("id"));
                        responseObject.put("responseMessage", Offer.class.getName() + " instance deleted");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "person":
                        personService.deletePerson(requestJsonObject.getInt("id"));
                        responseObject.put("responseMessage", Person.class.getName() + " instance deleted");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "report":
                        reportService.deleteReport(requestJsonObject.getInt("id"));
                        responseObject.put("responseMessage", Report.class.getName() + " instance deleted");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "study_program":
                        study_ProgramService.deleteStudyProgram(requestJsonObject.getInt("id"));
                        responseObject.put("responseMessage", Study_Program.class.getName() + " instance deleted");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "subject_for_practice":
                        subject_For_PracticeService.deleteSubjectForPractice(requestJsonObject.getInt("id"));
                        responseObject.put("responseMessage",
                                Subject_for_Practice.class.getName() + " instance deleted");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "work":
                        workService.deleteWork(requestJsonObject.getInt("id"));
                        responseObject.put("responseMessage", Work.class.getName() + " instance deleted");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "communication":
                        communicationService.deleteCommunication(requestJsonObject.getInt("id"));
                        responseObject.put("responseMessage", Communication.class.getName() + " instance deleted");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    default:
                        responseObject.put("responseMessage", "Invalid class type");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.BAD_REQUEST);
                }
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vymazávanie tried touto metódou");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Metóda na výpis tabulky z DB")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Report vytvorený")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "classType", description = "parameter na špecifikáciu triedy pre ktorú admin chce vytvoriť report", required = true)
    @Parameter (name = "entity", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json", schema = @Schema(allOf = LoginCredentialsSchemaAfterLogin.class))
    })
    @GetMapping(value = "/adminReport/{classType}")
    public ResponseEntity<?> reportGenerator(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("admin")) {
                switch (classType) {
                    case "account":
                        List<AccountDTO> accountDTOs = accountService.getAllAccounts();
                        JSONArray accountJsonArray = new JSONArray(accountDTOs);
                        responseObject.put("objectArray", accountJsonArray);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "company":
                        List<CompanyDTO> companyDTOs = companyService.getAllCompanies();
                        JSONArray companyJsonArray = new JSONArray(companyDTOs);
                        responseObject.put("objectArray", companyJsonArray);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "offer":
                        List<OfferDTO> offerDTOs = offerService.getAllOffers();
                        JSONArray offerJsonArray = new JSONArray(offerDTOs);
                        responseObject.put("objectArray", offerJsonArray);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "person":
                        List<PersonDTO> personDTOs = personService.getAllPersons();
                        JSONArray personJsonArray = new JSONArray(personDTOs);
                        responseObject.put("objectArray", personJsonArray);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "report":
                        List<ReportDTO> reportDTOs = reportService.getAllReports();
                        JSONArray reportJsonArray = new JSONArray(reportDTOs);
                        responseObject.put("objectArray", reportJsonArray);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "study_program":
                        List<Study_ProgramDTO> study_ProgramDTOs = study_ProgramService.getAllStudyPrograms();
                        JSONArray studyProgramJsonArray = new JSONArray(study_ProgramDTOs);
                        responseObject.put("objectArray", studyProgramJsonArray);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "subject_for_practice":
                        List<Subject_For_PracticeDTO> subject_For_PracticeDTOs = subject_For_PracticeService
                                .getAllSubjectsForPractice();
                        JSONArray subjectForPracticeJsonArray = new JSONArray(subject_For_PracticeDTOs);
                        responseObject.put("objectArray", subjectForPracticeJsonArray);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "work":
                        List<WorkDTO> workDTOs = workService.getAllWorks();
                        JSONArray workJsonArray = new JSONArray(workDTOs);
                        responseObject.put("objectArray", workJsonArray);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    case "communication":
                        List<CommunicationDTO> communicationDTOs = communicationService.getAllCommunications();
                        JSONArray communicationJsonArray = new JSONArray(communicationDTOs);
                        responseObject.put("objectArray", communicationJsonArray);
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    default:
                        responseObject.put("responseMessage", "Invalid class type");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.BAD_REQUEST);
                }
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na generovanie reportov touto metódou");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
