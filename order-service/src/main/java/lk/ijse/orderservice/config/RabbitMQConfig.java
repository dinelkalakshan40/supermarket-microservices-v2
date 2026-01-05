package lk.ijse.orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(RabbitMQConstants.EXCHANGE);
    }
    @Bean
    public Queue inventoryQueue(){
        return new Queue(RabbitMQConstants.QUEUE,true);
    }
    @Bean
    public Binding inventoryBinding(Queue queue,DirectExchange exchange){
        return BindingBuilder
                .bind(inventoryQueue())
                .to(exchange)
                .with(RabbitMQConstants.ROUTING_KEY);
    }
}
