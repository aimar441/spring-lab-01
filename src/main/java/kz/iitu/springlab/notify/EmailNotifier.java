package kz.iitu.springlab.notify;

import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("email")
@Order(2)
public class EmailNotifier implements Notifier {

    @Override
    public String send(String message) {
        return "email: " + message;
    }

    @Override
    public String channel() {
        return "email";
    }
}