package com.securityteste.securityspringteste.service.papeis;

import java.util.Optional;

import com.securityteste.securityspringteste.model.Papel;

public interface PapelService {
    
    public Optional<Papel> bucarPorNome(String nome);
}
