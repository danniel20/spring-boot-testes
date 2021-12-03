package com.securityteste.securityspringteste.service.papeis;

import java.util.Optional;

import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.repository.PapelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PapelServiceImpl implements PapelService{

    @Autowired
    private PapelRepository papelRepository;

    @Override
    public Optional<Papel> bucarPorNome(String nome) {
        return this.papelRepository.findByNome(nome);
    }
    
}
