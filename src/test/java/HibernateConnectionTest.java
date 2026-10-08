import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HibernateConnectionTest {
    private static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Test
    void testConnectionSuccess(){
        assertNotNull(sessionFactory, "SessionFactory isn't null");
        try(Session session = sessionFactory.openSession()){
            assertNotNull(session, "Session must isn't Null");
            assertTrue(session.isOpen(), "Session connection to MYSQL must a nice must a nice");

            System.out.println("Hibernate Connection and HIKARICP success");
        } catch (Exception e){
            System.out.println("Open session is failed");
            throw e;
        }
    }

    @AfterAll
    static void tearDown(){
        HibernateUtil.shutdown();
    }
}
