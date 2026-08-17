package com.quitto.server.domain.models.IA;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Preço de um modelo de IA (value object imutável).
 *
 * <p>Valores em USD por token/milhão de tokens, conforme o provedor. O campo
 * {@code free} sinaliza modelos gratuitos; quando {@code true}, os valores de
 * preço são normalizados para zero na construção.</p>
 */
public record AIModelPricing(
        BigDecimal inputPricing,
        BigDecimal outputPricing,
        BigDecimal cachedInputPricing,
        boolean free
) {

    /** Final Value for Free Models */
    public static final AIModelPricing FREE = new AIModelPricing(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, true);

    public AIModelPricing {
        Objects.requireNonNull(inputPricing, "inputPricing must not be null");
        Objects.requireNonNull(outputPricing, "outputPricing must not be null");
        Objects.requireNonNull(cachedInputPricing, "cachedInputPricing must not be null");

        if (free) {
            inputPricing = BigDecimal.ZERO;
            outputPricing = BigDecimal.ZERO;
            cachedInputPricing = BigDecimal.ZERO;
        }
    }

    /** Cria um pricing pago com preço de entrada e saída (cache = zero). */
    public static AIModelPricing of(BigDecimal input, BigDecimal output) {
        return new AIModelPricing(input, output, BigDecimal.ZERO,false);
    }
}
