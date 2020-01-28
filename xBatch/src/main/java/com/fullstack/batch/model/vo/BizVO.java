package com.fullstack.batch.model.vo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fullstack.batch.model.FileWriteDTO;

public class BizVO
{
    private Map<String, FileWriteDTO> stockPrices = new HashMap<String, FileWriteDTO>();

    public boolean containsKey(Object key)
    {
        return stockPrices.containsKey(key);
    }

    public FileWriteDTO put(String key, FileWriteDTO value)
    {
        return stockPrices.put(key, value);
    }

    public Collection<FileWriteDTO> values()
    {
        return stockPrices.values();
    }

    public FileWriteDTO get(Object key)
    {
        return stockPrices.get(key);
    }
}
