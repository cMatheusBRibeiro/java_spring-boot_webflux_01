package br.com.alura.codechella.Ingressos;

import br.com.alura.codechella.Vendas.Venda;
import br.com.alura.codechella.Vendas.VendaDTO;
import br.com.alura.codechella.Vendas.VendaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final VendaRepository vendaRepository;

    public IngressoService(IngressoRepository ingressoRepository, VendaRepository vendaRepository) {
        this.ingressoRepository = ingressoRepository;
        this.vendaRepository = vendaRepository;
    }

    public Flux<IngressoDTO> obterTodos() {
        return this.ingressoRepository.findAll().map(IngressoDTO::toDTO);
    }

    public Mono<IngressoDTO> obterPorId(Long id) {
        return this.ingressoRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(IngressoDTO::toDTO);
    }

    public Mono<IngressoDTO> cadastrar(IngressoDTO novoIngresso) {
        return this.ingressoRepository.save(novoIngresso.toEntity()).map(IngressoDTO::toDTO);
    }

    public Mono<IngressoDTO> atualizar(Long id, IngressoDTO novasInformacoes) {
        return this.ingressoRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(ingressoExistente -> {
                    ingressoExistente.setEventoId(novasInformacoes.eventoId());
                    ingressoExistente.setTipo(novasInformacoes.tipo());
                    ingressoExistente.setValor(novasInformacoes.valor());
                    ingressoExistente.setTotal(novasInformacoes.total());

                    return this.ingressoRepository.save(ingressoExistente);
                })
                .map(IngressoDTO::toDTO);
    }

    public Mono<Void> remover(Long id) {
        return this.ingressoRepository
                .findById(id)
                .flatMap(this.ingressoRepository::delete);
    }

    @Transactional
    public Mono<IngressoDTO> comprar(VendaDTO dto) {
        return this.ingressoRepository.findById(dto.ingressoId()).flatMap(ingresso -> {
            var venda = new Venda();

            venda.setIngressoId(dto.ingressoId());
            venda.setTotal(dto.total());

            return this.vendaRepository.save(venda).then(Mono.defer(() -> {
                ingresso.setTotal(ingresso.getTotal() - dto.total());
                return this.ingressoRepository.save(ingresso);
            }));
        }).map(IngressoDTO::toDTO);
    }
}
