package com.desafio.projeto_votacao.service;

import com.desafio.projeto_votacao.dto.AssociadoDto;
import com.desafio.projeto_votacao.dto.AssociadoRequestDto;
import com.desafio.projeto_votacao.exceptions.CustomException;
import java.util.List;

public interface AssociadoService {
    AssociadoDto cadastrarAssociado(AssociadoRequestDto request);

    List<AssociadoDto> listarAssociados() throws CustomException;

    void verificarAssociadosCadastrados();

}
