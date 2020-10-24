package com.example.demo.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class websocketConfig {
@Bean
public ServerEndpointExporter serverEndpointExporter(){
return new ServerEndpointExporter();
}


}


