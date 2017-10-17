package org.springframework.vault.authentication;


import org.springframework.util.Assert;


public class KubernetesAuthenticationOptions {

    public static final String DEFAULT_KUBERNETES_AUTHENTICATION_PATH = "kubernetes";

    private final String path;

    private final String role;

    private final KubernetesJwtProvider jwtProvider;


    private KubernetesAuthenticationOptions(String path, String role, KubernetesJwtProvider jwtProvider) {

        this.path = path;
        this.role = role;
        this.jwtProvider = jwtProvider;
    }

    public static KubernetesAuthenticationOptionsBuilder builder() {
        return new KubernetesAuthenticationOptionsBuilder();
    }

    public String getPath() {
        return path;
    }

    public String getRole() {
        return role;
    }

    public KubernetesJwtProvider getJwtProvider() {
        return jwtProvider;
    }

    public static class KubernetesAuthenticationOptionsBuilder {
        private String path = DEFAULT_KUBERNETES_AUTHENTICATION_PATH;

        private String role;

        private KubernetesJwtProvider jwtProvider;


        public KubernetesAuthenticationOptionsBuilder path(String path) {

            Assert.hasText(path, "Path must not be empty");

            this.path = path;
            return this;
        }

        public KubernetesAuthenticationOptionsBuilder role(String role) {

            Assert.hasText(role, "Role must not be null or empty");

            this.role = role;
            return this;
        }

        public KubernetesAuthenticationOptionsBuilder jwtProvider(
                KubernetesJwtProvider jwtProvider) {

            Assert.notNull(jwtProvider, "KubernetesJwtProvider must not be null");

            this.jwtProvider = jwtProvider;
            return this;
        }

        public KubernetesAuthenticationOptions build() {

            Assert.notNull(role, "role must not be null");
            Assert.notNull(path, "Path must not be null");
            Assert.notNull(jwtProvider, "jwtProvider must not be null");

            return new KubernetesAuthenticationOptions(path, role,jwtProvider);
        }
    }
}

