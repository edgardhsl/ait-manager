package org.edgard.ait.pojo;

public class Infracao {
    private int id;
    private String descricao;
    private double valor;

    public Infracao(int id, String descricao, double valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }

    @Override
    public String toString() {
        return descricao + " (R$ " + String.format("%.2f", valor) + ")";
    }
}

