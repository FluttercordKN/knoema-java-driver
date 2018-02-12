package com.knoema.series;

import com.knoema.Frequency;
import com.knoema.meta.RegularTimeSeriesRawData;

import java.util.Date;

public class TimeSeriesValues extends TimeSeriesId {

    public final Date startDate;
    public final Date endDate;
    public final String unit;
    public final Float scale;
    public final String mnemonics;
    public final Series series;

    public TimeSeriesValues(RegularTimeSeriesRawData item, Frequency frequency, AttributesMap<Integer> keys, AttributesMap<Object> attributes, Series series) {

        super(frequency, keys, attributes);

        this.startDate = item.startDate;
        this.endDate = item.endDate;
        this.unit = item.unit;
        this.scale = item.scale;
        this.mnemonics = item.mnemonics;
        this.series = series;
    }
}
