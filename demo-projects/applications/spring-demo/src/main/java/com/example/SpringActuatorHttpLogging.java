package com.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
@Configuration
public class SpringActuatorHttpLogging {
	@Bean
	public HttpTraceRepository httpTraceRepository() {
		return new InMemoryHttpTraceRepository() {
			ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
			Logger logger = LoggerFactory.getLogger(InMemoryHttpTraceRepository.class);
			@Override
			public void add(HttpTrace trace) {
				try {
					logger.info(objectMapper.writeValueAsString(trace));
				} catch (JsonProcessingException e) {
					logger.error(e.getMessage(), e);
				}
				super.add(trace);
				super.setCapacity(1);
			}
		};
	}
}
