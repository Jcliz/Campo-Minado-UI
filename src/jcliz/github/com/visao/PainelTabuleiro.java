package jcliz.github.com.visao;

import jcliz.github.com.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {
    public PainelTabuleiro(Tabuleiro tabuleiro) {
        setLayout(new GridLayout(
                tabuleiro.getLinhas(), tabuleiro.getColunas())); //layout de linhas e colunas

        tabuleiro.forEachCampo(c -> add(new BotaoCampo(c)));

        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() -> {
                if(e.isGanhou()) {
                JOptionPane.showMessageDialog(this, "Ganhou! :D");
                } else {
                    JOptionPane.showMessageDialog(this, "Perdeu... D:");
                }

                tabuleiro.reiniciar();
            });
        });
    }
}