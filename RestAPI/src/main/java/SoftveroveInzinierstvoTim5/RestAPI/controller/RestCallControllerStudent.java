package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.*;


@RestController
public class RestCallControllerStudent extends GeneralController {

    @Operation(summary = "Zobrazovanie ponúk praxe pre študenta")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Pole ponúk")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na zobrazovanie ponúk touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class}))
    })
    @GetMapping(value = "/student/zobrazStudentPonuky")
    public ResponseEntity<?> viewOffers(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student")) {
                List<OfferDTO> offerDTOs = offerService.getAllOffers();
                JSONArray offerJsonArray = new JSONArray(offerDTOs);
                responseObject.put("objectArray", offerJsonArray);
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazovanie ponúk touto metódou");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vytvorenie praxe z ponuky pre študenta")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Prax vytvorená")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na vytvorenie praxe z ponuky")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class, Work.class}))
    })
    @GetMapping(value = "/student/vytvoritRobotuzPonuky")
    public ResponseEntity<?> createWorkFromOffer(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student")) {
                WorkDTO workDTO = new WorkDTO();
                workDTO.setAccount_id_account(acc_id);
                workDTO.setOffer_id_offer(requestJsonObject.getInt("offer_id"));
                workDTO.setContract((byte[])requestJsonObject.get("contract"));
                workDTO.setState(requestJsonObject.getString("state"));
                workDTO.setWork_log(requestJsonObject.getString("work_log"));
                if (!requestJsonObject.getString("completion_year").isEmpty()
                        && requestJsonObject.getString("completion_year") != null) {
                    workDTO.setCompletion_year(requestJsonObject.getString("completion_year"));
                }
                workService.saveWork(workDTO);
                responseObject.put("responseMessage", Work.class.getName() + " instance created");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vytvorenie praxe z ponuky");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vybrať program pre študenta")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Program vybratý")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na pridavanie študijných programov touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Účet už má priradený študijný program")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class}, anyOf = {Account.class}))
    })
    @GetMapping(value = "/student/vybratProgram")
    public ResponseEntity<?> chooseStudyProgram(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student")) {
                AccountDTO accountDTO = accountService.getAccountId(acc_id);
                if (accountDTO.getStudy_program_idstudy_program() == null
                        || accountDTO.getStudy_program_idstudy_program() == 0) {
                    accountDTO.setStudy_program_idstudy_program(requestJsonObject.getInt("study_program_id"));
                    accountService.saveAccount(accountDTO);
                    responseObject.put("responseMessage", Account.class.getName() + " instance updated");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                } else {
                    responseObject.put("responseMessage", "Účet už má priradený študijný program");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else {
                responseObject.put("responseMessage",
                        "Nemáš povolenie na pridavanie študijných programov touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vytvoriť ponuku pre študenta")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Ponuka vytvorená")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na pridavanie ponúk touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class, Offer.class}))
    })
    @PostMapping(value = "/student/vytvoritPonuku")
    public ResponseEntity<?> studentVytvorPonuku(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student")) {
                OfferDTO offerDTO = new OfferDTO();
                    offerDTO.setCompany_id_company(requestJsonObject.getInt("company_id_company"));
                    offerDTO.setContract_type(requestJsonObject.getString("contract_type"));
                    offerDTO.setDescription(requestJsonObject.getString("description"));
                    offerDTO.setOverseer_id_person(requestJsonObject.getInt("overseer_id_person"));
                    offerDTO.setPosition(requestJsonObject.getString("position"));
                    offerService.saveOffer(offerDTO);
                    responseObject.put("responseMessage", Offer.class.getName() + " instance created");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage",
                        "Nemáš povolenie na pridavanie ponúk touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vytvoriť osobu pre študenta")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Osoba vytvorená")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na pridavanie osôb touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class, Person.class}))
    })
    @PostMapping(value = "/student/vytvoritOsobu")
    public ResponseEntity<?> studentVytvorOsobu(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student")) {
                PersonDTO personDTO = new PersonDTO();
                personDTO.setAddress(requestJsonObject.getString("address"));
                personDTO.setEmail(requestJsonObject.getString("email"));
                personDTO.setName(requestJsonObject.getString("name"));
                personDTO.setPhone_number(requestJsonObject.getString("phone_number"));
                personDTO.setSurname(requestJsonObject.getString("surname"));
                personService.savePerson(personDTO);
                responseObject.put("responseMessage", Person.class.getName() + " instance created");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage",
                        "Nemáš povolenie na pridavanie osôb touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vytvoriť organizaciu pre študenta")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Organizacia vytvorená")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na pridavanie organizacii touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class, Company.class}))
    })
    @PostMapping(value = "/student/vytvoritOrganizaciu")
    public ResponseEntity<?> studentVytvorOrganizaciu(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student")) {
                CompanyDTO companyDTO = new CompanyDTO();
                companyDTO.setAddress(requestJsonObject.getString("address"));
                companyDTO.setName(requestJsonObject.getString("name"));
                companyDTO.setRepresentative_id_person(requestJsonObject.getInt("representative_id"));
                companyService.saveCompany(companyDTO);
                responseObject.put("responseMessage", Company.class.getName() + " instance created");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage",
                        "Nemáš povolenie na pridavanie organizacii touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Dostať osvedčenie pre študenta")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Osvedčenie poslané", ref = "returns Certificate in base64String")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na prevzanie osvedčenia touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class}, anyOf = {Work.class}))
    })
    @GetMapping(value = "/student/GetCertificate")
    public ResponseEntity<?> studentGetCertificate(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student")) {
                WorkDTO workDTO = workService.getWorkById(requestJsonObject.getInt("id_work"));
                byte[] bytes = workDTO.getCertificate();
                String base64String = Base64.encodeBase64String(bytes);
                responseObject.put("certificateBase64String", base64String);
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage",
                        "Nemáš povolenie na pridavanie organizacii touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Pridať výkaz do praxe pre študenta")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Výkaz pridaný")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na priradenie výkazu do praxe touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class}, anyOf = {Work.class}))
    })
    @GetMapping(value = "/student/AddWorkLog")
    public ResponseEntity<?> studentAddWorkLog(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student")) {
                WorkDTO workDTO = workService.getWorkById(requestJsonObject.getInt("id_work"));
                workDTO.setWork_log(workDTO.getWork_log() + "\n" + requestJsonObject.getString("work_log"));
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage",
                        "Nemáš povolenie na pridavanie organizacii touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(summary = "Výpis praxe pre študenta")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Prax poslaná")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na zobrazenie praxe touto metódou")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @Parameter (name = "requestString", description = "request Body", required = false)
    @io.swagger.v3.oas.annotations.parameters.RequestBody (description = "JSON obsahujúci potrebné údaje pre metódu", required = true, content = {
        @Content(mediaType = "application/json",
        schema = @Schema(allOf = {LoginCredentialsSchemaAfterLogin.class}, anyOf = {Work.class}))
    })
    @GetMapping(value = "/student/vypisPraxe")
    public ResponseEntity<?> studentvypisPraxe(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("student")) {
                Study_ProgramDTO study_ProgramDTO = study_ProgramService.getStudyProgramById(acc.getStudy_program_idstudy_program());
                responseObject.put("studyProgram", new JSONObject(study_ProgramDTO));
                Subject_For_PracticeDTO subject_For_PracticeDTO = subject_For_PracticeService.getSubjectForPracticeById(study_ProgramDTO.getIdstudy_program());
                responseObject.put("SubjectForPractice", new JSONObject(subject_For_PracticeDTO));
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage",
                        "Nemáš povolenie na pridavanie organizacii touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

