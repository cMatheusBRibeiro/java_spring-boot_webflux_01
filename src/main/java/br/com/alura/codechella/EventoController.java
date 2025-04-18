package br.com.alura.codechella;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/evento")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDTO> obterTodos() {
        var eventos = this.eventoService.obterTodos();
        return eventos;
    }

    @GetMapping("/{id}")
    public Mono<EventoDTO> obterPorId(@PathVariable Long id) {
        var evento = this.eventoService.obterPorId(id);
        return evento;
    }

    @PostMapping
    public Mono<EventoDTO> cadastrarEvento(@RequestBody EventoDTO novoEvento) {
        var eventoCadastrado = this.eventoService.cadastrarEvento(novoEvento);
        return eventoCadastrado;
    }

    @PutMapping("/{id}")
    public Mono<EventoDTO> atualizarEvento(@PathVariable Long id, @RequestBody EventoDTO novasInformacoes) {
        var eventoAtualizado = this.eventoService.atualizarEvento(id, novasInformacoes);
        return eventoAtualizado;
    }

    @DeleteMapping("/{id}")
    public Mono<Void> removerEvento(@PathVariable Long id) {
        return this.eventoService.removerEvento(id);
    }

}
