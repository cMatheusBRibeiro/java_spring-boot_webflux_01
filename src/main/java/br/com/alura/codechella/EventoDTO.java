package br.com.alura.codechella;

import java.time.LocalDate;

public record EventoDTO(
        Long id,
        TipoEvento tipo,
        String nome,
        LocalDate data,
        String descricao
) {
    public static EventoDTO toDTO(Evento evento) {
        return new EventoDTO(
            evento.getId(),
            evento.getTipo(),
            evento.getNome(),
            evento.getData(),
            evento.getDescricao()
        );
    }

    public Evento toEntity() {
        var evento = new Evento();

        evento.setNome(this.nome);
        evento.setTipo(this.tipo);
        evento.setData(this.data);
        evento.setDescricao(this.descricao);

        return evento;
    }
}
