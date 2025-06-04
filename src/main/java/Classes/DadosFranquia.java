package Classes;

import java.math.BigDecimal;

public class DadosFranquia {
    private Integer codMatriz;
    private BigDecimal comissao;
    private String nome;
    private String email;
    private String emailVendedor;
    private BigDecimal inadinplencia;

    public DadosFranquia(){
    }

    public DadosFranquia(String nome, Integer codMatriz, BigDecimal comissao,BigDecimal inadinplencia, String email,String emailVendedor){

        this.nome = nome;
        this.codMatriz = codMatriz;
        this.comissao = comissao;
        this.email = email;
        this.inadinplencia = inadinplencia;
        this.emailVendedor = emailVendedor;
    }

    public String getEmailVendedor() {
        return emailVendedor;
    }

    public void setEmailVendedor(String emailVendedor) {
        this.emailVendedor = emailVendedor;
    }

    public BigDecimal getInadinplencia() {
        return inadinplencia;
    }

    public void setInadinplencia(BigDecimal inadinplencia) {
        this.inadinplencia = inadinplencia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public Integer getCodMatriz() {
        return codMatriz;
    }

    public void setCodMatriz(Integer codMatriz) {
        this.codMatriz = codMatriz;
    }
}
