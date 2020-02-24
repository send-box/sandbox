package com.fullstack.batch.model.vo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fullstack.batch.model.FileWriteDTO;
import com.fullstack.batch.model.entity.MeasureInfoReal;

public class MeasureInfoVO
{
    private Map<String, MeasureInfoReal> measureInfoReal = new HashMap<String, MeasureInfoReal>();

    public boolean containsKey(Object key)
    {
        return measureInfoReal.containsKey(key);
    }

    public MeasureInfoReal put(String key, MeasureInfoReal value)
    {
        return measureInfoReal.put(key, value);
    }

    public Collection<MeasureInfoReal> values()
    {
        return measureInfoReal.values();
    }

    public MeasureInfoReal get(Object key)
    {
        return measureInfoReal.get(key);
    }
}
