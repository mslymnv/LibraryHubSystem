package az.company.users.rabbit.listener;

import az.company.users.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DlqListener {
    @RabbitListener(queues = RabbitMQConfig.BORROW_DLQ)
    public void dlqAlert() {
        System.out.println("DLQ ALERT");
    }
}
