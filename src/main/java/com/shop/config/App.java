package com.shop.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class App extends SpringBootServletInitializer {

	public App() {
		super();
		setRegisterErrorPageFilter(false); // <- this one
	}
}
