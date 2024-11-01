package com.desafio.projeto_votacao.service.impl;

import com.desafio.projeto_votacao.dto.AssociadoDto;
import com.desafio.projeto_votacao.dto.AssociadoRequestDto;
import com.desafio.projeto_votacao.entity.Associado;
import com.desafio.projeto_votacao.exceptions.CustomException;
import com.desafio.projeto_votacao.repository.AssociadoRepository;
import com.desafio.projeto_votacao.service.AssociadoService;
import com.desafio.projeto_votacao.utils.AssociadoValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssociadoServiceImpl implements AssociadoService {

    private final AssociadoRepository associadoRepository;
    private final AssociadoValidator validationAssociado;
    private final ModelMapper modelMapper;

    @Override
    public AssociadoDto cadastrarAssociado(AssociadoRequestDto request) {

        validationAssociado.removerMascaraCPF(request.getCpf());

        validationAssociado.existeAssociadoComCPF(request.getCpf());

        Associado associado = Associado.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .build();


        Associado associadoSalvo = associadoRepository.save(associado);

        return modelMapper.map(associadoSalvo, AssociadoDto.class);
    }

    @Override
    public List<AssociadoDto> listarAssociados() {

        List<Associado> listAssociado = Optional.ofNullable(associadoRepository.findAll())
                .orElse(new ArrayList<>());

        if (listAssociado.isEmpty()) throw new CustomException(HttpStatus.NOT_FOUND, "Não há associados cadastrados.");

        return listAssociado.stream()
                .map(associado -> modelMapper
                        .map(associado, AssociadoDto.class))
                .toList();

    }

    @Override
    public void verificarAssociadosCadastrados() {

        if (!associadoRepository.existsAllAssociados()){
            throw new CustomException(HttpStatus.NOT_FOUND, "Não há associados cadastrados.");
        }

    }
}
