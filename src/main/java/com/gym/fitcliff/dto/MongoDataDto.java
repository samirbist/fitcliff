package com.gym.fitcliff.dto;

import java.io.InputStream;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoDataDto {

	private String fileName;

	private InputStream stream;
}