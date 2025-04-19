package br.com.alura.codechella.Traducao;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class TraducaoDeTextos {

    public static Mono<String> obterTraducao(String texto, String idioma) {
        WebClient webClient = WebClient.builder()
                .baseUrl(System.getenv("MY_MEMORY_URL") + "/get?q=" + texto + "&langpair=pt|" + idioma)
                .build();

        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();

        return webClient
                .get()
                .retrieve()
                .bodyToMono(Traducao.class)
                .map(Traducao::getTexto);
    }

}
