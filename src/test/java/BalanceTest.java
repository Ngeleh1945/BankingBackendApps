//import org.example.Balance;
//import org.junit.jupiter.api.Test;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class BalanceTest {
//    @Test
//    void testGetCustomerBalanceSuccess(){
//        BigInteger existingAccount = new BigInteger("1234567891234567");
//
//        String balance = Balance.getCustomerBalance(existingAccount.toString());
//
//        assertNotNull(balance);
//        assertEquals(new BigDecimal("0"), balance);
//    }
//
//    @Test
//    void testGetCustomerBalanceIfAccountNotFound() {
//        String fakeAccount = "9999999999999999";
//
//        String balance = Balance.getCustomerBalance(fakeAccount);
//
//        assertEquals(BigDecimal.ZERO, balance);
//    }
//}
