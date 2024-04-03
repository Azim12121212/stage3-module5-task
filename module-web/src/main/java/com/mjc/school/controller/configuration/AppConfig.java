package com.mjc.school.controller.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@ComponentScan(basePackages = {"com.mjc.school.*"})
@EnableAspectJAutoProxy
@EntityScan(basePackages = {"com.mjc.school.repository.model"})
public class AppConfig {
}