package com.quitto.server.unit.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.quitto.server.application.controllers.REST.AIProvider.AIProviderController;
import com.quitto.server.application.dto.AIProvider.AIModelRequestDTO;
import com.quitto.server.application.dto.AIProvider.AIProviderRequestDTO;
import com.quitto.server.application.interfaces.IA.AIProviderPort;
import com.quitto.server.domain.enums.IA.AIModelCapability;
import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.exception.IA.ProviderNotFoundException;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.domain.models.IA.AIModelPricing;

import tools.jackson.databind.json.JsonMapper;

/**
 * Testes do {@link AIProviderController} (REST de IA) em modo standalone.
 *
 * <p>Happy paths via MockMvc (conversão de path variable record + serialização
 * Jackson dos DTOs); caminhos de erro via chamada direta ao controller com
 * {@code assertThrows} — em standalone sem {@code @RestControllerAdvice}, a
 * exceção do port não vira um 5xx limpo, então não se deve assertar
 * {@code status().isServerError()} aqui.</p>
 */
@ExtendWith(MockitoExtension.class)
class AIProviderControllerTest {

    @Mock
    private AIProviderPort providerPort;

    @Mock
    private AIProvider provider;

    private MockMvc mockMvc;
    private AIProviderController controller;

    @BeforeEach
    void setUp() {
        controller = new AIProviderController(providerPort);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new JacksonJsonHttpMessageConverter(JsonMapper.builder().build()))
                .build();
    }

    private AIModel model(String id, String name, AIProviderType type, AIModelPricing pricing) {
        return new AIModel(
                id,
                name,
                type,
                EnumSet.of(AIModelCapability.STREAMING, AIModelCapability.TOOLS, AIModelCapability.REASONING),
                pricing);
    }

    @Test
    void shouldReturnAllModels() throws Exception {
        AIModel gpt = model("gpt-4o", "GPT-4o", AIProviderType.OPENAI, AIModelPricing.FREE);
        AIModel mini = model("gpt-4o-mini", "GPT-4o mini", AIProviderType.OPENAI,
                AIModelPricing.of(new BigDecimal("5"), new BigDecimal("15")));
        when(providerPort.getAllModels()).thenReturn(List.of(gpt, mini));

        mockMvc.perform(get("/coffee/api/v1/ai/provider/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("gpt-4o"))
                .andExpect(jsonPath("$[0].name").value("GPT-4o"))
                .andExpect(jsonPath("$[0].provider").value("OPENAI"))
                .andExpect(jsonPath("$[0].capabilities[0]").value("STREAMING"))
                .andExpect(jsonPath("$[0].pricing.free").value(true))
                .andExpect(jsonPath("$[1].id").value("gpt-4o-mini"))
                .andExpect(jsonPath("$[1].pricing.free").value(false));
    }

    @Test
    void shouldReturnModelById() throws Exception {
        AIModel gpt = model("gpt-4o", "GPT-4o", AIProviderType.OPENAI, AIModelPricing.FREE);
        when(providerPort.getModel("gpt-4o")).thenReturn(gpt);

        mockMvc.perform(get("/coffee/api/v1/ai/provider/models/gpt-4o"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("gpt-4o"))
                .andExpect(jsonPath("$.name").value("GPT-4o"))
                .andExpect(jsonPath("$.provider").value("OPENAI"))
                .andExpect(jsonPath("$.capabilities[0]").value("STREAMING"))
                .andExpect(jsonPath("$.pricing.free").value(true));
    }

    @Test
    void shouldReturnAllProviders() throws Exception {
        when(providerPort.getAllProviders()).thenReturn(List.of(provider));
        stubProviderIdentity();

        mockMvc.perform(get("/coffee/api/v1/ai/provider/provaiders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].provider").value("OPENAI"))
                .andExpect(jsonPath("$[0].name").value("OpenAI"))
                .andExpect(jsonPath("$[0].apiBaseUrl").value("https://api.openai.com/v1"))
                .andExpect(jsonPath("$[0].enabled").value(true))
                .andExpect(jsonPath("$[0].configured").value(false))
                .andExpect(jsonPath("$[0].models").isArray());
    }

    @Test
    void shouldReturnProviderByName() throws Exception {
        when(providerPort.findProvider("openai")).thenReturn(provider);
        stubProviderIdentity();

        mockMvc.perform(get("/coffee/api/v1/ai/provider/provaiders/openai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider").value("OPENAI"))
                .andExpect(jsonPath("$.name").value("OpenAI"))
                .andExpect(jsonPath("$.configured").value(false));
    }

    @Test
    void shouldPropagateExceptionWhenModelNotFound() {
        when(providerPort.getModel("missing")).thenThrow(new IllegalArgumentException("Model not found: missing"));

        assertThrows(IllegalArgumentException.class,
                () -> controller.getAIModels(new AIModelRequestDTO("missing")));
    }

    @Test
    void shouldPropagateExceptionWhenProviderNotFound() {
        when(providerPort.findProvider("gemini"))
                .thenThrow(new ProviderNotFoundException("AI provider not found: GEMINI"));

        assertThrows(ProviderNotFoundException.class,
                () -> controller.findProvaider(new AIProviderRequestDTO("gemini")));
    }

    private void stubProviderIdentity() {
        when(provider.getProvider()).thenReturn(AIProviderType.OPENAI);
        when(provider.getName()).thenReturn("OpenAI");
        when(provider.getApiBaseURL()).thenReturn("https://api.openai.com/v1");
        when(provider.isEnabled()).thenReturn(true);
        when(provider.isConfigured()).thenReturn(false);
        when(provider.getModels()).thenReturn(List.of());
    }
}