package br.com.alura.codechella.Eventos;

import br.com.alura.codechella.Traducao.TraducaoDeTextos;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/evento")
public class EventoController {

    private final EventoService eventoService;
    private final Sinks.Many<EventoDTO> eventoSink;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
        this.eventoSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping
    public Flux<EventoDTO> obterTodos() {
        return this.eventoService.obterTodos();
    }

    @GetMapping(value = "/tipo/{tipo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDTO> obterEventosPorTipo(@PathVariable String tipo) {
        return Flux
                .merge(this.eventoService.obterPorTipo(tipo), this.eventoSink.asFlux())
                .delayElements(Duration.ofSeconds(1));
    }

    @GetMapping("/{id}")
    public Mono<EventoDTO> obterPorId(@PathVariable Long id) {
        return this.eventoService.obterPorId(id);
    }

    @GetMapping("/{id}/traduzir/{idioma}")
    public Mono<String> obterTraducao(@PathVariable Long id, @PathVariable String idioma) {
        return this.eventoService.obterPorId(id)
                .flatMap(evento -> TraducaoDeTextos.obterTraducao(evento.descricao(), idioma));
    }

    @PostMapping
    public Mono<EventoDTO> cadastrarEvento(@RequestBody EventoDTO novoEvento) {
        return this.eventoService
                .cadastrarEvento(novoEvento)
                .doOnSuccess(this.eventoSink::tryEmitNext);
    }

    @PutMapping("/{id}")
    public Mono<EventoDTO> atualizarEvento(@PathVariable Long id, @RequestBody EventoDTO novasInformacoes) {
        return this.eventoService.atualizarEvento(id, novasInformacoes);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> removerEvento(@PathVariable Long id) {
        return this.eventoService.removerEvento(id);
    }

}
