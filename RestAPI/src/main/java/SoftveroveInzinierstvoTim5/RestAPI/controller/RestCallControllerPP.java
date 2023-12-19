package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SoftveroveInzinierstvoTim5.RestAPI.dto.*;
import SoftveroveInzinierstvoTim5.RestAPI.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.*;


@RestController
public class RestCallControllerPP extends GeneralController {

    @GetMapping(value = "poverenyPracovnik/zobrazitStudentov")
    public ResponseEntity<?> viewStudents(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                List<AccountDTO> accountDTOs = accountService.getAllAccounts();
                List<PersonDTO> personDTOs = personService.getAllPersons();
                for (AccountDTO accountDTO : accountDTOs) {
                    if (!accountDTO.getRole().equals("student")) {
                        for (int a = 0; a < personDTOs.size(); ++a) {
                            if (accountDTO.getPerson_id_person() == personDTOs.get(a).getId_person()) {
                                personDTOs.remove(a);
                            }
                        }
                    }
                }
                JSONArray personJsonArray = new JSONArray(personDTOs);
                responseObject.put("objectArray", personJsonArray);
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazovanie ponúk touto metódou");
                return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "poverenyPracovnik/zobrazitStudentovKatedry")
    public ResponseEntity<?> viewStudentsInInstitute(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                List<AccountDTO> accountDTOs = accountService.getAllAccounts();
                List<PersonDTO> personDTOs = personService.getAllPersons();
                for (AccountDTO accountDTO : accountDTOs) {
                    if (!accountDTO.getRole().equals("student")
                            || !accountDTO.getInstitute().equals(acc.getInstitute())) {
                        for (int a = 0; a < personDTOs.size(); ++a) {
                            if (accountDTO.getPerson_id_person() == personDTOs.get(a).getId_person()) {
                                personDTOs.remove(a);
                            }
                        }
                    }
                }
                JSONArray personJsonArray = new JSONArray(personDTOs);
                responseObject.put("objectArray", personJsonArray);
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazovanie ponúk touto metódou");
                return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "poverenyPracovnik/zobrazitPrace")
    public ResponseEntity<?> viewWorks(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                List<AccountDTO> accountDTOs = accountService.getAllAccounts();
                List<WorkDTO> workDTOs = workService.getAllWorks();
                for (AccountDTO accountDTO : accountDTOs) {
                    if (!accountDTO.getRole().equals("student")) {
                        for (int a = 0; a < workDTOs.size(); ++a) {
                            if (accountDTO.getPerson_id_person() == workDTOs.get(a).getId_work()) {
                                workDTOs.remove(a);
                            }
                        }
                    }
                }
                JSONArray workJsonArray = new JSONArray(workDTOs);
                responseObject.put("objectArray", workJsonArray);
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazovanie ponúk touto metódou");
                return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/poverenyPracovnik/zobrazitPraceKatedry")
    public ResponseEntity<?> viewWorksInInstitute(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                List<AccountDTO> accountDTOs = accountService.getAllAccounts();
                List<WorkDTO> workDTOs = workService.getAllWorks();
                for (AccountDTO accountDTO : accountDTOs) {
                    if (!accountDTO.getRole().equals("student")
                            || !accountDTO.getInstitute().equals(acc.getInstitute())) {
                        for (int a = 0; a < workDTOs.size(); ++a) {
                            if (accountDTO.getPerson_id_person() == workDTOs.get(a).getId_work()) {
                                workDTOs.remove(a);
                            }
                        }
                    }
                }
                JSONArray workJsonArray = new JSONArray(workDTOs);
                responseObject.put("objectArray", workJsonArray);
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazovanie ponúk touto metódou");

                return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/poverenyPracovnik/zmenitZnamkuPrace")
    public ResponseEntity<?> changeWorkMark(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(requestString);
            int id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                int work = requestJsonObject.getInt("work_id");
                WorkDTO workDTO = workService.getWorkById(work);
                workDTO.setMark(requestJsonObject.getString("mark"));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime now = LocalDateTime.now();
                workDTO.setCompletion_year(dtf.format(now));
                workService.saveWork(workDTO);
                responseObject.put("responseMessage", "Práca odsúhlasená.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenia odsúhlasovať pracovné výkazy.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/poverenyPracovnik/vytvoritOrganizaciu")
    public ResponseEntity<?> createCompany(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                CompanyDTO companyDTO = new CompanyDTO();
                companyDTO.setAddress(requestJsonObject.getString("address"));
                companyDTO.setName(requestJsonObject.getString("name"));
                companyDTO.setRepresentative_id_person(requestJsonObject.getInt("representative_id"));
                companyService.saveCompany(companyDTO);
                responseObject.put("responseMessage", Company.class.getName() + " instance created");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vytváranie organizácií touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/poverenyPracovnik/zobrazitOrganizacie")
    public ResponseEntity<?> readCompany(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                List<CompanyDTO> companyDTOs = companyService.getAllCompanies();
                JSONArray companyJsonArray = new JSONArray(companyDTOs);
                responseObject.put("objectArray", companyJsonArray);
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

    @PostMapping(value = "/poverenyPracovnik/upravitOrganizaciu")
    public ResponseEntity<?> updateCompany(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                CompanyDTO companyDTO = companyService.getCompanyId(requestJsonObject.getInt("id"));
                companyDTO.setAddress(requestJsonObject.getString("address"));
                companyDTO.setName(requestJsonObject.getString("name"));
                companyDTO.setRepresentative_id_person(requestJsonObject.getInt("representative_id"));
                companyService.saveCompany(companyDTO);
                responseObject.put("responseMessage", Company.class.getName() + " instance updated");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na aktualizovanie organizácií touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/poverenyPracovnik/zmazatOrganizaciu")
    public ResponseEntity<?> deleteCompany(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                companyService.deleteCompany(requestJsonObject.getInt("id"));
                responseObject.put("responseMessage", Company.class.getName() + " instance deleted");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vymazávanie organizácií touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/poverenyPracovnik/pridatStudenta")
    public ResponseEntity<?> addStudent(@RequestBody String entity, @PathVariable String classType) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                PersonDTO personDTO = new PersonDTO();
                personDTO.setAddress(requestJsonObject.getString("address"));
                personDTO.setEmail(requestJsonObject.getString("email"));
                personDTO.setName(requestJsonObject.getString("name"));
                personDTO.setPhone_number(requestJsonObject.getString("phone_number"));
                personDTO.setSurname(requestJsonObject.getString("surname"));
                personService.savePerson(personDTO);
                responseObject.put("responseMessage", Person.class.getName() + " instance created");
                List<PersonDTO> personDTOs = personService.getAllPersons();
                int newlyCreatedPersonId = -1;
                for (int a = 0; a < personDTOs.size(); ++a) {
                    if (personDTO.getAddress().equals(personDTOs.get(a).getAddress()) &&
                            personDTO.getEmail().equals(personDTOs.get(a).getEmail()) &&
                            personDTO.getName().equals(personDTOs.get(a).getName()) &&
                            personDTO.getPhone_number().equals(personDTOs.get(a).getPhone_number()) &&
                            personDTO.getSurname().equals(personDTOs.get(a).getSurname())) {
                        newlyCreatedPersonId = personDTOs.get(a).getId_person();
                    }
                }
                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setCompany_id_company(requestJsonObject.getInt("company_id"));
                accountDTO.setEmail_address(requestJsonObject.getString("email"));
                accountDTO.setInstitute(requestJsonObject.getString("institute"));
                accountDTO.setPassword(requestJsonObject.getString("password"));
                accountDTO.setPerson_id_person(newlyCreatedPersonId);
                accountDTO.setRole(requestJsonObject.getString("role"));
                accountDTO.setSignup_year(requestJsonObject.getString("signup_year"));
                accountDTO.setStudy_level(requestJsonObject.getString("study_level"));
                accountDTO.setStudy_program_idstudy_program(requestJsonObject.getInt("studyProgram_id"));
                accountService.saveAccount(accountDTO);
                responseObject.put("responseMessage", Account.class.getName() + " instance created");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vytváranie študentov touto metódou");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/poverenyPracovnik/zobrazitSpravy")
    public ResponseEntity<?> readCommunications(@RequestBody String entity) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJsonObject = new JSONObject(entity);
            int acc_id = requestJsonObject.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(acc_id);
            if (acc.getRole().equals("povereny_pracovnik")) {
                List<CommunicationDTO> communicationDTOs = communicationService.getAllCommunications();
                for (int a = 0; a < communicationDTOs.size(); ++a) {
                    if (communicationDTOs.get(a).getAccount_id_account1() != acc_id) {
                        communicationDTOs.remove(a);
                        --a;
                    }
                }
                JSONArray communicationJsonArray = new JSONArray(communicationDTOs);
                responseObject.put("objectArray", communicationJsonArray);
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
