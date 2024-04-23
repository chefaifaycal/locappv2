package com.locapp.service.impl;

import com.locapp.entities.Annonce;
import com.locapp.repository.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepository;

    public List<Annonce> getAllAnnonces() {
        return annonceRepository.findAll();
    }

    public Annonce getAnnonceById(Long id) {
        return annonceRepository.findById(id).orElse(null);
    }

    public Annonce addAnnonce(Annonce annonce) {
        return annonceRepository.save(annonce);
    }

    public Annonce updateAnnonce(Long id, Annonce updatedAnnonce) {
        if (annonceRepository.existsById(id)) {
            updatedAnnonce.setId(id);
            return annonceRepository.save(updatedAnnonce);
        }
        return null;
    }

    public void deleteAnnonce(Long id) {
        annonceRepository.deleteById(id);
    }
}
