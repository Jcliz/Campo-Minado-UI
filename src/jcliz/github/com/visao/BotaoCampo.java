package jcliz.github.com.visao;

import jcliz.github.com.modelo.Campo;
import jcliz.github.com.modelo.CampoEvento;
import jcliz.github.com.modelo.CampoObservador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BotaoCampo extends JButton
        implements CampoObservador, MouseListener {
    private final Color BG_PADRAO = new Color(184, 184, 184);
    private final Color BG_MARCAR = new Color(8, 179, 247);
    private final Color BG_EXPLOSAO = new Color(189, 66, 68);
    private final Color TEXTO_VERDE = new Color(0, 100, 0);

    private Campo campo;

    public BotaoCampo(Campo campo) {
        this.campo = campo;
        setBackground(BG_PADRAO);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));

        addMouseListener(this);
        campo.registrarObservador(this);
    }

    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        switch (evento) {
            case ABRIR:
                aplicarEstiloAbrir();
                break;
                
            case MARCAR:
                aplicarEstiloMarcar();
                break;

            case EXPLODIR:
                aplicarEstiloExplodir();
                break;
                
            default:
                aplicarEstiloPadrao();
        }
    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setText("");
    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLOSAO);
        setForeground(Color.WHITE);
        setText("X");
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCAR);
        setForeground(Color.BLACK);
        setText("+");
    }

    private void aplicarEstiloAbrir() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (campo.isMinado()) {
            setBackground(BG_EXPLOSAO);
            return;
        }

        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        switch (campo.minasNaVizinanca()) {
            case 1:
                setForeground(TEXTO_VERDE);
                break;

            case 2:
                setForeground(Color.BLUE);
                break;

            case 3:
                setForeground(Color.YELLOW);
                break;

            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;

            default:
                setForeground(Color.PINK);
                break;
        }

        String valor = !campo.vizinhancaSegura() ?
                campo.minasNaVizinanca() + "" : "";
        setText(valor);
    }

    //interface de eventos do mouse
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            campo.abrir();
        } else {
            campo.alternarMarcacao();
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}