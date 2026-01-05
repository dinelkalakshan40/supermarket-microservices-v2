package lk.ijse.orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate uses JSON converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
