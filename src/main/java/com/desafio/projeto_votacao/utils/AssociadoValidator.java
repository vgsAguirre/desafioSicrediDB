package com.desafio.projeto_votacao.utils;

import com.desafio.projeto_votacao.exceptions.CustomException;
import com.desafio.projeto_votacao.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssociadoValidator {
    
    private final AssociadoRepository associadoRepository;

    public String removerMascaraCPF(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    public void existeAssociadoComCPF(String cpf) {
        if (associadoRepository.existsByCpf(cpf)){
            throw new CustomException(HttpStatus.CONFLICT, "Já existe um associado com esse cpf.");
        }
    }

}
