package com.quitto.server.infrastructure.IA;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Assinatura AWS Signature Version 4 (SigV4) — usada pelo {@link AwsBedrockProvider}.
 * Implementação mínima sem SDK AWS (PG-003), testável com o vetor oficial da AWS.
 */
final class AwsSigV4 {

    private AwsSigV4() {
    }

    /**
     * Gera os headers de autorização SigV4 para uma requisição.
     *
     * @param accessKey   AWS access key id
     * @param secretKey   AWS secret access key
     * @param host        host canônico (ex.: {@code bedrock.us-east-1.amazonaws.com})
     * @param region      região AWS (ex.: {@code us-east-1})
     * @param service     serviço (ex.: {@code bedrock})
     * @param method      método HTTP (ex.: {@code GET})
     * @param canonicalUri path canônico (ex.: {@code /foundation-models})
     * @param queryString query string canônica (vazia quando não houver)
     * @param payloadHash hash SHA-256 hex do body (vazio → hash de string vazia)
     * @param now         instante atual (injetável para testes)
     * @return mapa com {@code Authorization} e {@code x-amz-date}
     */
    static Map<String, String> authorizationHeaders(
            String accessKey, String secretKey, String host, String region, String service,
            String method, String canonicalUri, String queryString, String payloadHash,
            Instant now) {
        String amzDate = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                .withZone(ZoneOffset.UTC)
                .format(now);
        String dateStamp = amzDate.substring(0, 8);

        String canonicalHeaders = "host:" + host + "\n" + "x-amz-date:" + amzDate + "\n";
        String signedHeaders = "host;x-amz-date";
        String canonicalRequest = method + "\n" + canonicalUri + "\n" + queryString + "\n"
                + canonicalHeaders + "\n" + signedHeaders + "\n" + payloadHash;

        String scope = dateStamp + "/" + region + "/" + service + "/aws4_request";
        String stringToSign = "AWS4-HMAC-SHA256\n" + amzDate + "\n" + scope + "\n"
                + sha256Hex(canonicalRequest);

        byte[] signingKey = hmac(("AWS4" + secretKey).getBytes(StandardCharsets.UTF_8), dateStamp);
        signingKey = hmac(signingKey, region);
        signingKey = hmac(signingKey, service);
        signingKey = hmac(signingKey, "aws4_request");
        String signature = hex(hmac(signingKey, stringToSign));

        String authorization = "AWS4-HMAC-SHA256 Credential=" + accessKey + "/" + scope
                + ", SignedHeaders=" + signedHeaders + ", Signature=" + signature;

        return Map.of("Authorization", authorization, "x-amz-date", amzDate);
    }

    /** Hash SHA-256 em hexadecimal de uma string UTF-8. */
    static String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 não disponível", e);
        }
    }

    private static byte[] hmac(byte[] key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("HmacSHA256 não disponível", e);
        }
    }

    private static String hex(byte[] bytes) {
        return HexFormat.of().formatHex(bytes);
    }
}