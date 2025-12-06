package faturamento.mapper;


import faturamento.model.Cliente;
import faturamento.model.ItemPedido;
import faturamento.model.Pedido;
import faturamento.subscriber.representation.DetalheItemPedidoRepresetation;
import faturamento.subscriber.representation.DetalhePedidoRepresentation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public Pedido map(DetalhePedidoRepresentation representation){
        Cliente cliente = new Cliente(
                representation.nome(),
                representation.cpf(),
                representation.logradouro(),
                representation.numero(),
                representation.bairro(),
                representation.email(),
                representation.telefone());

        List<ItemPedido> itens = representation.itens().stream()
                .map(this::mapItem).toList();

        return new Pedido(
                representation.codigo(),
                cliente,
                representation.dataPedido(),
                representation.total(),
                itens
        );
    }


    private ItemPedido mapItem(DetalheItemPedidoRepresetation represetation){
        return new ItemPedido(
                represetation.codigoProduto(),
                represetation.nome(),
               represetation.valorUnitario(),
                represetation.quantidade(),
                represetation.total()
        );
    }
}
