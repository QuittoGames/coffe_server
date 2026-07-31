# =============================================================================
# Dockerfile — Security Runner
# =============================================================================
# Imagem multi-ferramenta para DevSecOps pipeline.
# Contém: k6, OWASP ZAP, Nuclei, ffuf, Trivy, Locust, MHDDoS, e utilitários.
# =============================================================================

FROM ubuntu:24.04 AS base

LABEL maintainer="Quitto <quitto@coffe-server>"
LABEL description="Security runner for coffe_server DevSecOps pipeline"
LABEL environment="LAB_ONLY"

# ── Evitar prompts interativos ──
ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=UTC

# ── Tools Version ──
ENV K6_VERSION=0.57.0
ENV NUCLEI_VERSION=3.3.9
ENV FFUF_VERSION=2.1.0
ENV TRIVY_VERSION=0.58.2
ENV ZAP_VERSION=2.16.0

# ── Timeout de segurança (kill automático) ──
ENV SECURITY_TIMEOUT=600
ENV MHDDOS_MAX_THREADS=50
ENV MHDDOS_MAX_DURATION=120

SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# ══════════════════════════════════════════════════════════════════════════════
# ESTÁGIO 1: Dependências do Sistema
# ══════════════════════════════════════════════════════════════════════════════
FROM base AS deps

RUN apt-get update && apt-get install -y --no-install-recommends \
    # Base
    ca-certificates \
    curl \
    wget \
    gnupg \
    lsb-release \
    software-properties-common \
    # Python
    python3 \
    python3-pip \
    python3-venv \
    # Java (para ZAP)
    openjdk-21-jre-headless \
    # Build & Utilitários
    git \
    jq \
    unzip \
    zip \
    xz-utils \
    # Network
    dnsutils \
    netcat-openbsd \
    iputils-ping \
    net-tools \
    # Monitoring
    procps \
    sysstat \
    htop \
    # Outros
    make \
    gcc \
    g++ \
    pkg-config \
    && rm -rf /var/lib/apt/lists/*

# ══════════════════════════════════════════════════════════════════════════════
# ESTÁGIO 2: Ferramentas de Segurança
# ══════════════════════════════════════════════════════════════════════════════
FROM deps AS tools

WORKDIR /opt

# ── 1. k6 ────────────────────────────────────────────────────────────────────
RUN wget -q "https://github.com/grafana/k6/releases/download/v${K6_VERSION}/k6-v${K6_VERSION}-linux-amd64.tar.gz" \
    && tar -xzf "k6-v${K6_VERSION}-linux-amd64.tar.gz" \
    && mv "k6-v${K6_VERSION}-linux-amd64/k6" /usr/local/bin/k6 \
    && rm -rf "k6-v${K6_VERSION}-linux-amd64"*

# ── 2. Nuclei ────────────────────────────────────────────────────────────────
RUN wget -q "https://github.com/projectdiscovery/nuclei/releases/download/v${NUCLEI_VERSION}/nuclei_${NUCLEI_VERSION}_linux_amd64.zip" \
    && unzip -q "nuclei_${NUCLEI_VERSION}_linux_amd64.zip" -d /tmp/nuclei \
    && mv /tmp/nuclei/nuclei /usr/local/bin/nuclei \
    && rm -rf /tmp/nuclei "nuclei_${NUCLEI_VERSION}_linux_amd64.zip" \
    && nuclei -update-templates 2>/dev/null || true

# ── 3. ffuf ──────────────────────────────────────────────────────────────────
RUN wget -q "https://github.com/ffuf/ffuf/releases/download/v${FFUF_VERSION}/ffuf_${FFUF_VERSION}_linux_amd64.tar.gz" \
    && tar -xzf "ffuf_${FFUF_VERSION}_linux_amd64.tar.gz" \
    && mv ffuf /usr/local/bin/ffuf \
    && rm -f "ffuf_${FFUF_VERSION}_linux_amd64.tar.gz"

# ── 4. Trivy ─────────────────────────────────────────────────────────────────
RUN wget -q "https://github.com/aquasecurity/trivy/releases/download/v${TRIVY_VERSION}/trivy_${TRIVY_VERSION}_Linux-64bit.tar.gz" \
    && tar -xzf "trivy_${TRIVY_VERSION}_Linux-64bit.tar.gz" \
    && mv trivy /usr/local/bin/trivy \
    && rm -f "trivy_${TRIVY_VERSION}_Linux-64bit.tar.gz"

# ── 5. OWASP ZAP ─────────────────────────────────────────────────────────────
RUN wget -q "https://github.com/zaproxy/zaproxy/releases/download/v${ZAP_VERSION}/ZAP_${ZAP_VERSION}_Linux.tar.gz" \
    && tar -xzf "ZAP_${ZAP_VERSION}_Linux.tar.gz" \
    && mv ZAP_${ZAP_VERSION} /opt/zap \
    && ln -s /opt/zap/zap.sh /usr/local/bin/zap.sh \
    && rm -f "ZAP_${ZAP_VERSION}_Linux.tar.gz"

# ── 6. Locust ────────────────────────────────────────────────────────────────
RUN pip3 install --no-cache-dir locust==2.33.2

# ── 7. MHDDoS (modo controlado) ──────────────────────────────────────────────
RUN git clone --depth 1 https://github.com/MatrixTM/MHDDoS.git /opt/MHDDoS \
    && cd /opt/MHDDoS \
    && pip3 install --no-cache-dir -r requirements.txt \
    && rm -rf /opt/MHDDoS/.git

# ── 8. Python Libs adicionais ────────────────────────────────────────────────
RUN pip3 install --no-cache-dir \
    requests \
    pyyaml \
    psutil \
    dnspython \
    icmplib \
    boofuzz==0.4.2 \
    matplotlib \
    jinja2

# ── 9. Limpeza ───────────────────────────────────────────────────────────────
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# ══════════════════════════════════════════════════════════════════════════════
# ESTÁGIO FINAL
# ══════════════════════════════════════════════════════════════════════════════
FROM tools AS final

WORKDIR /workspace

# ── Variáveis de segurança ──
ENV TARGET_URL=http://localhost:8080
ENV LAB_MODE=true
ENV REPORT_DIR=/workspace/logs/security-lab
ENV LOG_DIR=/workspace/logs/security-lab/logs

# ── Entrypoint ──
COPY scripts/ /workspace/scripts/
COPY k6-scripts/ /workspace/k6-scripts/
COPY security-test-context.json /workspace/security-test-context.json

RUN chmod +x /workspace/scripts/*.sh && \
    mkdir -p $REPORT_DIR/logs $REPORT_DIR/metrics $REPORT_DIR/screenshots

ENTRYPOINT ["/workspace/scripts/entrypoint.sh"]
CMD ["--help"]
