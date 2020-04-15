package com.voiture.etatvoiture.processor;

import com.voiture.etatvoiture.Voiture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class VoitureItemProcessor implements ItemProcessor<Voiture, Voiture> {

    private static final Logger log = LoggerFactory.getLogger(VoitureItemProcessor.class);

    @Override
    public Voiture process(final Voiture voiture) throws Exception {
        final String marque = voiture.getMarque().toUpperCase();
        final String modele = voiture.getModele().toUpperCase();

        final Voiture transformedVoiture = new Voiture(marque, modele,voiture.getEtat());

        log.info("Converting (" + voiture + ") into (" + transformedVoiture + ")");

        return transformedVoiture;
    }

}