package faturamento.subscriber.representation;

import java.math.BigDecimal;

public record DetalheItemPedidoRepresetation(
        Long codigoProduto,
        String nome,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal total
) {



}
