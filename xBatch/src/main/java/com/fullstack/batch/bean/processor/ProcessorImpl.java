package com.fullstack.batch.bean.processor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.fullstack.batch.model.ProcessorReceiveDTO;
import com.fullstack.batch.model.ReaderReturnDTO;

public class ProcessorImpl implements ItemProcessor<List<ReaderReturnDTO>, List<ProcessorReceiveDTO>>
{
    private static final Logger log = LoggerFactory.getLogger(ProcessorImpl.class);

    List<ProcessorReceiveDTO> processorReceiveDTO = new ArrayList<ProcessorReceiveDTO>();
    
    @Override
    public List<ProcessorReceiveDTO> process(final List<ReaderReturnDTO> items) throws Exception
    {
        for (ReaderReturnDTO record : items)
        {
            ProcessorReceiveDTO ProcessorReceiveDTOObj = new ProcessorReceiveDTO();
            
            ProcessorReceiveDTOObj.setColumn1 (record.getColumn1 ());
            ProcessorReceiveDTOObj.setColumn2 (record.getColumn2 ());
            ProcessorReceiveDTOObj.setColumn3 (record.getColumn3 ());
            ProcessorReceiveDTOObj.setColumn4 (record.getColumn4 ());
            ProcessorReceiveDTOObj.setColumn5 (record.getColumn5 ());
            ProcessorReceiveDTOObj.setColumn6 (record.getColumn6 ());
            ProcessorReceiveDTOObj.setColumn7 (record.getColumn7 ());
            ProcessorReceiveDTOObj.setColumn8 (record.getColumn8 ());
            ProcessorReceiveDTOObj.setColumn9 (record.getColumn9 ());
            ProcessorReceiveDTOObj.setColumn10(record.getColumn10());
            ProcessorReceiveDTOObj.setColumn11(record.getColumn11());
            ProcessorReceiveDTOObj.setColumn12(record.getColumn12());
            ProcessorReceiveDTOObj.setColumn13(record.getColumn13());
            ProcessorReceiveDTOObj.setColumn14(record.getColumn14());
            ProcessorReceiveDTOObj.setColumn15(record.getColumn15());
            ProcessorReceiveDTOObj.setColumn16(record.getColumn16());
            ProcessorReceiveDTOObj.setColumn17(record.getColumn17());
            ProcessorReceiveDTOObj.setColumn18(record.getColumn18());
            ProcessorReceiveDTOObj.setColumn19(record.getColumn19());
            ProcessorReceiveDTOObj.setColumn20(record.getColumn20());
            ProcessorReceiveDTOObj.setColumn21(record.getColumn21());
            ProcessorReceiveDTOObj.setColumn22(record.getColumn22());
            ProcessorReceiveDTOObj.setColumn23(record.getColumn23());
            ProcessorReceiveDTOObj.setColumn24(record.getColumn24());
            ProcessorReceiveDTOObj.setColumn25(record.getColumn25());
            ProcessorReceiveDTOObj.setColumn26(record.getColumn26());
            ProcessorReceiveDTOObj.setColumn27(record.getColumn27());
            ProcessorReceiveDTOObj.setColumn28(record.getColumn28());
            ProcessorReceiveDTOObj.setColumn29(record.getColumn29());
            ProcessorReceiveDTOObj.setColumn30(record.getColumn30());
            ProcessorReceiveDTOObj.setColumn31(record.getColumn31());
            ProcessorReceiveDTOObj.setColumn32(record.getColumn32());
            
            //log.info("[ProcessorImpl] process() ProcessorReceiveDTOObj : " + ProcessorReceiveDTOObj.toString());
            
            processorReceiveDTO.add(ProcessorReceiveDTOObj);
        }
        
        return processorReceiveDTO;
    }
}
