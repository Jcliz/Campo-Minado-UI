package jcliz.github.com.visao;

import jcliz.github.com.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {
    public PainelTabuleiro(Tabuleiro tabuleiro) {

        setLayout(new GridLayout(
                tabuleiro.getLinhas(), tabuleiro.getColunas())); //layout de linhas e colunas

        int total = tabuleiro.getLinhas() * tabuleiro.getColunas();

        tabuleiro.forEachCampo(c -> add(new BotaoCampo(c)));

        tabuleiro.registrarObservador(e -> {
            //TODO mostrar resultado para o usuário
        });
    }
}