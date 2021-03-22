package com.spike.codesnippet.seata.api.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo(scanBasePackages = "com.spike.codesnippet.seata.api.service.ref")
@PropertySource("classpath:dubbo.properties")
@ComponentScan(value = { "com.spike.codesnippet.seata.api.service.ref" })
public class DubboConsumerConfiguration {
}