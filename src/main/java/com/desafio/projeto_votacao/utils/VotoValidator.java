package com.desafio.projeto_votacao.utils;

import com.desafio.projeto_votacao.exceptions.CustomException;
import com.desafio.projeto_votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VotoValidator {

    private final VotoRepository votoRepository;

    public void associadoJaVotou(String cpf) {
        if (votoRepository.existsPautaByAssociadoCpf(cpf)) {
            throw new CustomException(HttpStatus.CONFLICT, "Associado já votou nessa pauta.");
        }
    }
}
