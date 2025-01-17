package jcliz.github.com.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro {
    private int linhas;
    private int colunas;
    private int minas;

    private final List<Campo> campos = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        //inicia a geração dos atributos necessários
        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public void abrir(int linha, int coluna) {
        try {
            campos.parallelStream()
                    //filtra os campos para encontrar o desejado com os parametros
                    .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                    .findFirst()
                    //caso no método abrir a explosão ser jogada, cairá no catch
                    .ifPresent(Campo::abrir);
        } catch (Exception e) {
            //FIXME Ajustar a implementação do método abrir
            campos.forEach(c -> c.setAberto(true));
            throw e;
        }
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
                campos.add(new Campo(i, j));
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

    public List<Campo> getCampos() {
        return campos;
    }
}