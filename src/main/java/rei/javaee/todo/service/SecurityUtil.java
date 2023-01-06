package rei.javaee.todo.service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class SecurityUtil {

    @Inject
    private QueryService queryService;
    public static final String HASHED_PASSWORD_KEY = "hashedPassword";
    public static final String SALT_KEY = "salt";
    public static final String BEARER = "Bearer";
    private SecretKey securityKey;

    // because it is application scoped, it will generate the key only once
    @PostConstruct
    private void init() {
        securityKey = generateKey();
    }

    public Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public boolean passwordsMatch(String dbStoredHashedPassword, String saltText, String clearTextPassword) {
        ByteSource salt = ByteSource.Util.bytes(Hex.decode(saltText));
        String hashedPassword = hashAndSaltPassword(clearTextPassword, salt);
        return hashedPassword.equals(dbStoredHashedPassword);
    }

    public Map<String, String> hashPassword(String clearTextPassword) {
        ByteSource salt = getSalt();
        Map<String, String> credentialsMap = new HashMap<>();
        credentialsMap.put(HASHED_PASSWORD_KEY, hashAndSaltPassword(clearTextPassword, salt));
        credentialsMap.put(SALT_KEY, salt.toHex());
        return credentialsMap;
    }

    private String hashAndSaltPassword(String clearTextPassword, ByteSource salt) {
        return new Sha512Hash(clearTextPassword, salt, 2000000).toHex();
    }

    private ByteSource getSalt() {
        return new SecureRandomNumberGenerator().nextBytes();
    }

    public boolean authenticateUser(String email, String password) {
        return queryService.authenticateUser(email, password);
    }

    private SecretKey generateKey() {
        return MacProvider.generateKey(SignatureAlgorithm.HS512);
    }

    public SecretKey getSecurityKey() {
        return securityKey;
    }
}
