package com.fullstack.batch.model.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fullstack.batch.model.FileWriteDTO;
import com.fullstack.batch.model.ProcessorReceiveDTO;
import com.fullstack.batch.model.entity.MeasureInfoReal;

public class MeasureInfoRealListVO
{
    private List<MeasureInfoReal> measureInfoRealList = new ArrayList<MeasureInfoReal>();
    
    public void add(MeasureInfoReal measureInfoReal)
    {
        measureInfoRealList.add(measureInfoReal);
    }

    public List<MeasureInfoReal> get()
    {
        return measureInfoRealList;
    }
    
    public void clear()
    {
        measureInfoRealList.clear();
    }
    
    public int size()
    {
        return measureInfoRealList.size();
    }
}