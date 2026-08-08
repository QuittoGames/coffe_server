package com.quitto.server.domain.enums;

/**
 * Catálogo de <strong>provedores de IA</strong> do ecossistema Coffee.
 *
 * <p>Cada constante representa um serviço de modelos (ou uma família de
 * serviços) e possui um {@code AIProvider} concreto registrado na
 * infraestrutura ({@code infrastructure/IA/*Provider.java}). As constantes são
 * agrupadas por família:</p>
 *
 * <ul>
 *   <li><strong>OpenAI Compatible</strong> — OPENAI, OPENROUTER, NVIDIA, GROQ,
 *       TOGETHER_AI, FIREWORKS_AI, SAMBANOVA, DEEPINFRA, HYPERBOLIC,
 *       NOVITA_AI, OPENAI_COMPATIBLE;</li>
 *   <li><strong>Big Tech</strong> — GOOGLE_AI_STUDIO, GOOGLE_VERTEX_AI,
 *       AZURE_OPENAI, AWS_BEDROCK, IBM_WATSONX, OCI_GENERATIVE_AI;</li>
 *   <li><strong>Provedores diretos</strong> — ANTHROPIC, MISTRAL_AI, COHERE,
 *       XAI, DASHSCOPE, MOONSHOT_AI, ZHIPU_AI, QIANFAN;</li>
 *   <li><strong>Self-hosted</strong> — OLLAMA, VLLM, LM_STUDIO, LLAMACPP,
 *       TEXT_GENERATION_WEBUI;</li>
 *   <li><strong>Outros</strong> — HUGGING_FACE, REPLICATE, PERPLEXITY,
 *       CEREBRAS, LEPTON_AI, CLOUDFLARE_AI, CUSTOM.</li>
 * </ul>
 *
 * <p>É usado como identidade em {@code AIProvider.getProvider()}, como chave
 * de indexação do {@code AIRegistry} e em {@code AIModel.provider}.</p>
 *
 * @see com.quitto.server.domain.interfaces.IA.AIProvider
 * @see com.quitto.server.domain.models.IA.AIModel
 */
public enum ServiceProvider {

    // OpenAI Compatible
    OPENAI,
    OPENROUTER,
    NVIDIA,
    GROQ,
    TOGETHER_AI,
    FIREWORKS_AI,
    SAMBANOVA,
    DEEPINFRA,
    HYPERBOLIC,
    NOVITA_AI,
    OPENAI_COMPATIBLE,

    // Big Tech
    GOOGLE_AI_STUDIO,
    GOOGLE_VERTEX_AI,
    AZURE_OPENAI,
    AWS_BEDROCK,
    IBM_WATSONX,
    OCI_GENERATIVE_AI,

    // Anthropic
    ANTHROPIC,

    // Mistral
    MISTRAL_AI,

    // Cohere
    COHERE,

    // xAI
    XAI,

    // Alibaba
    DASHSCOPE,

    // Moonshot
    MOONSHOT_AI,

    // Zhipu
    ZHIPU_AI,

    // Baidu
    QIANFAN,

    // Self-host
    OLLAMA,
    VLLM,
    LM_STUDIO,
    LLAMACPP,
    TEXT_GENERATION_WEBUI,

    // Outros
    HUGGING_FACE,
    REPLICATE,
    PERPLEXITY,
    CEREBRAS,
    LEPTON_AI,
    CLOUDFLARE_AI,
    CUSTOM
}
