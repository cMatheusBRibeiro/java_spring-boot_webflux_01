package br.com.alura.codechella;

import br.com.alura.codechella.Eventos.EventoDTO;
import br.com.alura.codechella.Eventos.TipoEvento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodechellaApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void deveRetornarStatus201QuandoCriarNovoEvento() {
		EventoDTO dto = new EventoDTO(
				null,
				TipoEvento.SHOW,
				"KISS",
				LocalDate.parse("2025-01-01"),
				"Show de uma bela banda"
		);

		this.webTestClient
				.post()
				.uri("/evento")
				.bodyValue(dto)
				.exchange()
				.expectStatus()
				.isCreated();
	}

	@Test
	void deveRetornarUmEventoIgualAoEnviadoQuandoCriarUmNovoEvento() {
		EventoDTO dto = new EventoDTO(
				null,
				TipoEvento.SHOW,
				"KISS",
				LocalDate.parse("2025-01-01"),
				"Show de uma bela banda"
		);

		this.webTestClient
				.post()
				.uri("/evento")
				.bodyValue(dto)
				.exchange()
				.expectBody(EventoDTO.class)
				.value(eventoCriado -> {
					assertEquals(dto.nome(), eventoCriado.nome());
					assertEquals(dto.tipo(), eventoCriado.tipo());
					assertEquals(dto.data(), eventoCriado.data());
					assertEquals(dto.descricao(), eventoCriado.descricao());
				});
	}

	@Test
	void deveRetornarUmEventoComIdDiferenteDeNuloQuandoCriarUmNovoEvento() {
		EventoDTO dto = new EventoDTO(
				null,
				TipoEvento.SHOW,
				"KISS",
				LocalDate.parse("2025-01-01"),
				"Show de uma bela banda"
		);

		this.webTestClient
				.post()
				.uri("/evento")
				.bodyValue(dto)
				.exchange()
				.expectBody(EventoDTO.class)
				.value(eventoCriado -> {
					assertNotNull(eventoCriado.id());
				});
	}

	@Test
	void deveRetornarOEventoEsperadoNaPosicaoCorreta() {
		EventoDTO dto = new EventoDTO(
				13L,
				TipoEvento.SHOW,
				"The Weeknd",
				LocalDate.parse("2025-11-02"),
				"Um show eletrizante ao ar livre com muitos efeitos especiais."
		);

		this.webTestClient
				.get()
				.uri("/evento")
				.exchange()
				.expectBodyList(EventoDTO.class)
				.value(response -> {
					var eventoBuscado = response.get(12);

					assertEquals(dto.id(), eventoBuscado.id());
					assertEquals(dto.nome(), eventoBuscado.nome());
					assertEquals(dto.tipo(), eventoBuscado.tipo());
					assertEquals(dto.data(), eventoBuscado.data());
					assertEquals(dto.descricao(), eventoBuscado.descricao());
				});
	}

}
