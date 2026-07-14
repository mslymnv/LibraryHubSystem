package az.company.users.rabbit.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static az.company.users.config.RabbitMQConfig.BORROW_CREATED_DLQ;

@Component
public class DlqListener {
    @RabbitListener(queues = BORROW_CREATED_DLQ)
    public void dlqAlert() {
        System.out.println("DLQ ALERT");
    }
}
