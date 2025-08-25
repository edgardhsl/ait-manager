package org.edgard.ait;

import javax.swing.*;

import static org.edgard.ait.view.AITCadastro.criarTelaListagem;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            criarTelaListagem();
        });
    }
}