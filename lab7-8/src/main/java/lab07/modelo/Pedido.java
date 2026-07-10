package lab07.modelo;

public class Pedido {
    private int id;
    private String clienteCpf;
    private String dataPedido;
    private String status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getClienteCpf() { return clienteCpf; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }
    public String getDataPedido() { return dataPedido; }
    public void setDataPedido(String dataPedido) { this.dataPedido = dataPedido; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
