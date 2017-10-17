package org.springframework.vault.authentication;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class TokenFile implements KubernetesJwtProvider {

    private final String path;

    public TokenFile(String path) {
        Assert.hasText(path, "Path must not be empty");
        this.path = path;
    }

    @Override
    public String getJwt() {
        try {
            //final File resource = ResourceUtils.getFile(path);
            final Resource resource = new FileSystemResource(path);
            return StreamUtils.copyToString(resource.getInputStream(), Charset.forName("US-ASCII"));
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }
}
