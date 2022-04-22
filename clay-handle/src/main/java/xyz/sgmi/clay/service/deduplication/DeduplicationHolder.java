package xyz.sgmi.clay.service.deduplication;

import org.springframework.stereotype.Service;
import xyz.sgmi.clay.service.deduplication.build.Builder;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Service
public class DeduplicationHolder {

    private Map<Integer, Builder> builderHolder = new HashMap<>(4);
    private Map<Integer, DeduplicationService> serviceHolder = new HashMap<>(4);

    public Builder selectBuilder(Integer key) {
        return builderHolder.get(key);
    }

    public DeduplicationService selectService(Integer key) {
        return serviceHolder.get(key);
    }

    public void putBuilder(Integer key, Builder builder) {
        builderHolder.put(key, builder);
    }

    public void putService(Integer key, DeduplicationService service) {
        serviceHolder.put(key, service);
    }
}

