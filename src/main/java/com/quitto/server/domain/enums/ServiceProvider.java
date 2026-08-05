package com.quitto.server.domain.enums;

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
