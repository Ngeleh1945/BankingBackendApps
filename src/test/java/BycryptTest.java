import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import static org.junit.jupiter.api.Assertions.*;

public class BycryptTest {
    @Test
    void compareHash() {
        String password = "Ngeleh1945";

        String oldHash = "$2a$10$oQDw5heCGxmuxRNmdoD.bugAPRd4Htu.nHAiVKmQCRD.KGAMdJhhK";
        String newHash = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("Old Hash : " + oldHash);
        System.out.println("New Hash : " + newHash);

        System.out.println("Old Match : " + BCrypt.checkpw(password, oldHash));
        System.out.println("New Match : " + BCrypt.checkpw(password, newHash));
    }
}
