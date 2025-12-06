package faturamento.subscriber;


import com.fasterxml.jackson.databind.ObjectMapper;
import faturamento.mapper.PedidoMapper;
import faturamento.model.Pedido;
import faturamento.service.GeradorNotaFiscalService;
import faturamento.subscriber.representation.DetalhePedidoRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoPagoSubscriber {

    private final ObjectMapper objectMapper;
    private final GeradorNotaFiscalService geradorNotaFiscalService;
    private final PedidoMapper pedidoMapper;

    @KafkaListener(groupId = "icompras-faturamento",
            topics = "${icompras.config.kafka.topics.pedidos-pagos}")
    public void listen(String json) {

        try {
            log.info("Recebendo evento de pedido pago: {}", json);
            var representation = objectMapper.readValue(json, DetalhePedidoRepresentation.class);
            Pedido pedido = pedidoMapper.map(representation);
            geradorNotaFiscalService.gerar(pedido);

        } catch (Exception e) {
            log.error("Erro na consumação do topico de pedidos pago: {}", e.getMessage(), e);
        }

    }
}
