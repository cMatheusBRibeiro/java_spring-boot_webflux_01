package br.com.alura.codechella;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public Flux<EventoDTO> obterTodos() {
        return this.eventoRepository.findAll().map(EventoDTO::toDTO);
    }

    public Mono<EventoDTO> obterPorId(Long id) {
        return this.eventoRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(EventoDTO::toDTO);
    }

    public Mono<EventoDTO> cadastrarEvento(EventoDTO novoEvento) {
        return this.eventoRepository.save(novoEvento.toEntity()).map(EventoDTO::toDTO);
    }

    public Mono<EventoDTO> atualizarEvento(Long id, EventoDTO novasInformacoes) {
        return this.eventoRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(eventoExistente -> {
                    eventoExistente.setNome(novasInformacoes.nome());
                    eventoExistente.setTipo(novasInformacoes.tipo());
                    eventoExistente.setData(novasInformacoes.data());
                    eventoExistente.setDescricao(novasInformacoes.descricao());

                    return this.eventoRepository.save(eventoExistente);
                })
                .map(EventoDTO::toDTO);
    }

    public Mono<Void> removerEvento(Long id) {
        return this.eventoRepository.findById(id).flatMap(this.eventoRepository::delete);
    }
}
