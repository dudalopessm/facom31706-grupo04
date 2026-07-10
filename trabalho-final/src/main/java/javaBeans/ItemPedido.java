package javaBeans;

public class ItemPedido {

    private int idPedido;
    private int idVinho;
    private int quantidade;
    private double precoUnitario;
    private Vinho vinho;

    public ItemPedido() {
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdVinho() {
        return idVinho;
    }

    public void setIdVinho(int idVinho) {
        this.idVinho = idVinho;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Vinho getVinho() {
        return vinho;
    }

    public void setVinho(Vinho vinho) {
        this.vinho = vinho;
    }

    public double getSubtotal() {
        return precoUnitario * quantidade;
    }
}
