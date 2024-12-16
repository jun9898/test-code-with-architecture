package com.example.demo.common.infrastructure;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.common.service.port.UUidHolder;

@Component
public class SystemUUidHolder implements UUidHolder {
	@Override
	public String random() {
		return UUID.randomUUID().toString();
	}
}
