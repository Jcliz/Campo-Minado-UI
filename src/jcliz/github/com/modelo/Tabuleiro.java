package jcliz.github.com.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {
    private final int linhas;
    private final int colunas;
    private final int minas;

    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observadores =
            new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        //inicia a geração dos atributos necessários
        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public void forEachCampo(Consumer<Campo> funcao) {
        campos.forEach(funcao);
    }

    public void registrarObservador(Consumer<ResultadoEvento> observador) {
        observadores.add(observador);
    }

    private void notificarObservadores(boolean resultado) {
        observadores.stream()
                .forEach(o -> o.accept(new ResultadoEvento(resultado)));
    }

    public void abrir(int linha, int coluna) {
        campos.parallelStream()
                //filtra os campos para encontrar o desejado com os parametros
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                //caso no método abrir a explosão ser jogada, cairá no catch
                .ifPresent(Campo::abrir);
    }

    public void marcar(int linha, int coluna) {
        campos.parallelStream()
                //filtra os campos para encontrar o desejado com os parametros
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(Campo::alternarMarcacao);
    }

    public void gerarCampos() {
        //geração de campos com uma geração de matriz
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Campo campo = new Campo(i, j);
                campo.registrarObservador(this);
                campos.add(campo);
            }
        }
    }

    public void associarVizinhos() {
        //percorre a matriz adicionando os vizinhos
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                c1.adicionarVizinho(c2);
            }
        }
    }

    public void sortearMinas() {
        long minasArmadas;
        //predicado para a verificação se está minado ou nao
        Predicate<Campo> minado = Campo::isMinado;

        do {
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(minado).count();

        } while (minasArmadas < minas);
    }

    //retorna true se todos os campos tiveram seus objetivos alcançados
    public boolean objetivoAlcancado() {
        return campos.stream().allMatch(Campo::objetivoAlcancado);
    }

    //reinicia o tabuleiro, ou seja, cada campo
    public void reiniciar() {
        campos.forEach(Campo::reiniciar);
        sortearMinas();
    }

    @Override
    public void eventoOcorreu (Campo campo, CampoEvento evento) {
        if (evento == CampoEvento.EXPLODIR) {
            mostrarMinas();
            notificarObservadores(false);
        } else if (objetivoAlcancado()) {
            notificarObservadores(true);
        }
    }

    private void mostrarMinas() {
        campos.stream()
                .filter(Campo::isMinado)
                .forEach(c -> c.setAberto(true));
    }

    public List<Campo> getCampos() {
        return campos;
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }
}