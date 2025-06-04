package br.Fran;

import Classes.DadosFranquia;
import Classes.FatClientesFran;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;
import utils.WebHookService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AcAgNotiFranquia implements ScheduledAction {
    ObjectMapper objectMapper = new ObjectMapper();
    Integer dataPesquisa = 1;

    // Calcula o mês de referência: se a consulta usa LAST_DAY(ADD_MONTHS(SYSDATE, -1)),
    // o mês de referência é o mês anterior ao atual.
    LocalDate mesConsulta = LocalDate.now().minusMonths(dataPesquisa);
    // Formata com o padrão abreviado (ex.: "fev/2025"). Você pode ajustar o padrão conforme necessário.
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM/yyyy", new Locale("pt", "BR"));
    String mesConsultaStr = mesConsulta.format(dtf);

    @Override
    public void onTime(ScheduledActionContext scheduledActionContext) {
        // Obtém a data de hoje
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        ArrayList<DadosFranquia> dadosFranquias  = new ArrayList<>();

        //dadosFranquias.add(new DadosFranquia("DOG KING", 1073, new BigDecimal("0.125"), new BigDecimal("0.01"), "", ""));
        dadosFranquias.add(new DadosFranquia("GENERAL PRIME BURGUER", 1240, new BigDecimal("0.04"), new BigDecimal("0.01"), "adriano.boscolo@hotmail.com,sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));
        dadosFranquias.add(new DadosFranquia("HORA DO PASTEL", 2404, new BigDecimal("0.04"), new BigDecimal("0.01"), "financeiro@horadopastel.com,sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));
        dadosFranquias.add(new DadosFranquia("JAH AÇAÍ", 2103, new BigDecimal("0.04"), new BigDecimal("0.01"), "fernanda.pinto@jah.company,weverton.oliveira@jah.company,lilian.campos@jah.company,sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));
        dadosFranquias.add(new DadosFranquia("MUNDO ANIMAL", 2788, new BigDecimal("0.04"), new BigDecimal("0.01"), "cristianicapelletti@euamomundoanimal.com.br,mauricioboneberg@euamomundoanimal.com.br,juliana.furtado@euamomundoanimal.com.br,sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));
        dadosFranquias.add(new DadosFranquia("MR. FRITZ", 3483, new BigDecimal("0.04"), new BigDecimal("0.01"), "viniciuspaiva@mrfritz.com.br,admmrfritz@gmail.com,sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));
        dadosFranquias.add(new DadosFranquia("MR. HOPPY", 1074, new BigDecimal("0.04"), new BigDecimal("0.01"), "sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));
        dadosFranquias.add(new DadosFranquia("NANICA", 2063, new BigDecimal("0.04"), new BigDecimal("0.01"), "mariana@nanicabr.com.br,jainefaustino@nanicabr.com.br,sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));
        dadosFranquias.add(new DadosFranquia("SODIE DOCES E SALGADOS", 2604, new BigDecimal("0.02"), new BigDecimal("0.00"), "compras@sodiedoces.com.br,sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));
        dadosFranquias.add(new DadosFranquia("THE WAFFLE KING", 1085, new BigDecimal("0.04"), new BigDecimal("0.01"), "contasapagar@thewaffleking.com.br,contasareceber@thewaffleking.com.br,financeiro@thewaffleking.com.br,sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));
        dadosFranquias.add(new DadosFranquia("TT BURGUER", 1609, new BigDecimal("0.04"), new BigDecimal("0.01"), "mayli@ttburger.com.br,bruno.araujo@ttburger.com.br,sistemas@profood.com.br,david@profood.com.br,cobranca@tuicial.com.br,cobranca2@tuicial.com.br", ""));

        try {
            EntityFacade entityFacade = EntityFacadeFactory.getDWFFacade();
            JdbcWrapper jdbc = entityFacade.getJdbcWrapper();
            jdbc.openSession();

            for (DadosFranquia dadoFranquia : dadosFranquias){

                // Exemplo de montagem da query, concatenando a condição de data:
                String sql =
                        "SELECT\n" +
                                "    CAB.nunota,\n" +
                                "    pro.codprod AS Codigo_Produto,\n" +
                                "    PRO.descrprod AS Descricao_Produto,\n" +
                                "    PAR.nomeparc AS CLIENTE,\n" +
                                "    PAR.cgc_cpf,\n" +
                                "    CAB.Danfe,\n" +
                                "    SUM(CASE WHEN TPO.tipmov = 'D' THEN ITE.qtdneg ELSE ITE.qtdneg END) AS QtdNegociada,\n" +
                                "\n" +
                                "    -- NOVO cálculo do valor da devolução, igual ao TOTALLIQNOTICMS\n" +
                                "    SUM(CASE WHEN TPO.tipmov = 'D' THEN (ITE.vlrtot - ITE.vlrdesc) ELSE 0 END)\n" +
                                "      - SUM(CASE WHEN TPO.tipmov = 'D' THEN (ITE.qtdconferida * ITE.vlrunit) ELSE 0 END)\n" +
                                "      - SUM(CASE WHEN TPO.tipmov = 'D' THEN ITE.vlricms ELSE 0 END) AS ValorDevolucao,\n" +
                                "\n" +
                                "    MAT.nomeparc AS Matriz,\n" +
                                "    TO_CHAR(CAB.dtneg, 'dd/mm/yyyy') AS DATA,\n" +
                                "    VEN.email AS ven_email,\n" +
                                "\n" +
                                "    -- TOTAL líquido como já estava\n" +
                                "    SUM(ITE.vlrtot - ITE.vlrdesc)\n" +
                                "      - SUM(ITE.qtdconferida * ITE.vlrunit)\n" +
                                "      - SUM(ITE.vlricms) AS TotalSicmsSipi\n" +
                                "\n" +
                                "FROM tgfcab CAB\n" +
                                "INNER JOIN tgfite ITE ON CAB.nunota = ITE.nunota\n" +
                                "INNER JOIN tgfven VEN ON CAB.codvend = VEN.codvend\n" +
                                "INNER JOIN tgfpro PRO ON ITE.codprod = PRO.codprod\n" +
                                "INNER JOIN tgfpar PAR ON CAB.codparc = PAR.codparc\n" +
                                "INNER JOIN tgfpar MAT ON PAR.codparcmatriz = MAT.codparc\n" +
                                "INNER JOIN tgftop TPO ON CAB.codtipoper = TPO.codtipoper\n" +
                                "       AND CAB.dhtipoper = TPO.dhalter\n" +
                                "WHERE\n" +
                                "    TPO.codtipoper IN ('1727', '1724', '1118', '1709', '1112', '1131', '1130', '1200', '1201')\n" +
                                "    AND CAB.nunota <> 130811\n" +
                                "    AND CAB.dtneg BETWEEN TRUNC(ADD_MONTHS(SYSDATE, -" + dataPesquisa + "), 'MM')\n" +
                                "                      AND LAST_DAY(ADD_MONTHS(SYSDATE, -" + dataPesquisa + "))\n" +
                                "    AND CAB.Danfe IS NOT NULL\n" +
                                "    AND (PAR.ad_NAOINCLUIRCOMISSAO IS NULL OR PAR.ad_NAOINCLUIRCOMISSAO = 'N')\n" +
                                "    AND MAT.codparc = " + dadoFranquia.getCodMatriz() + "\n" +
                                "    AND ITE.USOPROD <> 'D'\n" +
                                "GROUP BY\n" +
                                "    CAB.nunota,\n" +
                                "    VEN.email,\n" +
                                "    CAB.Danfe,\n" +
                                "    PRO.descrprod,\n" +
                                "    PRO.codprod,\n" +
                                "    PAR.nomeparc,\n" +
                                "    PAR.cgc_cpf,\n" +
                                "    MAT.nomeparc,\n" +
                                "    PAR.ad_NAOINCLUIRCOMISSAO,\n" +
                                "    TO_CHAR(CAB.dtneg, 'dd/mm/yyyy')";

                List<FatClientesFran> listaFatClientes = new ArrayList<>();


                try (PreparedStatement stmt = jdbc.getPreparedStatement(sql);

                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        // Cria um objeto do tipo ProdutoEntrada
                        FatClientesFran fatClientesFran = new FatClientesFran();

                        //================================================================================
                        BigDecimal bdCodigo = rs.getBigDecimal("CODIGO_PRODUTO");
                        Integer codigoProduto = (bdCodigo != null ? bdCodigo.intValue() : 0);
                        fatClientesFran.setCodProduto(codigoProduto);


                        //================================================================================
                        Integer NUNOTA = rs.getObject("NUNOTA", Integer.class);
                        if (NUNOTA == null) {
                            // Se a coluna for nula no banco
                            NUNOTA = 0;
                        }
                        fatClientesFran.setNunota(NUNOTA);

                        //================================================================================

                        Integer DANFE = rs.getObject("DANFE", Integer.class);
                        if (DANFE == null) {
                            // Se a coluna for nula no banco
                            DANFE = 0;
                        }

                        fatClientesFran.setDanfe(DANFE);

                        //================================================================================

                        fatClientesFran.setDescrprod(//DESCRICAO_PRODUTO
                                rs.getString("DESCRICAO_PRODUTO") != null
                                        ? rs.getString("DESCRICAO_PRODUTO")
                                        : "*"
                        );

                        //================================================================================

                        fatClientesFran.setEmailVendedor(//ven_email
                                rs.getString("ven_email") != null
                                        ? rs.getString("ven_email")
                                        : "*"
                        );

                        //================================================================================

                        Integer QTDNEGOCIADA = rs.getObject("QTDNEGOCIADA", Integer.class);
                        if (QTDNEGOCIADA == null) {
                            // Se a coluna for nula no banco
                            QTDNEGOCIADA = 0;
                        }
                        fatClientesFran.setQtdNegociada(QTDNEGOCIADA);

                        //================================================================================

                        fatClientesFran.setCliente(//CLIENTE
                                rs.getString("CLIENTE") != null
                                        ? rs.getString("CLIENTE")
                                        : ""
                        );

                        //================================================================================

                        fatClientesFran.setMatriz(//MATRIZ
                                rs.getString("MATRIZ") != null
                                        ? rs.getString("MATRIZ")
                                        : ""
                        );

                        //================================================================================

                        fatClientesFran.setData(//DATA
                                rs.getString("DATA") != null
                                        ? rs.getString("DATA")
                                        : ""
                        );

                        //================================================================================

                        fatClientesFran.setCpfCnpjCLiente(//CGC_CPF
                                rs.getString("CGC_CPF") != null
                                        ? rs.getString("CGC_CPF")
                                        : ""
                        );

                        //================================================================================

                        BigDecimal TOTALSICMSSIPI = rs.getObject("TOTALSICMSSIPI", BigDecimal.class);
                        if (TOTALSICMSSIPI == null) {
                            // Se a coluna for nula no banco
                            TOTALSICMSSIPI = BigDecimal.ZERO;
                        }
                        fatClientesFran.setTotalSicmsSipi(TOTALSICMSSIPI);

                        //================================================================================

                        BigDecimal VALORDEVOLUCAO = rs.getObject("VALORDEVOLUCAO", BigDecimal.class);
                        if (VALORDEVOLUCAO == null) {
                            // Se a coluna for nula no banco
                            VALORDEVOLUCAO = BigDecimal.ZERO;
                        }

                        fatClientesFran.setValorDevolucao(VALORDEVOLUCAO);


                        //================================================================================

                        // Adiciona o objeto 'pedido' na lista
                        listaFatClientes.add(fatClientesFran);
                    }

                    String emailHtml = gerarHtmlPedidosSemInline(listaFatClientes,dadoFranquia);

                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(listaFatClientes);

                    WebHookService.post(json);

                    // Se houve resultados, envia a lista no formato HTML
                    if (!listaFatClientes.isEmpty()) {
                        JapeSession.open();

                        JapeWrapper email = JapeFactory.dao("MSDFilaMensagem");

                        DynamicVO cabApoVO = email.create()
                                .set("STATUS", "Pendente")
                                .set("CODCON", BigDecimal.valueOf(0))
                                .set("TENTENVIO", BigDecimal.valueOf(1))
                                .set("MENSAGEM", emailHtml.toCharArray())
                                .set("TIPOENVIO", "E")
                                .set("MAXTENTENVIO", BigDecimal.valueOf(3))
                                .set("NUANEXO", null)
                                .set("ASSUNTO", "Resumo de Comissões | "+mesConsultaStr+" | "+dadoFranquia.getNome())
                                .set("EMAIL", Stream.of(dadoFranquia.getEmail(), dadoFranquia.getEmailVendedor())
                                        .filter(e -> e != null && !e.trim().isEmpty())
                                        .collect(Collectors.joining(",")))
                                .set("MIMETYPE", null)
                                .set("TIPODOC", null)
                                .set("CODUSU", BigDecimal.valueOf(0))
                                .set("NUCHAVE", null)
                                .set("CODUSUREMET", null)
                                .set("REENVIAR", "N")
                                .set("MSGERRO", null)
                                .set("CODSMTP", null)
                                .set("DHULTTENTA", null)
                                .set("DBHASHCODE", null)
                                .set("CODCONTASMS", null)
                                .set("CELULAR", null)
                                .save();

                        BigDecimal codFilaGerado = cabApoVO.asBigDecimal("CODFILA");

                        WebHookService.post("{cod gerado: " + codFilaGerado + "}");
                    }

                }
            }
        } catch (Exception e) {
            WebHookService.post("{\"Deu erro: "+ e +"\"}");
        } finally {
            JapeSession.close();
        }
    }



    /**
     * Gera um HTML de notificação de pedidos.
     * Todos os estilos são definidos como classes dentro de <style> e aplicados via class="...".
     * O layout é baseado em <table> para garantir maior compatibilidade em diversos clientes de e-mail.
     */
    private String gerarHtmlPedidosSemInline(List<FatClientesFran> fatClientesFrans, DadosFranquia dadoFranquia) {


        // Cria instâncias de NumberFormat para valores monetários e inteiros
        NumberFormat nfValor = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        NumberFormat nfInteiro = NumberFormat.getIntegerInstance(new Locale("pt", "BR"));


        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"pt-BR\">");
        sb.append("<head>");
        sb.append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        sb.append("    <title>Relatório Franquia - ").append(dadoFranquia.getNome()).append("</title>");
        sb.append("</head>");

        sb.append("<body style=\"background-color:#fafafa;font-family:'Inter', Arial, sans-serif; margin:0; padding:0;\">");
        sb.append("    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"");
        sb.append("           style=\"width:100%; margin:0 auto; background-color:#fafafa; padding:24px; border-collapse:collapse; font-family:Arial, sans-serif;\">");
        sb.append("        <tr>");
        sb.append("            <td align=\"center\">");
        sb.append("                <table width=\"600\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#fff; border-radius:8px; box-shadow:0 1px 3px rgba(0,0,0,0.1); margin-bottom:30px; border-collapse:collapse; margin:20px auto; font-family:Arial, sans-serif;\">");
        sb.append("                    <tr>");
        sb.append("                        <td align=\"center\" style=\"padding-bottom: 15px; padding-top: 15px;\">");
        sb.append("                            <img src=\"https://imgur.com/uyCBGU6.png\" alt=\"Logo da Empresa\" style=\"max-width:200px; height:auto;\">");
        sb.append("                        </td>");
        sb.append("                    </tr>");
        sb.append("                    <tr>");
        sb.append("                        <td style=\"padding:15px; padding-top: 0px;\">");
        sb.append("                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-bottom:15px; border-collapse:collapse; font-family:Arial, sans-serif;\">");
        sb.append("                                <tr>");
        sb.append("                                    <td align=\"center\" style=\"font-size:24px; font-weight:600; color:#333; border-bottom:none; padding:8px; padding-bottom: 0px;\">");
        sb.append("                                    ").append(dadoFranquia.getNome()).append(" - ").append(mesConsultaStr);
        sb.append("                                    </td>");
        sb.append("                                </tr>");
        sb.append("                            </table>");

        // Calcula o total geral de devolução de todos os clientes
        java.math.BigDecimal totalDevolucao = fatClientesFrans.stream()
                .map(FatClientesFran::getValorDevolucao)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        // Calcula o total geral de todos os clientes
        java.math.BigDecimal totalGeral = fatClientesFrans.stream()
                .map(FatClientesFran::getTotalSicmsSipi)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        BigDecimal faturamentoLiquido = totalGeral.subtract(totalDevolucao);

        BigDecimal comissaoTotal = totalGeral.subtract(totalDevolucao) //como o valor de devolução esta com o sinal "-", somando os valores, ele vai subtrair corretamente
                .multiply(dadoFranquia.getComissao())
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal comissaoEmPorcentagem = dadoFranquia.getComissao().multiply(new BigDecimal("100"));

        // Cria um DecimalFormat que use vírgula e remova zeros desnecessários
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        DecimalFormat df = new DecimalFormat("0.##", symbols);

        // Formata o BigDecimal em string
        String comissaoFormatada = df.format(comissaoEmPorcentagem);

        BigDecimal inadimplenciaTotal = totalGeral.subtract(totalDevolucao) //como o valor de devolução esta com o sinal "-", somando os valores, ele vai subtrair corretamente
                .multiply(dadoFranquia.getInadinplencia())
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal inadimplenciaEmPorcentagem = dadoFranquia.getInadinplencia().multiply(new BigDecimal("100"));

        // Formata o BigDecimal em string
        String inadimplenciaFormatada = df.format(inadimplenciaEmPorcentagem);

        sb.append("<table width=\"100%\"");
        sb.append("    style=\"border-collapse: collapse; margin: 20px auto; font-family: Arial, sans-serif;\">");
        sb.append("    <thead>");
        sb.append("        <tr class=\"linha-destaque\"");
        sb.append("            style=\"background-color: #A62B1F; color: #fff; font-weight: bold;\">");
        sb.append("            <td style=\"padding: 8px; border-bottom: 1px solid #ddd;  border-radius: 8px 0px 0px 0px;\">");
        sb.append("            </td>");
        sb.append("            <td style=\"padding: 8px; text-align: center; border-bottom: 1px solid #ddd;\">");
        sb.append("                COMISSÃO</td>");
        sb.append("            <td style=\"padding: 8px; border-bottom: 1px solid #ddd;  border-radius: 0px 8px 0px 0px;\">");
        sb.append("            </td>");
        sb.append("        </tr>");
        sb.append("    </thead>");
        sb.append("    <tbody>");
        sb.append("        <tr>");
        sb.append("            <td></td>");
        sb.append("            <td style=\" padding: 4px; text-align: left;\">");
        sb.append("                <span>Faturamento Total: </span>");
        sb.append("                <span>").append(nfValor.format(totalGeral)).append("</span>");
        sb.append("            </td>");
        sb.append("            <td></td>");
        sb.append("        </tr>");
        sb.append("        <tr>");
        sb.append("            <td></td>");
        sb.append("            <td style=\" padding: 4px; text-align: left;\">");
        sb.append("                <span>Devolução Total: ").append(nfValor.format(totalDevolucao)).append("</span>");
        sb.append("            </td>");
        sb.append("            <td></td>");
        sb.append("        </tr>");
        sb.append("        <tr>");
        sb.append("            <td></td>");
        sb.append("            <td style=\" padding: 4px; text-align: left;\">");
        sb.append("                <span>Faturamento Liquido: ").append(nfValor.format(faturamentoLiquido)).append("</span>");
        sb.append("            </td>");
        sb.append("            <td></td>");
        sb.append("        </tr>");
        sb.append("        <tr>");
        sb.append("            <td></td>");
        sb.append("            <td style=\" padding: 4px\">");
        sb.append("                <table width=\"100%\" style=\"border-collapse: collapse;\">");
        sb.append("                    <tr>");
        sb.append("                      <td style=\"text-align: left;\">");
        sb.append("                        <span>Comissão: ").append(comissaoFormatada).append("%</span>");
        sb.append("                      </td>");
        sb.append("                      <td style=\"text-align: right;\">");
        sb.append("                        <span>Comissão Total: ").append(nfValor.format(comissaoTotal)).append("</span>");
        sb.append("                      </td>");
        sb.append("                    </tr>");
        sb.append("                </table>");
        sb.append("            </td>");
        sb.append("            <td></td>");
        sb.append("        </tr>");

        if (dadoFranquia.getInadinplencia().compareTo(BigDecimal.ZERO) > 0) {
            sb.append("        <tr>");
            sb.append("            <td></td>");
            sb.append("            <td style=\" padding: 4px\">");
            sb.append("                <table width=\"100%\" style=\"border-collapse: collapse;\">");
            sb.append("                    <tr>");
            sb.append("                      <td style=\"text-align: left;\">");
            sb.append("                        <span>Fundo de Inadimplência: ").append(inadimplenciaFormatada).append("%</span>");
            sb.append("                      </td>");
            sb.append("                      <td style=\"text-align: right;\">");
            sb.append("                        <span>F.I Total: ").append(nfValor.format(inadimplenciaTotal)).append("</span>");
            sb.append("                      </td>");
            sb.append("                    </tr>");
            sb.append("                </table>");
            sb.append("            </td>");
            sb.append("            <td></td>");
            sb.append("        </tr>");
        }

        sb.append("    </tbody>");
        sb.append("    <tfoot>");
        sb.append("        <tr class=\"linha-destaque\"");
        sb.append("            style=\"background-color: #A62B1F; color: #fff; font-weight: bold;\">");
        sb.append("            <td style=\"padding: 8px; ; border-bottom: 1px solid #ddd;\">");
        sb.append("            </td>");
        sb.append("            <td style=\"padding: 8px; ; border-bottom: 1px solid #ddd;\">");
        sb.append("            </td>");
        sb.append("            <td style=\"padding: 8px; ; border-bottom: 1px solid #ddd;\">");
        sb.append("            </td>");
        sb.append("        </tr>");
        sb.append("    </tfoot>");
        sb.append("</table>");






















        sb.append("                           <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"" );
        sb.append("                                style=\"margin-bottom:15px; border-collapse:collapse; font-family:Arial, sans-serif;\">" );
        sb.append("                                <tr>" );
        sb.append("                                    <td align=\"center\"" );
        sb.append("                                        style=\"font-size:24px; font-weight:600; color:#333; border-bottom:none; padding:8px; padding-bottom: 0px;\">" );
        sb.append("                                        Relatório de Faturamento ").append(mesConsultaStr).append("</td>" );
        sb.append("                                </tr>" );
        sb.append("                            </table>");

        // Agrupa a lista por cliente
        Map<String, List<FatClientesFran>> pedidosPorCliente = fatClientesFrans.stream()
                .collect(Collectors.groupingBy(FatClientesFran::getCliente));

        //                                  TABELA CLIENTES FATURAMENTO
        sb.append("                            <table width=\"100%\" style=\"border-collapse:collapse; margin:20px auto; font-family:Arial, sans-serif;\">");
        sb.append("                                <thead>");
        sb.append("                                    <tr class=\"linha-destaque\" style=\"background-color:#A62B1F; color:#fff; font-weight:bold;\">");
        sb.append("                                        <th style=\"padding:8px; text-align:left; border-bottom:1px solid #ddd;\">Total por Cliente</th>");
        sb.append("                                        <th style=\"padding:8px; text-align:right; border-bottom:1px solid #ddd;\">Total: ").append(nfValor.format(totalGeral)).append("</th>");
        sb.append("                                    </tr>");
        sb.append("                                </thead>");
        sb.append("                                <tbody>");


        // (A) Monta lista de pares (cliente, totalFaturado) e ordena
        List<Map.Entry<String, BigDecimal>> listaFaturamentoPorCliente =
                pedidosPorCliente.entrySet().stream()
                        .map(e -> {
                            String cliente = e.getKey();
                            BigDecimal totalFaturado = e.getValue().stream()
                                    .map(FatClientesFran::getTotalSicmsSipi)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            // Usamos SimpleEntry no Java 8
                            return new AbstractMap.SimpleEntry<>(cliente, totalFaturado);
                        })
                        .collect(Collectors.toList());

        // Ordenar do maior para o menor
        listaFaturamentoPorCliente.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Agora percorremos a lista já ordenada para montar HTML
        for (Map.Entry<String, BigDecimal> entrada : listaFaturamentoPorCliente) {
            String cliente = entrada.getKey();
            BigDecimal totalPorCliente = entrada.getValue();

            sb.append("                                    <tr>");
            sb.append("                                        <td style=\"border-bottom:1px solid #ddd; padding:8px; text-align:left;\">");
            sb.append("                                            <span>").append(cliente).append("</span>");
            sb.append("                                        </td>");
            sb.append("                                        <td style=\"border-bottom:1px solid #ddd; padding:8px; text-align:right;\">Total: ").append(nfValor.format(totalPorCliente)).append("</td>");
            sb.append("                                    </tr>");
        }

        sb.append("                                </tbody>");
        sb.append("                            </table>");



        //                                  TABELA CLIENTES DEVOLUÇÃO
        if (totalDevolucao != BigDecimal.ZERO){
            sb.append("                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-bottom:15px; border-collapse:collapse; font-family:Arial, sans-serif;\">");
            sb.append("                                <tr>");
            sb.append("                                    <td align=\"center\" style=\"font-size:24px; font-weight:600; color:#333; border-bottom:none; padding:8px; padding-bottom: 0px;\">");
            sb.append("                                    Relatório de Devolução ").append(mesConsultaStr);
            sb.append("                                    </td>");
            sb.append("                                </tr>");
            sb.append("                            </table>");


            sb.append("                            <table width=\"100%\" style=\"border-collapse:collapse; margin:20px auto; font-family:Arial, sans-serif;\">");
            sb.append("                                <thead>");
            sb.append("                                    <tr class=\"linha-destaque\" style=\"background-color:#A62B1F; color:#fff; font-weight:bold;\">");
            sb.append("                                        <th style=\"padding:8px; text-align:left; border-bottom:1px solid #ddd;\">Devolução por Cliente</th>");
            sb.append("                                        <th style=\"padding:8px; text-align:right; border-bottom:1px solid #ddd;\">Total: ").append(nfValor.format(totalDevolucao)).append("</th>");
            sb.append("                                    </tr>");
            sb.append("                                </thead>");
            sb.append("                                <tbody>");

            // (B) Monta lista (cliente, totalDevolvido) e ordena
            List<Map.Entry<String, BigDecimal>> listaDevolucaoPorCliente =
                    pedidosPorCliente.entrySet().stream()
                            .map(e -> {
                                String cliente = e.getKey();
                                BigDecimal totalDevolvido = e.getValue().stream()
                                        .map(FatClientesFran::getValorDevolucao)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                return new AbstractMap.SimpleEntry<>(cliente, totalDevolvido);
                            })
                            .collect(Collectors.toList());

            // Ordenar do maior para o menor
            listaDevolucaoPorCliente.sort(
                    (e1, e2) -> e2.getValue().abs().compareTo(e1.getValue().abs())
            );

            // Percorre lista ordenada para montar HTML
            for (Map.Entry<String, BigDecimal> entrada : listaDevolucaoPorCliente) {
                BigDecimal totalDevolucaoCliente = entrada.getValue();
                if (totalDevolucaoCliente.compareTo(BigDecimal.ZERO) != 0) {
                    String cliente = entrada.getKey();

                    sb.append("                                    <tr>");
                    sb.append("                                        <td style=\"border-bottom:1px solid #ddd; padding:8px; text-align:left;\">");
                    sb.append("                                            <span>").append(cliente).append("</span>");
                    sb.append("                                        </td>");
                    sb.append("                                        <td style=\"border-bottom:1px solid #ddd; padding:8px; text-align:right;\">").append(nfValor.format(totalDevolucaoCliente)).append("</td>");
                    sb.append("                                    </tr>");
                }
            }

        sb.append("                                </tbody>");
        sb.append("                            </table>");
        }



        // Agrupa somente os registros com qtdNegociada > 0
        Map<Integer, List<FatClientesFran>> clientesPorProduto = fatClientesFrans.stream()
                .filter(item -> item.getQtdNegociada() > 0) // aqui filtramos os valores negativos
                .collect(Collectors.groupingBy(FatClientesFran::getCodProduto));


        sb.append("                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-bottom:15px; border-collapse:collapse; font-family:Arial, sans-serif;\">");
        sb.append("                                <tr>");
        sb.append("                                    <td align=\"center\" style=\"font-size:24px; font-weight:600; color:#333; border-bottom:none; padding:8px;\">");
        sb.append("                                    Relatório de Consumo ").append(mesConsultaStr);
        sb.append("                                    </td>");
        sb.append("                                </tr>");
        sb.append("                            </table>");

        // (6) Tabela de Produtos
        sb.append("                            <table width=\"100%\" style=\"margin-top:25px; border-collapse:collapse; font-family:Arial, sans-serif; margin:20px auto;\">");
        sb.append("                                <thead>");
        sb.append("                                    <tr>");
        sb.append("                                        <th style=\"background-color:#A62B1F; color:#fff; padding:8px; text-align:left; border-bottom:1px solid #ddd;\">Produtos").append("</th>");
        sb.append("                                        <th style=\"background-color:#A62B1F; color:#fff; padding:8px; text-align:right; border-bottom:1px solid #ddd;\">").append("</th>");
        sb.append("                                    </tr>");
        sb.append("                                </thead>");
        sb.append("                                <tbody>");

        // (C) Monta lista (codProduto, consumoTotal) e ordena
        List<Map.Entry<Integer, Integer>> listaConsumoPorProduto =
                clientesPorProduto.entrySet().stream()
                        .map(e -> {
                            Integer codProd = e.getKey();
                            List<FatClientesFran> lista = e.getValue();

                            int consumoTotal = lista.stream()
                                    .mapToInt(FatClientesFran::getQtdNegociada)
                                    .sum();

                            return new AbstractMap.SimpleEntry<>(codProd, consumoTotal);
                        })
                        .collect(Collectors.toList());

        // Ordenar do maior para o menor
        listaConsumoPorProduto.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        // Agora percorremos a lista ordenada
        for (Map.Entry<Integer, Integer> entrada : listaConsumoPorProduto) {
            Integer codProd = entrada.getKey();
            int consumoTotalDoProduto = entrada.getValue();

            // Para descrição, pegue do primeiro item do grouping
            List<FatClientesFran> listaProdutos = clientesPorProduto.get(codProd);
            String descrProd = (listaProdutos != null && !listaProdutos.isEmpty())
                    ? listaProdutos.get(0).getDescrprod()
                    : "(Desconhecido)";

            // Corpo da tabela para o produto: cada linha mostra um cliente e seu consumo total
            sb.append("                                    <tr>");
            sb.append("                                        <td style=\"border-bottom:1px solid #ddd; padding:8px; text-align:left;\">").append(codProd).append(" - ").append(descrProd).append("</td>");
            sb.append("                                        <td style=\"border-bottom:1px solid #ddd; padding:8px; text-align:right;\">").append(nfInteiro.format(consumoTotalDoProduto)).append("</td>");
            sb.append("                                    </tr>");
        }

        sb.append("                                </tbody>");
        sb.append("                            </table>");

        sb.append("                        </td>");
        sb.append("                    </tr>");
        sb.append("                </table>");
        sb.append("            </td>");
        sb.append("        </tr>");
        sb.append("    </table>");
        sb.append("</body>");
        sb.append("</html>");



        return sb.toString();
    }
}
