package com.quitto.server.unit.application;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.application.controllers.REST.AIProvaider.AIProvaiderController;
import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.OperationKey.OperationKeyManager;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.infrastructure.Adapters.out.api.AIProvaider.AIProvaiderAdpiter;

/**
 * Testes unitários do {@link AIProvaiderController}.
 *
 * <p>Usa {@link MockitoExtension} + {@code standaloneSetup} (sem contexto do
 * Spring), mockando o {@link AIProvaiderAdpiter}. Os casos cobrem os 4
 * endpoints REST, o mapeamento para DTOs e a propagação de erros vindos do
 * adapter (modelo não encontrado / enum de provedor inválido).</p>
 */
@ExtendWith(MockitoExtension.class)
class AIProvaiderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AIProvaiderAdpiter provaiderAdpiter;

    @Mock
    private ObjectProvider<OperationKeyManager> operationKeyManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new AIProvaiderController(provaiderAdpiter, operationKeyManager)
        ).setControllerAdvice(new IllegalArgumentExceptionAdvice())
        .build();
    }

    @RestControllerAdvice
    static class IllegalArgumentExceptionAdvice {
        @ExceptionHandler(IllegalArgumentException.class)
        ResponseEntity<Void> handleIllegalArgumentException() {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ──────────────────────────────────────────────────────────────────
    // GET /coffee/api/v1/ai/provider/models/{model}
    // ──────────────────────────────────────────────────────────────────

    @Test
    void getAIModels_byId_returnsModelAsDTO() throws Exception {
        AIModel model = new AIModel("gpt-4o", "GPT-4o", ServiceProvider.OPENAI, true, true, false);
        when(provaiderAdpiter.getModel("gpt-4o")).thenReturn(model);

        mockMvc.perform(get("/coffee/api/v1/ai/provider/models/gpt-4o")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("gpt-4o"))
            .andExpect(jsonPath("$.name").value("GPT-4o"))
            .andExpect(jsonPath("$.provider").value("OPENAI"))
            .andExpect(jsonPath("$.stream").value(true))
            .andExpect(jsonPath("$.tools").value(true))
            .andExpect(jsonPath("$.reasoning").value(false));

        verify(provaiderAdpiter).getModel("gpt-4o");
    }

    @Test
    void getAIModels_unknownModel_propagatesIllegalArgumentException() throws Exception {
        when(provaiderAdpiter.getModel("not-found"))
            .thenThrow(new IllegalArgumentException("No AI model found for: not-found"));

        // Standalone setup sem ControllerAdvice → IllegalArgumentException vira 500.
        mockMvc.perform(get("/coffee/api/v1/ai/provider/models/not-found")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(provaiderAdpiter).getModel("not-found");
    }

    // ──────────────────────────────────────────────────────────────────
    // GET /coffee/api/v1/ai/provider/models
    // ──────────────────────────────────────────────────────────────────

    @Test
    void getAllAIModels_returnsListOfDTOs() throws Exception {
        AIModel m1 = new AIModel("claude-3-5-sonnet", "Claude 3.5 Sonnet", ServiceProvider.ANTHROPIC, true, true, true);
        AIModel m2 = new AIModel("llama-3.1", "Llama 3.1", ServiceProvider.OLLAMA, true, false, false);
        when(provaiderAdpiter.getAllModels()).thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/coffee/api/v1/ai/provider/models")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("claude-3-5-sonnet"))
            .andExpect(jsonPath("$[0].provider").value("ANTHROPIC"))
            .andExpect(jsonPath("$[1].id").value("llama-3.1"))
            .andExpect(jsonPath("$[1].provider").value("OLLAMA"))
            .andExpect(jsonPath("$[1].tools").value(false));

        verify(provaiderAdpiter).getAllModels();
    }

    @Test
    void getAllAIModels_emptyList_returns200WithEmptyArray() throws Exception {
        when(provaiderAdpiter.getAllModels()).thenReturn(List.of());

        mockMvc.perform(get("/coffee/api/v1/ai/provider/models")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        verify(provaiderAdpiter).getAllModels();
    }

    // ──────────────────────────────────────────────────────────────────
    // GET /coffee/api/v1/ai/provider/provaiders
    // ──────────────────────────────────────────────────────────────────

    @Test
    void getAllProvaiders_returnsListOfProviderDTOs() throws Exception {
        AIProvider openAI = mockProvider(ServiceProvider.OPENAI, "OpenAI",
            "https://api.openai.com/v1", true, List.of(
                new AIModel("gpt-4o", "GPT-4o", ServiceProvider.OPENAI, true, true, false)
            ));
        AIProvider ollama = mockProvider(ServiceProvider.OLLAMA, "Ollama",
            "http://localhost:11434", true, List.of());

        when(provaiderAdpiter.getAllProviders()).thenReturn(List.of(openAI, ollama));

        mockMvc.perform(get("/coffee/api/v1/ai/provider/provaiders")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].provider").value("OPENAI"))
            .andExpect(jsonPath("$[0].name").value("OpenAI"))
            .andExpect(jsonPath("$[0].apiBaseUrl").value("https://api.openai.com/v1"))
            .andExpect(jsonPath("$[0].enabled").value(true))
            .andExpect(jsonPath("$[0].models[0].id").value("gpt-4o"))
            .andExpect(jsonPath("$[1].provider").value("OLLAMA"))
            .andExpect(jsonPath("$[1].models").isEmpty());

        verify(provaiderAdpiter).getAllProviders();
    }

    @Test
    void getAllProvaiders_empty_returns200WithEmptyArray() throws Exception {
        when(provaiderAdpiter.getAllProviders()).thenReturn(List.of());

        mockMvc.perform(get("/coffee/api/v1/ai/provider/provaiders")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        verify(provaiderAdpiter).getAllProviders();
    }

    // ──────────────────────────────────────────────────────────────────
    // GET /coffee/api/v1/ai/provider/provaiders/{provaider}
    // ──────────────────────────────────────────────────────────────────

    @Test
    void findProvaider_byEnumName_returnsProviderDTO() throws Exception {
        AIProvider anthropic = mockProvider(ServiceProvider.ANTHROPIC, "Anthropic",
            "https://api.anthropic.com/v1", true, List.of(
                new AIModel("claude-3-5-sonnet", "Claude 3.5 Sonnet", ServiceProvider.ANTHROPIC, true, true, true)
            ));
        when(provaiderAdpiter.findProvider("ANTHROPIC")).thenReturn(anthropic);

        mockMvc.perform(get("/coffee/api/v1/ai/provider/provaiders/ANTHROPIC")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.provider").value("ANTHROPIC"))
            .andExpect(jsonPath("$.name").value("Anthropic"))
            .andExpect(jsonPath("$.enabled").value(true))
            .andExpect(jsonPath("$.models[0].id").value("claude-3-5-sonnet"));

        verify(provaiderAdpiter).findProvider("ANTHROPIC");
    }

    @Test
    void findProvaider_caseInsensitive_returnsMatch() throws Exception {
        AIProvider openAI = mockProvider(ServiceProvider.OPENAI, "OpenAI",
            "https://api.openai.com/v1", true, List.of());
        when(provaiderAdpiter.findProvider("openai")).thenReturn(openAI);

        mockMvc.perform(get("/coffee/api/v1/ai/provider/provaiders/openai")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.provider").value("OPENAI"));

        verify(provaiderAdpiter).findProvider("openai");
    }

    @Test
    void findProvaider_unknownProvider_propagatesIllegalArgumentException() throws Exception {
        when(provaiderAdpiter.findProvider("not-a-real-provider"))
            .thenThrow(new IllegalArgumentException(
                "No enum constant com.quitto.server.domain.enums.ServiceProvider.NOT-A-REAL-PROVIDER"));

        mockMvc.perform(get("/coffee/api/v1/ai/provider/provaiders/not-a-real-provider")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(provaiderAdpiter).findProvider("not-a-real-provider");
    }

    // ──────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────

    private AIProvider mockProvider(ServiceProvider provider, String name,
                                    String apiBaseUrl, boolean enabled,
                                    List<AIModel> models) {
        AIProvider mock = mock(AIProvider.class);
        when(mock.getProvider()).thenReturn(provider);
        when(mock.getName()).thenReturn(name);
        when(mock.getApiBaseURL()).thenReturn(apiBaseUrl);
        when(mock.isEnabled()).thenReturn(enabled);
        when(mock.getModels()).thenReturn(models);
        return mock;
    }
}
