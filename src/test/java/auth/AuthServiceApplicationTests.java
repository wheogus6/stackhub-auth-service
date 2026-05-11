package auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class AuthServiceApplicationTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void test() {
        System.out.println("passwordEncoder.encode(\"123qwe\") = " + passwordEncoder.encode("123qwe"));
    }

}
