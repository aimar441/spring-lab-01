package kz.iitu.springlab.notify;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Fallback;

@Component("noop")
@Fallback
@Order(99)
public class NoopNotifier implements Notifier {

    @Override
    public String send(String message) {
        return "noop: " + message;
    }

    @Override
    public String channel() {
        return "noop";
    }
}
