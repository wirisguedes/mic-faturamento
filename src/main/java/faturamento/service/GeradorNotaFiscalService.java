package faturamento.service;


import faturamento.bucket.BucketFile;
import faturamento.bucket.BucketService;
import faturamento.model.Pedido;
import faturamento.publisher.FaturamentoPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeradorNotaFiscalService {

    private final NotaFiscalService notaFiscalService;
    private final BucketService bucketService;
    private final FaturamentoPublisher faturamentoPublisher;

    public void gerar(Pedido pedido) {
        log.info("Gerando nota fiscal para o pedido: {}", pedido.codigo());

        try {

            byte[] byteArray = notaFiscalService.gerarNota(pedido);

            String nomeArquivo = String.format("notafiscal_pedido_%d.pdef", pedido.codigo());

            var file = new BucketFile(
                    nomeArquivo, new ByteArrayInputStream(byteArray), MediaType.APPLICATION_PDF, byteArray.length);
            bucketService.upload(file);

            String url = bucketService.getUrl(nomeArquivo);
            faturamentoPublisher.publicar(pedido, url);
            log.info("Nota fiscal gerada com sucesso, nome do arquivo: {}", nomeArquivo);
        }
        catch (Exception e) {
            log.error("Erro ao gerar nota fiscal para o pedido: {}", pedido.codigo(), e);

        }
    }
}
