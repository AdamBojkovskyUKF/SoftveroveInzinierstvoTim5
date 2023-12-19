package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SoftveroveInzinierstvoTim5.RestAPI.dto.*;
import SoftveroveInzinierstvoTim5.RestAPI.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

import org.json.*;


@RestController
public class RestCallControllerVP extends GeneralController {

    @Operation(summary = "Zobrazenie zoznamu študentov na praxi")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "List študentov na praxi")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup k zobrazeniu študentov na praxi.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/zobrazStudentovNaPraxi")
    public ResponseEntity<?> zobrazStudentovNaPraxi(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            List<WorkDTO> works = workService.getAllWorks();
            JSONArray studentWorkArray = new JSONArray();

            if (acc.getRole().equals("veduci_pracoviska")) {
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

                return new ResponseEntity<>(studentWorkArray.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš prístup k zobrazeniu študentov na praxi.");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Zobrazenie schválených a ukončených praxí")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "List študentov na praxi so stavom schválená alebo ukončená")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup k zobrazeniu schválených a ukončených praxí študentov.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/zobrazSchvaleneUkoncenePraxe")
    public ResponseEntity<?> zobrazSchvaleneUkoncenePraxe(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            List<WorkDTO> works = workService.getAllWorks();
            JSONArray studentWorkArray = new JSONArray();

            if (acc.getRole().equals("veduci_pracoviska")) {
                for (WorkDTO workDTO : works) {
                    JSONObject studentWork = new JSONObject();
                    AccountDTO studentAcc = accountService.getAccountId(workDTO.getAccount_id_account());
                    PersonDTO studentPerson = personService.getPersonById(studentAcc.getPerson_id_person());
                    OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                    CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());

                    if (workDTO.getState().equals("schvalena") || workDTO.getState().equals("ukoncena")) {
                        studentWork.put("Meno: ", studentPerson.getName());
                        studentWork.put(" Priezvisko: ", studentPerson.getSurname());
                        studentWork.put(" Firma: ", company.getName());
                        studentWork.put(" Stav: ", workDTO.getState());

                        studentWorkArray.put(studentWork);
                    }
                }

                return new ResponseEntity<>(studentWorkArray.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage",
                        "Nemáš prístup k zobrazeniu schválených a ukončených praxí študentov.");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Zobrazenie študentov na praxi podľa zadanej katedry")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "List študentov na praxi na danej katedre")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup k zobrazeniu študentov na praxi podľa zadanej katedry.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/zobrazStudentovNaPraxiPodlaKatedry")
    public ResponseEntity<?> zobrazStudentovNaPraxiPodlaKatedry(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            String institut = json.getString("institute");
            AccountDTO acc = accountService.getAccountId(id);

            List<WorkDTO> works = workService.getAllWorks();
            JSONArray studentWorkArray = new JSONArray();

            if (acc.getRole().equals("veduci_pracoviska")) {
                for (WorkDTO workDTO : works) {
                    JSONObject studentWork = new JSONObject();
                    AccountDTO studentAcc = accountService.getAccountId(workDTO.getAccount_id_account());
                    PersonDTO studentPerson = personService.getPersonById(studentAcc.getPerson_id_person());
                    OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                    CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());

                    if (studentAcc.getInstitute().equals(institut)) {
                        studentWork.put("Meno: ", studentPerson.getName());
                        studentWork.put(" Priezvisko: ", studentPerson.getSurname());
                        studentWork.put(" Firma: ", company.getName());
                        studentWork.put(" Stav: ", workDTO.getState());
                        studentWork.put(" Katedra: ", studentAcc.getInstitute());

                        studentWorkArray.put(studentWork);
                    }
                }

                return new ResponseEntity<>(studentWorkArray.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage",
                        "Nemáš prístup k zobrazeniu študentov na praxi podľa zadanej katedry.");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vytvorenie reportu za pracovisko")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Report úspešne vytvorený.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie pre vytváranie reportu za pracovisko.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/vytvorReportZaPracovisko")
    public ResponseEntity<?> vytvorReportZaPracovisko(@RequestBody String requestString) {
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

                responseObject.put("responseMessage", "Report úspešne vytvorený.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie pre vytváranie reportu za pracovisko.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Zobrazenie spätnej väzby zástupcov firiem")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "List spätných väzieb od zástupcov firiem")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na zobrazenie spätných väzieb zástupcu firmy.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/zobrazSpatneVazbyZastupcuFirmy")
    public ResponseEntity<?> zobrazSpatneVazbyZastupcuFirmy(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            String menoFirmy = json.getString("meno_firmy");
            AccountDTO acc = accountService.getAccountId(id);
            List<WorkDTO> works = workService.getAllWorks();

            JSONArray feedbackCompanyArray = new JSONArray();

            if (acc.getRole().equals("veduci_pracoviska")) {
                for (WorkDTO workDTO : works) {
                    JSONObject feedbackCompany = new JSONObject();
                    OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                    CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());
                    PersonDTO person = personService.getPersonById(company.getRepresentative_id_person());
                    AccountDTO account = accountService.getAccountId(workDTO.getAccount_id_account());
                    PersonDTO studentPerson = personService.getPersonById(account.getPerson_id_person());

                    if (company.getName().equals(menoFirmy)) {
                        feedbackCompany.put(" Zástupca Meno: ", person.getName());
                        feedbackCompany.put(" Zástupca Priezvisko: ", person.getSurname());
                        feedbackCompany.put(" Študent Meno: ", studentPerson.getName());
                        feedbackCompany.put(" Študent priezvisko: ", studentPerson.getSurname());
                        feedbackCompany.put(" Spätná väzba: ", workDTO.getFeedback_company());

                        feedbackCompanyArray.put(feedbackCompany);
                    }
                }

                return new ResponseEntity<>(feedbackCompanyArray.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazenie spätných väzieb zástupcu firmy.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Priradenie povereného pracovníka pracoviska")
    @ApiResponse(responseCode = "HttpStatus.CONFLICT", description = "Zadaná ponuka už má prideleného povereného pracovníka katedry.")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Úspešne si priradil povereného pracovníka katedry danej pracovnej ponuke.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie priraďovať poverených pracovníkov katedier k ponukám práce.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/priradPoverenehoPracovnika")
    public ResponseEntity<?> priradPoverenehoPracovnika(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            int id_offer = json.getInt("offer_id");
            int overseer_id_person = json.getInt("person_id");

            AccountDTO acc = accountService.getAccountId(id);

            if (acc.getRole().equals("veduci_pracoviska")) {
                OfferDTO offer = offerService.getOfferId(id_offer);
                if (offer.getOverseer_id_person() != null) {
                    responseObject.put("responseMessage",
                            "Zadaná ponuka už má prideleného povereného pracovníka katedry.");

                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.CONFLICT);
                } else {
                    offer.setOverseer_id_person(overseer_id_person);
                    offerService.saveOffer(offer);

                    responseObject.put("responseMessage",
                            "Úspešne si priradil povereného pracovníka katedry danej pracovnej ponuke.");

                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                }
            } else {
                responseObject.put("responseMessage",
                        "Nemáš povolenie priraďovať poverených pracovníkov katedier k ponukám práce.");

                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vytvorenie študíjneho programu")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Instance created.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na vytváranie študíjnych programov.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/studijnyProgramCreate")
    public ResponseEntity<?> studijnyProgramCreate(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                Study_ProgramDTO study_ProgramDTO = new Study_ProgramDTO();
                study_ProgramDTO.setName(json.getString("name"));
                study_ProgramService.saveStudyProgram(study_ProgramDTO);
                responseObject.put("responseMessage", Study_Program.class.getName() + " instance created");
                return new ResponseEntity<>(responseObject.toString(),HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vytváranie študíjnych programov.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Prečítanie a zobrazenie študíjneho programu")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Zobrazenie študíjneho programu.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na zobrazovanie študíjnych programov.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/studijnyProgramRead")
    public ResponseEntity<?> studijnyProgramRead(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                Study_ProgramDTO study_ProgramDTO = study_ProgramService.getStudyProgramById(json.getInt("id"));
                JSONObject responseStudyProgram = new JSONObject(study_ProgramDTO);
                responseObject.put("object", responseStudyProgram);
                return new ResponseEntity<>(responseObject.toString(),HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazovanie študíjnych programov.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Aktualizovanie študíjneho programu")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Instance updated.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na aktualizovanie študíjnych programov.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/studijnyProgramUpdate")
    public ResponseEntity<?> studijnyProgramUpdate(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                Study_ProgramDTO study_ProgramDTO = study_ProgramService.getStudyProgramById(json.getInt("id"));
                study_ProgramDTO.setName(json.getString("name"));
                study_ProgramService.saveStudyProgram(study_ProgramDTO);
                responseObject.put("responseMessage", Study_Program.class.getName() + " instance updated");
                return new ResponseEntity<>(responseObject.toString(),HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na aktualizovanie študíjnych programov.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vymazanie študíjneho programu")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Instance deleted.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na odstraňovanie študíjnych programov.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/studijnyProgramDelete")
    public ResponseEntity<?> studijnyProgramDelete(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                study_ProgramService.deleteStudyProgram(json.getInt("id"));
                responseObject.put("responseMessage", Study_Program.class.getName() + " instance deleted");
                return new ResponseEntity<>(responseObject.toString(),HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na odstraňovanie študíjnych programov.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Pridanie predmetu na prax")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Instance created.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na vytváranie predmetov k odbornej praxi.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/predmetyPraxCreate")
    public ResponseEntity<?> predmetyPraxCreate(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                Subject_For_PracticeDTO subject_For_PracticeDTO = new Subject_For_PracticeDTO();
                subject_For_PracticeDTO.setCredits(json.getInt("credits"));
                subject_For_PracticeDTO.setName(json.getString("name"));
                subject_For_PracticeDTO.setStudy_program_idstudy_program(json.getInt("study_program_id"));
                subject_For_PracticeService.saveSubjectForPractice(subject_For_PracticeDTO);
                responseObject.put("responseMessage", Subject_for_Practice.class.getName() + " instance created");
                return new ResponseEntity<>(responseObject.toString(),HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vytváranie predmetov k odbornej praxi.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Prečítanie a zobrazenie predmetu na prax")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Zobrazenie predmetu na prax.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na zobrazovanie predmetov k odbornej praxi.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/predmetyPraxRead")
    public ResponseEntity<?> predmetyPraxRead(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                Subject_For_PracticeDTO subject_For_PracticeDTO = subject_For_PracticeService.getSubjectForPracticeById(json.getInt("id"));
                Study_ProgramDTO study_ProgramDTO = study_ProgramService.getStudyProgramById(subject_For_PracticeDTO.getStudy_program_idstudy_program());
                responseObject.put(" Meno: ", subject_For_PracticeDTO.getName());
                responseObject.put(" Kredity: ", subject_For_PracticeDTO.getCredits());
                responseObject.put(" Študíjny program: ", study_ProgramDTO.getName());
                return new ResponseEntity<>(responseObject.toString(),HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazovanie predmetov k odbornej praxi.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Aktualizovanie predmetu na prax")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Instance updated.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na aktualizovanie predmetov k odbornej praxi.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/predmetyPraxUpdate")
    public ResponseEntity<?> predmetyPraxUpdate(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                Subject_For_PracticeDTO subject_For_PracticeDTO = subject_For_PracticeService.getSubjectForPracticeById(json.getInt("id"));
                subject_For_PracticeDTO.setCredits(json.getInt("credits"));
                subject_For_PracticeDTO.setName(json.getString("name"));
                subject_For_PracticeDTO.setStudy_program_idstudy_program(json.getInt("study_program_id"));
                subject_For_PracticeService.saveSubjectForPractice(subject_For_PracticeDTO);
                responseObject.put("responseMessage", Subject_for_Practice.class.getName() + " instance updated");
                return new ResponseEntity<>(responseObject.toString(),HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na aktualizovanie predmetov k odbornej praxi.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vymazanie predmetu na prax")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Instance deleted.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na vymazávanie predmetov k odbornej praxi.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/predmetyPraxDelete")
    public ResponseEntity<?> predmetyPraxDelete(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                subject_For_PracticeService.deleteSubjectForPractice(json.getInt("id"));
                responseObject.put("responseMessage", Subject_for_Practice.class.getName() + " instance deleted");
                return new ResponseEntity<>(responseObject.toString(),HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vymazávanie predmetov k odbornej praxi.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Zmeniť študíjny level študentovi")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Instance updated.")
    @ApiResponse(responseCode = "HttpStatus.CONFLICT", description = "Zadaný account nie je študent.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup ku zmene študíjneho levelu študentov.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/zmenitStudijnyLevelStudentovi")
    public ResponseEntity<?> zmenitStudijnyLevelStudentovi(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("veduci_pracoviska")) {
                int student_id = json.getInt("student_id");
                AccountDTO student_acc = accountService.getAccountId(student_id);
                if(student_acc.getRole().equals("student")) {
                    String study_level = json.getString("study_level");
                    student_acc.setStudy_level(study_level);
                    accountService.saveAccount(student_acc);

                    responseObject.put("responseMessage", Account.class.getName() + " instance updated");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                } else {
                    responseObject.put("responseMessage", "Zadaný account nie je študent.");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.CONFLICT);
                }
            } else {
                responseObject.put("responseMessage", "Nemáš prístup ku zmene študíjneho levelu študentov.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
