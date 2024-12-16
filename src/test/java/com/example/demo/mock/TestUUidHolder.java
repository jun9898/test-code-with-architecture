package com.example.demo.mock;

import com.example.demo.common.service.port.UUidHolder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestUUidHolder implements UUidHolder {

	private final String uuid;

	@Override
	public String random() {
		return uuid;
	}
}
