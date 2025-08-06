package actuator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 15:23
 * @des: 与SpringSecurity集成保障安全
 */
@Configuration(proxyBeanMethods = false)
public class MySecurityConfiguration {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.requestMatcher(EndpointRequest.toAnyEndpoint())
//                .authorizeRequests((requests) -> requests.anyRequest().hasRole("ENDPOINT_ADMIN"));
//        http.httpBasic();
//        return http.build();
//    }
}
