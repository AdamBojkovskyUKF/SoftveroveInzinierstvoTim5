package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.List;

import SoftveroveInzinierstvoTim5.RestAPI.dto.OfferDTO;

public interface OfferService {
    OfferDTO saveOffer(OfferDTO offer);
    boolean deleteOffer(final Integer id_offer);
    List<OfferDTO> getAllOffers();
    OfferDTO getOfferId(final Integer id_offer);
}
