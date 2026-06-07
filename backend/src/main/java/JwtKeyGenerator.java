import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class JwtKeyGenerator {
    public static void main(String[] args) throws Exception {
        // Para HS256, use 256 bits (32 bytes). Para HS512, use 512 bits (64 bytes).
        int keyLengthBits = 256; 

        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(keyLengthBits);
        SecretKey secretKey = keyGen.generateKey();
        
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        
        System.out.println("--- Chave JWT Base64 Segura Gerada ---");
        System.out.println("Por favor, adicione esta chave ao seu 'application.properties' ou 'application.yml':");
        System.out.println("jwt.secret.key=" + encodedKey);
        System.out.println("----------------------------------------");
    }
}