package com.redpig.common.dao;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDSessionIdGenerator implements UIdGenerator {
	public synchronized String genId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
