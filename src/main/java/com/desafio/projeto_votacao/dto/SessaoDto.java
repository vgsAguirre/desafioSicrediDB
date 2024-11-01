package com.desafio.projeto_votacao.dto;

import com.desafio.projeto_votacao.entity.Pauta;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessaoDto {

    private Long id;
    private Integer tempoSessao;
    private Boolean status;
    private PautaDto pauta;

}
