package com.fullstack.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileWriteDTO
{
	private String stock;
	private String open;
	private String close;
	private String low;
	private String high;
}
