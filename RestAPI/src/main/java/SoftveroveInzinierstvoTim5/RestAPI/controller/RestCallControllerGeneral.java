package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;

import SoftveroveInzinierstvoTim5.RestAPI.APISchemas.LoginCredentialsSchemaAfterLogin;
import SoftveroveInzinierstvoTim5.RestAPI.APISchemas.LoginCredentialsSchemaBeforeLogin;
import SoftveroveInzinierstvoTim5.RestAPI.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
    @GetMapping(value = "/zacatNovuKomunikaciu")
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

    @GetMapping(value = "/pridatDoKomunikacie")
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
                responseObject.put("responseMessage", "Nemáš povolenie na čítanie organizácií touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
