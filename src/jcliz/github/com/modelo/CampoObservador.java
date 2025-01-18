package jcliz.github.com.modelo;

@FunctionalInterface
public interface CampoObservador {
    void eventoOcorreu(Campo campo, CampoEvento evento);
}
