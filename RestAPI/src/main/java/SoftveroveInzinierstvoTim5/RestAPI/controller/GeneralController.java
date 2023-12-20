package SoftveroveInzinierstvoTim5.RestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;

import SoftveroveInzinierstvoTim5.RestAPI.service.*;

public class GeneralController {
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

    String[] roly = { "admin", "veduci_pracoviska", "student", "povereny_pracovnik", "zastupca_firmy" };

    String[] institute = { "katedra_informatiky", "katedra_matematiky", "katedra_fyziky" };

    String[] contractTypes = { "brigada", "staz", "dohoda", "zivnost"};

    String[] positions = {"programator", "tester"};

    String[] workStates = {"zacata","nezacata"};

    String[] programNames = {"AIb","AIm"};

    String[] sfps = {"OdbornaPrax1","OdbornaPrax2","OdbornaPrax3","OdbornaPrax4"};

    String[] reportTypes = {"reportZP","reportVP","reportPP"};
}
