package org.springframework.vault.authentication;

public interface KubernetesJwtProvider {
    String getJwt();
}
