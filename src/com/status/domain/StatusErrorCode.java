package com.status.domain;

public enum StatusErrorCode implements ErrorCode{
	invalid_email,
	invalid_status;

	@Override
	public String getCode() {
		return this.name();
	}
}
