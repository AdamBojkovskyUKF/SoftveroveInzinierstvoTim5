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
public class RestCallControllerZP extends GeneralController {

    @Operation(summary = "Zobrazenie zoznamu praxí v danej organizácií")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "List praxí v danej organizácií")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na zobrazenie praxí v danej organizácií.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/zobrazitPraxeVoSvojejOrganizacii")
    public ResponseEntity<?> zobrazitPraxeVoSvojejOrganizacii(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);
            List<WorkDTO> works = workService.getAllWorks();

            JSONArray worksArray = new JSONArray();

            if (acc.getRole().equals("zastupca_firmy")) {
                for (WorkDTO workDTO : works) {
                    JSONObject work = new JSONObject();
                    OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                    CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());
                    AccountDTO student = accountService.getAccountId(workDTO.getAccount_id_account());
                    PersonDTO studentPerson = personService.getPersonById(student.getPerson_id_person());
                    PersonDTO reprePerson = personService.getPersonById(acc.getPerson_id_person());

                    if (reprePerson.getId_person() == company.getRepresentative_id_person()) {
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

                return new ResponseEntity<>(worksArray.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na zobrazenie praxí v danej organizácií.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vytvorenie spätnej väzby ku praxi")
    @ApiResponse(responseCode = "HttpStatus.CONFLICT", description = "Zadaná práca už má vytvorenú spätnú väzbu od firmy.")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Úspešne si vytvoril spätnú väzbu firmy pre zadanú prax.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup k tejto praxi, pretože nie si zástupca firmy, v ktorej sa vykonáva.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na vytváranie spätných väzieb firiem.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/vytvorSpatnuVazbuPraxe")
    public ResponseEntity<?> vytvorSpatnuVazbuPraxe(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            int id_work = json.getInt("work_id");
            String spatnaVazba = json.getString("spatna_vazba");

            AccountDTO acc = accountService.getAccountId(id);

            if (acc.getRole().equals("zastupca_firmy")) {
                WorkDTO work = workService.getWorkById(id_work);
                OfferDTO offer = offerService.getOfferId(work.getOffer_id_offer());
                CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());
                PersonDTO reprePerson = personService.getPersonById(acc.getPerson_id_person());

                if (company.getRepresentative_id_person() == reprePerson.getId_person()) {
                    if (work.getFeedback_company() != null) {
                        responseObject.put("responseMessage", "Zadaná práca už má vytvorenú spätnú väzbu od firmy.");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.CONFLICT);
                    } else {
                        work.setFeedback_company(spatnaVazba);
                        workService.saveWork(work);

                        responseObject.put("responseMessage",
                                "Úspešne si vytvoril spätnú väzbu firmy pre zadanú prax.");

                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    }
                } else {
                    responseObject.put("responseMessage",
                            "Nemáš prístup k tejto praxi, pretože nie si zástupca firmy, v ktorej sa vykonáva.");

                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
                }

            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vytváranie spätných väzieb firiem.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Odsúhlasenie pracovného výkazu")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Pracovný výkaz odsúhlasený.")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Pracovný výkaz zamietnutý.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup k tejto práci, pretože nie si zástupca danej firmy.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenia odsúhlasovať pracovné výkazy.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/odsuhlasitPracovnyVykaz")
    public ResponseEntity<?> odsuhlasitPracovnyVykaz(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            int work = json.getInt("work_id");
            AccountDTO acc = accountService.getAccountId(id);
            WorkDTO workDTO = workService.getWorkById(work);

            if (acc.getRole().equals("zastupca_firmy")) {
                OfferDTO offer = offerService.getOfferId(workDTO.getOffer_id_offer());
                CompanyDTO company = companyService.getCompanyId(offer.getCompany_id_company());
                PersonDTO reprePerson = personService.getPersonById(acc.getPerson_id_person());

                if(reprePerson.getId_person() == company.getRepresentative_id_person()) {
                    if(workDTO.getWork_log().length() > 100) {
                        workDTO.setState("schvalena");
                        workService.saveWork(workDTO);

                        responseObject.put("responseMessage", "Pracovný výkaz odsúhlasený.");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    } else {
                        responseObject.put("responseMessage", "Pracovný výkaz zamietnutý.");
                        return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                    }
                } else {
                    responseObject.put("responseMessage",
                            "Nemáš prístup k tejto práci, pretože nie si zástupca danej firmy.");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
                }

            } else {
                responseObject.put("responseMessage", "Nemáš povolenia odsúhlasovať pracovné výkazy.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Zobrazenie ponúk praxí")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "List ponúk prác")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš prístup k zobrazovaniu ponúk.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @GetMapping("/zobrazPonuky")
    public ResponseEntity<?> zobrazPonuky(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            List <OfferDTO> offers = offerService.getAllOffers();
            JSONArray offersArray = new JSONArray();

            if(acc.getRole().equals("zastupca_firmy") || acc.getRole().equals("veduci_pracoviska")) {
                for (OfferDTO offerDto : offers) {
                    JSONObject offerJson = new JSONObject();
                    CompanyDTO offerCompany = companyService.getCompanyId(offerDto.getCompany_id_company());

                    offerJson.put("Typ kontraktu: ", offerDto.getContract_type());
                    offerJson.put(" Popis: ", offerDto.getDescription());
                    offerJson.put(" Pozícia: ", offerDto.getPosition());
                    offerJson.put(" Firma: ", offerCompany.getName());

                    offersArray.put(offerJson);
                }

                return new ResponseEntity<>(offersArray.toString(), HttpStatus.OK);
            } else {
                responseObject.put("responseMessage", "Nemáš prístup k zobrazovaniu ponúk.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Vytvorenie ponuky na prax pre svoju organizáciu")
    @ApiResponse(responseCode = "HttpStatus.OK", description = "Instance created.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nie si zástupca pre túto firmu.")
    @ApiResponse(responseCode = "HttpStatus.UNAUTHORIZED", description = "Nemáš povolenie na vytváranie pracovných ponúk.")
    @ApiResponse(responseCode = "HttpStatus.INTERNAL_SERVER_ERROR", description = "Internal Server Error")
    @PostMapping("/vytvorPonukuPreSvojuOrganizaciu")
    public ResponseEntity<?> vytvorPonukuPreSvojuOrganizaciu(@RequestBody String requestString) {
        JSONObject responseObject = new JSONObject();

        try {
            JSONObject json = new JSONObject(requestString);
            int id = json.getInt("account_id");
            AccountDTO acc = accountService.getAccountId(id);

            if(acc.getRole().equals("zastupca_firmy")) {
                CompanyDTO company = companyService.getCompanyId(acc.getCompany_id_company());
                String contract_type = json.getString("contract_type");
                String description = json.getString("description");
                String position = json.getString("position");
                String name = json.getString("name");
                if(company.getName().equals(name)) {
                    OfferDTO offerDTO = new OfferDTO();
                    offerDTO.setCompany_id_company(acc.getCompany_id_company());
                    offerDTO.setContract_type(contract_type);
                    offerDTO.setDescription(description);
                    offerDTO.setPosition(position);
                    offerService.saveOffer(offerDTO);
                    responseObject.put("responseMessage", Offer.class.getName() + " instance created");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
                } else {
                    responseObject.put("responseMessage", "Nie si zástupca pre túto firmu.");
                    return new ResponseEntity<>(responseObject.toString(), HttpStatus.METHOD_NOT_ALLOWED);
                }
            } else {
                responseObject.put("responseMessage", "Nemáš povolenie na vytváranie pracovných ponúk.");
                return new ResponseEntity<>(responseObject.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            responseObject.put("responseMessage", e.getMessage());
            return new ResponseEntity<>(responseObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
