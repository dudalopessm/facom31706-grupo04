package javaBeans;

public class ItemSacola {

    private int idSacola;
    private int idVinho;
    private int quantidade;
    private Vinho vinho;

    public ItemSacola() {
    }

    public int getIdSacola() {
        return idSacola;
    }

    public void setIdSacola(int idSacola) {
        this.idSacola = idSacola;
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

    public Vinho getVinho() {
        return vinho;
    }

    public void setVinho(Vinho vinho) {
        this.vinho = vinho;
    }

    public double getSubtotal() {
        if (vinho != null) {
            return vinho.getPreco() * quantidade;
        }
        return 0;
    }
}
