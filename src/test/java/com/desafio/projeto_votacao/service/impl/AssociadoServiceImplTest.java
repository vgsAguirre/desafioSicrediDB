package com.desafio.projeto_votacao.service.impl;

import com.desafio.projeto_votacao.dto.AssociadoDto;
import com.desafio.projeto_votacao.dto.AssociadoRequestDto;
import com.desafio.projeto_votacao.entity.Associado;
import com.desafio.projeto_votacao.exceptions.CustomException;
import com.desafio.projeto_votacao.repository.AssociadoRepository;
import com.desafio.projeto_votacao.utils.AssociadoValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssociadoServiceImplTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private AssociadoValidator validationAssociado;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AssociadoServiceImpl associadoService;

    private final static String nome = "João";
    private final static String cpf = "01303935090";
    private final static AssociadoRequestDto request = AssociadoRequestDto
            .builder()
            .nome(nome)
            .cpf(cpf)
            .build();
    @Test
    @DisplayName("Teste do método cadastrarAssociado - CPF válido e inexistente")
    void testCadastrarAssociadoWithValidCPFAndNotExisting() {

        when(validationAssociado.removerMascaraCPF(cpf)).thenReturn(cpf);

        //when(validationAssociado.existeAssociadoComCPF(cpf)).thenReturn(false);

        Associado associadoSalvo = Associado
                .builder()
                .id(1L)
                .nome(nome)
                .cpf(cpf)
                .build();

        AssociadoDto associadoDto = AssociadoDto
                .builder()
                .id(1L)
                .nome(nome)
                .cpf(cpf)
                .build();

        when(associadoRepository.save(any(Associado.class))).thenReturn(associadoSalvo);

        when(modelMapper.map(associadoSalvo, AssociadoDto.class)).thenReturn(associadoDto);

        AssociadoDto result = associadoService.cadastrarAssociado(AssociadoRequestDto
                .builder()
                .nome(nome)
                .cpf(cpf)
                .build());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(nome, result.getNome());
        assertEquals(cpf, result.getCpf());
    }

    @Test
    @DisplayName("Teste do método cadastrarAssociado - CPF já existente")
    void testCadastrarAssociadoWithExistingCPF() {

        //when(validationAssociado.removerMascaraCPF(cpf)).thenReturn(cpf);

        /*CustomException exception = assertThrows(CustomException.class, () -> {
            validationAssociado.existeAssociadoComCPF(cpf);
        });*/
    }

    @Test
    @DisplayName("Teste do método listarAssociados - Lista não vazia")
    void testListarAssociadosWithNonEmptyList() {

        List<Associado> listAssociado = Arrays.asList(Associado.builder().build(),
                Associado.builder().build());

        when(associadoRepository.findAll()).thenReturn(listAssociado);
        AssociadoDto associadoDto = AssociadoDto.builder().build();

        when(modelMapper.map(any(Associado.class), any())).thenReturn(associadoDto);
        List<AssociadoDto> result = associadoService.listarAssociados();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Teste do método listarAssociados - Lista vazia")
    void testListarAssociadosWithEmptyList() {

        when(associadoRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(CustomException.class, () -> associadoService.listarAssociados());
    }

    @Test
    @DisplayName("Teste do método verificarAssociadosCadastrados - Com associados cadastrados")
    void testVerificarAssociadosCadastradosWithAssociados() {

        /*when(associadoRepository.existsAllAssociados()).thenReturn(Boolean.TRUE);
        Assertions.assertThrows(CustomException.class,
                () -> associadoService.verificarAssociadosCadastrados());*/
    }

    @Test
    @DisplayName("Teste do método verificarAssociadosCadastrados - INTERNAL_SERVER_ERROR")
    void testVerificarAssociadosCadastradosInternalServerError() {

        when(validationAssociado.removerMascaraCPF(cpf)).thenReturn(cpf);
        //when(validationAssociado.existeAssociadoComCPF(cpf)).thenReturn(false);

        when(associadoRepository.save(any(Associado.class))).thenThrow(new RuntimeException("Erro interno"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            associadoService.cadastrarAssociado(request);
        });

        String expectedMessage = "Erro interno";
        assertEquals(expectedMessage, exception.getMessage());

    }
}
