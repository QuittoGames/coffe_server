package com.quitto.server.infrastructure.IA;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa o {@link AwsSigV4} (PG-003) usando o vetor oficial da AWS para
 * Signature Version 4 (GET IAM / Action=ListUsers).
 */
class AwsSigV4Test {

    private static final String ACCESS_KEY = "AKIDEXAMPLE";
    private static final String SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

    @Test
    void sha256Hex_emptyString_matchesKnownDigest() {
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                AwsSigV4.sha256Hex(""));
    }

    @Test
    void sha256Hex_abc_matchesKnownDigest() {
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                AwsSigV4.sha256Hex("abc"));
    }

    @Test
    void authorizationHeaders_officialAwsVector_producesKnownSignature() {
        Map<String, String> headers = AwsSigV4.authorizationHeaders(
                ACCESS_KEY,
                SECRET_KEY,
                "iam.amazonaws.com",
                "us-east-1",
                "iam",
                "GET",
                "/",
                "Action=ListUsers&Version=2010-05-08",
                AwsSigV4.sha256Hex(""),
                Instant.parse("2015-08-30T12:36:00Z"));

        assertEquals("20150830T123600Z", headers.get("x-amz-date"));

        String authorization = headers.get("Authorization");
        assertNotNull(authorization);
        assertTrue(authorization.startsWith(
                "AWS4-HMAC-SHA256 Credential=AKIDEXAMPLE/20150830/us-east-1/iam/aws4_request"));
        assertTrue(authorization.endsWith(", SignedHeaders=host;x-amz-date, "
                + "Signature=dad145687cde2dbf9684236b386711320b5997e4d31b3b5efe762858f46cc755"));
    }

    @Test
    void authorizationHeaders_containsAuthorizationAndAmzDateKeys() {
        Map<String, String> headers = AwsSigV4.authorizationHeaders(
                ACCESS_KEY, SECRET_KEY, "iam.amazonaws.com", "us-east-1", "iam",
                "GET", "/", "Action=ListUsers&Version=2010-05-08",
                AwsSigV4.sha256Hex(""), Instant.parse("2015-08-30T12:36:00Z"));

        assertTrue(headers.containsKey("Authorization"));
        assertTrue(headers.containsKey("x-amz-date"));
    }

    @Test
    void authorizationHeaders_differentPayload_changesSignature() {
        Map<String, String> emptyPayload = AwsSigV4.authorizationHeaders(
                ACCESS_KEY, SECRET_KEY, "iam.amazonaws.com", "us-east-1", "iam",
                "GET", "/", "Action=ListUsers&Version=2010-05-08",
                AwsSigV4.sha256Hex(""), Instant.parse("2015-08-30T12:36:00Z"));

        Map<String, String> differentPayload = AwsSigV4.authorizationHeaders(
                ACCESS_KEY, SECRET_KEY, "iam.amazonaws.com", "us-east-1", "iam",
                "GET", "/", "Action=ListUsers&Version=2010-05-08",
                AwsSigV4.sha256Hex("changed"), Instant.parse("2015-08-30T12:36:00Z"));

        assertNotEquals(emptyPayload.get("Authorization"),
                differentPayload.get("Authorization"));
    }
}