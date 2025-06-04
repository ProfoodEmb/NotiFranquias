package Classes;


import java.math.BigDecimal;

public class FatClientesFran {

    private Integer nunota;
    private String descrprod;
    private Integer codProduto;
    private String cliente;
    private Integer danfe;
    private Integer qtdNegociada;
    private String matriz;
    private String data;
    private BigDecimal totalSicmsSipi;
    private BigDecimal valorDevolucao;
    private String cpfCnpjCLiente;
    private String emailVendedor;

    // Construtor vazio
    public FatClientesFran() {
    }

    // Construtor com todos os atributos
    public FatClientesFran(Integer nunota, BigDecimal valorDevolucao, String cpfCnpjCLiente, String descrprod, Integer codProduto, String cliente, Integer danfe,
                           Integer qtdNegociada, String matriz, String data, BigDecimal totalSicmsSipi,String emailVendedor) {
        this.nunota = nunota;
        this.descrprod = descrprod;
        this.cliente = cliente;
        this.danfe = danfe;
        this.qtdNegociada = qtdNegociada;
        this.matriz = matriz;
        this.data = data;
        this.totalSicmsSipi = totalSicmsSipi;
        this.codProduto = codProduto;
        this.cpfCnpjCLiente = cpfCnpjCLiente;
        this.valorDevolucao = valorDevolucao;
        this.emailVendedor = emailVendedor;
    }

    // Getters e Setters


    public String getEmailVendedor() {
        return emailVendedor;
    }

    public void setEmailVendedor(String emailVendedor) {
        this.emailVendedor = emailVendedor;
    }

    public void setValorDevolucao(BigDecimal valorDevolucao) {
        this.valorDevolucao = valorDevolucao;
    }

    public BigDecimal getValorDevolucao() {
        return valorDevolucao;
    }

    public String getCpfCnpjCLiente(){return cpfCnpjCLiente;}

    public void setCpfCnpjCLiente(String cpfCnpjCLiente) {
        this.cpfCnpjCLiente = cpfCnpjCLiente;
    }

    public void setCodProduto(Integer codProduto) {
        this.codProduto = codProduto;
    }

    public Integer getCodProduto() {
        return codProduto;
    }

    public Integer getNunota() {
        return nunota;
    }

    public void setNunota(Integer nunota) {
        this.nunota = nunota;
    }

    public String getDescrprod() {
        return descrprod;
    }

    public void setDescrprod(String descrprod) {
        this.descrprod = descrprod;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Integer getDanfe() {
        return danfe;
    }

    public void setDanfe(Integer danfe) {
        this.danfe = danfe;
    }

    public Integer getQtdNegociada() {
        return qtdNegociada;
    }

    public void setQtdNegociada(Integer qtdNegociada) {
        this.qtdNegociada = qtdNegociada;
    }

    public String getMatriz() {
        return matriz;
    }

    public void setMatriz(String matriz) {
        this.matriz = matriz;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BigDecimal getTotalSicmsSipi() {
        return totalSicmsSipi;
    }

    public void setTotalSicmsSipi(BigDecimal totalSicmsSipi) {
        this.totalSicmsSipi = totalSicmsSipi;
    }

    @Override
    public String toString() {
        return "FatClientesFran{" +
                "nunota=" + nunota +
                ", descrprod='" + descrprod + '\'' +
                ", cliente='" + cliente + '\'' +
                ", danfe='" + danfe + '\'' +
                ", qtdNegociada=" + qtdNegociada +
                ", matriz='" + matriz + '\'' +
                ", data='" + data + '\'' +
                ", totalSicmsSipi=" + totalSicmsSipi +
                '}';
    }
}
