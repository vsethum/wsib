


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
 
@SpringBootApplication(scanBasePackages={"com.vijays.springboot"})

// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class SpringBootEmployeeRestAPI {
 
    public static void main(String[] args) {
        SpringApplication.run(SpringBootEmployeeRestAPI.class, args);
    }
}