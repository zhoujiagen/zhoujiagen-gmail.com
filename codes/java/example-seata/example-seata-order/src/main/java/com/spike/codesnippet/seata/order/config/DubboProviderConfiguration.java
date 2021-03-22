package com.spike.codesnippet.seata.order.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo(scanBasePackages = "com.spike.codesnippet.seata.order.service.impl")
@PropertySource("classpath:dubbo.properties")
public class DubboProviderConfiguration {
}