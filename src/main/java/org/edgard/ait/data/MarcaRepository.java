package org.edgard.ait.data;

import org.edgard.ait.pojo.Marca;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MarcaRepository {
    private final List<Marca> marcas = new ArrayList<>();

    public MarcaRepository() {
        marcas.add(new Marca(1, "Fiat"));
        marcas.add(new Marca(2, "Volkswagen"));
        marcas.add(new Marca(3, "Chevrolet"));
    }

    public List<Marca> findAll() {
        return new ArrayList<>(marcas);
    }

    public Optional<Marca> findById(int id) {
        return marcas.stream().filter(m -> m.getId() == id).findFirst();
    }
}