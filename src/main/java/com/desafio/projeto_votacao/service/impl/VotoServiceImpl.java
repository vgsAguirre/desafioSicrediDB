package com.desafio.projeto_votacao.service.impl;

import com.desafio.projeto_votacao.dto.VotoDto;
import com.desafio.projeto_votacao.dto.VotoRequestDto;
import com.desafio.projeto_votacao.entity.Associado;
import com.desafio.projeto_votacao.entity.Pauta;
import com.desafio.projeto_votacao.entity.Sessao;
import com.desafio.projeto_votacao.entity.Voto;
import com.desafio.projeto_votacao.enums.VotoEnum;
import com.desafio.projeto_votacao.exceptions.CustomException;
import com.desafio.projeto_votacao.repository.AssociadoRepository;
import com.desafio.projeto_votacao.repository.SessaoRepository;
import com.desafio.projeto_votacao.repository.VotoRepository;
import com.desafio.projeto_votacao.service.VotoService;
import com.desafio.projeto_votacao.utils.AssociadoValidator;
import com.desafio.projeto_votacao.utils.VotoValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final SessaoRepository sessaoRepository;
    private final AssociadoRepository associadoRepository;
    private final VotoRepository votoRepository;
    private final ModelMapper modelMapper;
    private final VotoValidator votoValidator;
    private final AssociadoValidator associadoValidator;
    @Override
    public void registrarVoto(VotoEnum votoEnum, VotoRequestDto request) {

        String cpfAssociado = request.getCpfAssociado();

        cpfAssociado = associadoValidator.removerMascaraCPF(cpfAssociado);

        votoValidator.associadoJaVotou(cpfAssociado);

        Sessao sessao = Optional.ofNullable(sessaoRepository.findByStatus(Boolean.TRUE))
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Não existe uma sessão aberta."));

        Associado associado = Optional.ofNullable(associadoRepository.findByCpf(cpfAssociado))
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Associado não cadastrado para efetuar a votação."));

        Voto votoBuilder = Voto.builder()
                .votoEnum(votoEnum)
                .associado(associado)
                .pauta(sessao.getPauta())
                .build();

        votoRepository.save(votoBuilder);

    }

    @Override
    public List<VotoDto> obterResultadoVotacao(Pauta pauta) {

        List<Voto> listVoto = Optional.ofNullable(votoRepository.findByPauta(pauta))
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum voto."));

        return listVoto.stream()
                .map(voto -> modelMapper
                        .map(voto, VotoDto.class))
                .toList();
    }

}
