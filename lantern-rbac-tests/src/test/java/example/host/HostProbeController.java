package example.host;

import com.lamp.lantern.plugins.api.annotation.UserInjection;
import java.util.Map;
import java.util.concurrent.Callable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HostProbeController {

    public static final String CLUSTER_ROUTE = "/user/cluster/queryClusterByOrganizationIdAndType";
    public static final String ORDER_ROUTE = "/rbac/orders/{id}";

    @PostMapping(CLUSTER_ROUTE)
    public ClusterBody clusters(@RequestBody ClusterBody body) {
        return body;
    }

    @GetMapping(ORDER_ROUTE)
    public String order() {
        return "ok";
    }

    @PostMapping("/public/annotated")
    public ClusterBody publicBody(@RequestBody ClusterBody body) {
        return body;
    }

    @GetMapping("/rbac/async")
    public Callable<String> async() {
        return () -> "async-ok";
    }

    @GetMapping("/public/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/public/echo")
    public Map<String, String> echo(@RequestBody Map<String, String> body) {
        return body;
    }

    public static class ClusterBody {
        public String message;
        @UserInjection("uiId")
        public Long userId;
    }
}
