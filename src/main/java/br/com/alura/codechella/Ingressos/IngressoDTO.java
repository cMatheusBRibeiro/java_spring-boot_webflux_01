package br.com.alura.codechella.Ingressos;

public record IngressoDTO(
        Long id,
        Long eventoId,
        TipoIngresso tipo,
        Double valor,
        int total
) {

    public static IngressoDTO toDTO(Ingresso ingresso) {
        return new IngressoDTO(
                ingresso.getId(),
                ingresso.getEventoId(),
                ingresso.getTipo(),
                ingresso.getValor(),
                ingresso.getTotal()
        );
    }

    public Ingresso toEntity() {
        var ingresso = new Ingresso();

        ingresso.setId(this.id);
        ingresso.setEventoId(this.eventoId);
        ingresso.setTipo(this.tipo);
        ingresso.setValor(this.valor);
        ingresso.setTotal(this.total);

        return ingresso;
    }

}
