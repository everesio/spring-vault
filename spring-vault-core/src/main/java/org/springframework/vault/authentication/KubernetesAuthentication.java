package org.springframework.vault.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.vault.VaultException;
import org.springframework.vault.client.VaultResponses;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultToken;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;

import java.util.HashMap;
import java.util.Map;

public class KubernetesAuthentication implements ClientAuthentication {

    private static final Log logger = LogFactory.getLog(KubernetesAuthentication.class);

    private final KubernetesAuthenticationOptions options;

    private final RestOperations restOperations;

    public KubernetesAuthentication(KubernetesAuthenticationOptions options,
                               RestOperations restOperations) {

        Assert.notNull(options, "KubernetesAuthenticationOptions must not be null");
        Assert.notNull(restOperations, "RestOperations must not be null");

        this.options = options;
        this.restOperations = restOperations;
    }

    @Override
    public VaultToken login() throws VaultException {
        return createTokenUsingKubernetes();
    }


    private VaultToken createTokenUsingKubernetes() {

        Map<String, String> login = getKubernetesLogin(options.getRole(), options
                .getJwtProvider().getJwt());

        try {
            VaultResponse response = restOperations.postForObject("auth/{mount}/login",
                    login, VaultResponse.class, options.getPath());

            Assert.state(response != null && response.getAuth() != null,
                    "Auth field must not be null");

            logger.debug("Login successful using Kubernetes authentication");

            return LoginTokenUtil.from(response.getAuth());
        }
        catch (HttpStatusCodeException e) {
            throw new VaultException(String.format("Cannot login using kubernetes: %s",
                    VaultResponses.getError(e.getResponseBodyAsString())));
        }
    }

    private static Map<String, String> getKubernetesLogin(String role, String jwt) {

        Map<String, String> login = new HashMap<String, String>();

        login.put("jwt", jwt);
        login.put("role", role);

        return login;
    }

}
