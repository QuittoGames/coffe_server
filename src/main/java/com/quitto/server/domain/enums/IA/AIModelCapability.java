package com.quitto.server.domain.enums.IA;

/**
 * Capacidades de um modelo de IA.
 *
 * <p>Substitui os três booleanos do {@code AIModel} legado
 * ({@code stream}/{@code tools}/{@code reasoning}) por um conjunto imutável
 * de capacidades, tornando o contrato extensível (novas capacidades não
 * alteram a assinatura do modelo).</p>
 */
public enum AIModelCapability {

    STREAMING,
    TOOLS,
    REASONING
}