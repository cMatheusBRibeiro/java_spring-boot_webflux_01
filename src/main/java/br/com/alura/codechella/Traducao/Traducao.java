package br.com.alura.codechella.Traducao;

import java.util.List;

public record Traducao(
        Texto responseData
) {

    public String getTexto() {
        return this.responseData.translatedText();
    }

}
