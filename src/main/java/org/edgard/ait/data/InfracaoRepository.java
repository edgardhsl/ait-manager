package org.edgard.ait.data;

import org.edgard.ait.pojo.Infracao;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfracaoRepository {
    private final List<Infracao> infracoes = new ArrayList<>();

    public InfracaoRepository() {
        infracoes.add(new Infracao(1, "Excesso de velocidade", 150.00));
        infracoes.add(new Infracao(2, "Estacionamento proibido", 80.00));
        infracoes.add(new Infracao(3, "Avanço de sinal vermelho", 200.00));
    }

    public List<Infracao> findAll() {
        return new ArrayList<>(infracoes);
    }

    public Optional<Infracao> findById(int id) {
        return infracoes.stream().filter(i -> i.getId() == id).findFirst();
    }
}