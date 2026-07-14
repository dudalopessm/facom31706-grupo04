package lab05.modelo;

public class ItemPedido {
    private int id;
    private int idPedido;
    private int idVinho;
    private int quantidade;
    private double precoUnitario;
    private String vinhoNome;
    private String dataItem;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public int getIdVinho() { return idVinho; }
    public void setIdVinho(int idVinho) { this.idVinho = idVinho; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }
    public String getVinhoNome() { return vinhoNome; }
    public void setVinhoNome(String vinhoNome) { this.vinhoNome = vinhoNome; }
    public String getDataItem() { return dataItem; }
    public void setDataItem(String dataItem) { this.dataItem = dataItem; }
}
