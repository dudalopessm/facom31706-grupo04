package javaBeans;

import java.time.LocalDateTime;

public class Pedido {

    private int id;
    private LocalDateTime dataConclusao;
    private double valorTotal;
    private String statusPagamento;
    private String statusEnvio;
    private int idSacola;

    public Pedido() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getStatusEnvio() {
        return statusEnvio;
    }

    public void setStatusEnvio(String statusEnvio) {
        this.statusEnvio = statusEnvio;
    }

    public int getIdSacola() {
        return idSacola;
    }

    public void setIdSacola(int idSacola) {
        this.idSacola = idSacola;
    }
}
