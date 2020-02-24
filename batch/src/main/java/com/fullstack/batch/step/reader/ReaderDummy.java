package com.fullstack.batch.step.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import com.fullstack.batch.model.ReaderReturnDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
 
public class ReaderDummy implements ItemReader<ReaderReturnDTO>
{
	private static final Logger log = LoggerFactory.getLogger(ReaderDummy.class);
	
	private static int readCount = 0;
	
	public ReaderDummy() {};
	
	public ReaderReturnDTO getResource(int readCount)
	{
		log.info("[ReaderImpl] getResource() readCount : " + readCount);
		
		ReaderReturnDTO readerReturnDTO = new ReaderReturnDTO();
		
		readerReturnDTO.setColumn1(String.valueOf(readCount));
		readerReturnDTO.setColumn2("1");
		readerReturnDTO.setColumn3("2");
		readerReturnDTO.setColumn4("2");
		readerReturnDTO.setColumn5("4");
		
		return readerReturnDTO;
	}
	
	@Override
	public ReaderReturnDTO read()
	{
		ReaderReturnDTO readerReturnDTO = null;
		
		if (readCount < 10)
		{
			readerReturnDTO = this.getResource(readCount);
			
			readCount++;
		}
		
		return readerReturnDTO;
	}
}
