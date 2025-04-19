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

}
