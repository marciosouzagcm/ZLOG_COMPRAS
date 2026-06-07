import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        String rawPassword = "sua_senha_secreta"; // <-- ALtere esta linha para a senha desejada!

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("--------------------------------------------------");
        System.out.println("A senha encodificada para '" + rawPassword + "' é:");
        System.out.println(encodedPassword);
        System.out.println("--------------------------------------------------");
    }
}