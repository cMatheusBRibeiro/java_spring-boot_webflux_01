package br.com.alura.codechella.Ingressos;

import br.com.alura.codechella.Vendas.VendaDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/ingresso")
public class IngressoController {

    private final IngressoService ingressoService;
    private final Sinks.Many<IngressoDTO> ingressoSink;

    public IngressoController(IngressoService ingressoService) {
        this.ingressoService = ingressoService;
        this.ingressoSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping
    public Flux<IngressoDTO> obterTodos() {
        return this.ingressoService.obterTodos();
    }

    @GetMapping("/{id}")
    public Mono<IngressoDTO> obterPorId(@PathVariable Long id) {
        return this.ingressoService.obterPorId(id);
    }

    @PostMapping
    public Mono<IngressoDTO> cadastrar(@RequestBody IngressoDTO novoIngresso) {
        return this.ingressoService.cadastrar(novoIngresso);
    }

    @PutMapping("/{id}")
    public Mono<IngressoDTO> atualizar(@PathVariable Long id, @RequestBody IngressoDTO novasInformacoes) {
        return this.ingressoService.atualizar(id, novasInformacoes);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> remover(@PathVariable Long id) {
        return this.ingressoService.remover(id);
    }

    @PostMapping("/compra")
    public Mono<IngressoDTO> comprar(@RequestBody VendaDTO venda) {
        return this.ingressoService.comprar(venda).doOnSuccess(this.ingressoSink::tryEmitNext);
    }

}
