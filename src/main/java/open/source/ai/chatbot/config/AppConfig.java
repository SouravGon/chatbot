package open.source.ai.chatbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@Configuration
public class AppConfig {

    @Value("${thread.min.pool.size}")
    private int corePoolSize;
    @Value("${thread.max.pool.size}")
    private int maxPoolSize;
    @Value("${thread.queue.size}")
    private int queueCapacity;
    @Value("${thread.keep.alive.time}")
    private int keepAliveTime;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
         return new ThreadPoolTaskExecutor() {
             {
                 setCorePoolSize(corePoolSize);
                 setMaxPoolSize(maxPoolSize);
                 setQueueCapacity(queueCapacity);
                 setKeepAliveSeconds(keepAliveTime);
                 setThreadNamePrefix("ChatBot-");
                 initialize();
             }
         };
    }
}
