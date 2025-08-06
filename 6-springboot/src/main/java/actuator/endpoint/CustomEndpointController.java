package actuator.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 15:27
 * @des: 自定义endpoint
 */
@RestController("custom")
@WebEndpoint(id = "date")
public class CustomEndpointController {

    @ReadOperation
    public ResponseEntity<String> currentDate() {
        return ResponseEntity.ok(LocalDateTime.now().toString());
    }
}
