package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import SoftveroveInzinierstvoTim5.RestAPI.dto.OfferDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Offer;
import SoftveroveInzinierstvoTim5.RestAPI.repository.OfferRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Configurable
public class DefaultOfferService implements OfferService{

    @Autowired
    OfferRepository offerRepository;

    @Override
    public OfferDTO saveOffer(OfferDTO offer) {
        Offer offerModel = populateOfferEntity(offer);
        return populateOfferData(offerRepository.save(offerModel));
    }

    @Override
    public boolean deleteOffer(Integer id_offer) {
        offerRepository.deleteById(id_offer);
        return true;
    }

    @Override
    public List<OfferDTO> getAllOffers() {
        List<OfferDTO> offers = new ArrayList<>();
        Iterable <Offer> offerList = offerRepository.findAll();
        offerList.forEach(offer -> {
            offers.add(populateOfferData(offer));
        });
        return offers;
    }

    @Override
    public OfferDTO getOfferId(Integer id_offer) {
        return populateOfferData(offerRepository.findById(id_offer).orElseThrow(() -> new EntityNotFoundException("Offer not found")));
    }

    private OfferDTO populateOfferData(final Offer offer) {
        OfferDTO offerData = new OfferDTO();
        offerData.setOverseer_id_person(offer.getOverseer_id_person());
        offerData.setCompany_id_company(offer.getCompany_id_company());
        offerData.setPosition(offer.getPosition());
        offerData.setDescription(offer.getDescription());
        offerData.setContract_type(offer.getContract_type());
        return offerData;
    }

    private Offer populateOfferEntity(OfferDTO offerData) {
        Offer offer = new Offer();
        offer.setOverseer_id_person(offerData.getOverseer_id_person());
        offer.setCompany_id_company(offerData.getCompany_id_company());
        offer.setPosition(offerData.getPosition());
        offer.setDescription(offerData.getDescription());
        offer.setContract_type(offerData.getDescription());
        return offer;
    }
    
}
