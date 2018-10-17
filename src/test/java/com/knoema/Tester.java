package com.knoema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knoema.core.ClientCore;
import com.knoema.core.PropSet;
import com.knoema.meta.*;
import com.knoema.search.SearchResponse;
import com.knoema.search.SearchScope;
import com.knoema.series.TimeSeriesFrame;
import com.knoema.series.TimeSeriesId;
import com.knoema.series.TimeSeriesValues;
import org.apache.http.concurrent.FutureCallback;
import org.junit.Assert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CancellationException;

@SuppressWarnings("SpellCheckingInspection")
public class Tester {
    private static class FutureValueCallback<T> implements FutureCallback<T> {
        private T result;
        private Exception err;

        @Override
        public void completed(T value) {
            result = value;
        }

        @Override
        public void failed(Exception e) {
            err = e;
        }

        @Override
        public void cancelled() {
            err = new CancellationException();
        }

        T getResult() throws Exception {
            if (err != null)
                throw err;
            return result;
        }
    }

    public static int testPivot(String host) throws Exception {

        Client client = new Client(host);

        PivotRequest request;
        PivotRequestItem item;
        request = new PivotRequest();
        request.dataset = "IMFWEO2017Oct";
        request.frequencies = new ArrayList<>();
        request.frequencies.add("A");
        item = new PivotRequestItem();
        item.dimensionId = "Time";
        request.header.add(item);
        item = new PivotRequestItem();
        item.dimensionId = "country";
        item.members.add(1001840); // United Kingdom
        item.members.add(1001830); // United States
        request.stub.add(item);
        item = new PivotRequestItem();
        item.dimensionId = "subject";
        item.members.add(1000040); // GDP
        request.filter.add(item);

        PivotResponse response;
        try {
            response = client.getData(request).get();
            if (response != null) {
                ArrayList<PivotRequest> requests = new ArrayList<>();
                requests.add(request);
                List<PivotResponse> responses = client.getData(requests).get();
                if (responses != null)
                    response = responses.iterator().next();
            }
        }
        finally {
            client.close();
        }

        return response != null ? response.tuples.size() : 0;
    }

    public static int testSearchTimeSeries(String host) throws Exception {
        Client client = new Client(host);
        try {
            SearchResponse response = client.search("UAE oil production", SearchScope.of(SearchScope.Timeseries), 2).get();
            if (response != null)
                return 1;
        }
        finally {
            client.close();
        }
        return 0;
    }

    public static String testDatasetList(String host) throws Exception {

        Client client = new Client(host);
        Iterable<Dataset> datasets;
        Dataset dataset = null;
        try {
            FutureValueCallback<Iterable<Dataset>> callback = new FutureValueCallback<>();
            datasets = client.listDatasets("IMF", null, null).callback(callback).get();
            if (datasets != null) {
                if (datasets != callback.getResult())
                    throw new Exception("Unexpected result");
                for (Dataset it : datasets) {
                    dataset = it;
                    if (dataset != null)
                        break;
                }

            }
        }
        finally {
            client.close();
        }

        return dataset != null ? dataset.name : "";
    }

    public static String testDatasetMetadata(String host) throws Exception {

        Client client = new Client(host);
        Dataset dataset;
        try {
            dataset = client.getDataset("IMFWEO2017Oct").get();
            if (dataset != null) {
                FutureValueCallback<Dataset> callback = new FutureValueCallback<>();
                dataset = client.getDataset("IMFWEO2017Oct").callback(callback).get();
                if (dataset != callback.getResult())
                    throw new Exception("Unexpected result");
            }
        }
        finally {
            client.close();
        }

        return dataset != null ? dataset.name : "";
    }

    public static String testComplexSerialization() throws IOException {
        ObjectMapper objectMapper = ClientCore.getJsonMapper();

        RegularTimeSeriesRawDataResponse response = new RegularTimeSeriesRawDataResponse();
        response.continuationToken = "@token";
        response.data = new ArrayList<>();
        RegularTimeSeriesRawData data = new RegularTimeSeriesRawData();
        data.startDate = new Date();
        data.endDate = new Date();
        data.frequency = "A";
        data.mnemonics = "B";
        data.values = new Double[2];
        data.values[0] = 1.2;
        data.values[1] = 0.1;
        data.dimensions = new ArrayList<>();
        data.timeSeriesAttributes = new AttributeValues();
        data.timeSeriesAttributes.put("attribute", "attribute value");
        response.data.add(data);
        DimensionItem item = new DimensionItem();
        item.dimensionId = "d1";
        item.name = "Dimension Name 1";
        item.metadataFields = new FieldValues();
        item.metadataFields.put("field_str", "field value 1");
        item.metadataFields.put("field_int", 0);
        data.dimensions.add(item);

        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, response);

        String content = sw.toString();

        objectMapper = ClientCore.getJsonMapper();
        StringReader rd = new StringReader(content);

        response = objectMapper.readValue(rd, RegularTimeSeriesRawDataResponse.class);
        if (response.continuationToken != null && response.data != null) {
            data = response.data.iterator().next();
            if (data != null) {
                if (data.dimensions != null && data.dimensions.size() > 0) {
                    item = data.dimensions.get(0);
                    if (item.metadataFields != null && item.metadataFields.containsKey("field_str")) {
                        return item.metadataFields.get("field_str").toString();
                    }
                }
            }
        }

        return null;
    }

    public static String testFlatSeriesSerialization() throws IOException {
        ObjectMapper objectMapper = ClientCore.getJsonMapper();

        FlatTimeSeriesRawDataResponse response = new FlatTimeSeriesRawDataResponse();
        response.continuationToken = "@token";
        response.data = new ArrayList<>();
        FlatTimeSeriesRawData data = new FlatTimeSeriesRawData();
        data.data = new ArrayList<>();
        DataItem dataItem = new DataItem();
        data.data.add(dataItem);

        DataItemTime timeValue = new DataItemTime();
        timeValue.name = "Time";
        timeValue.date = new Date();
        timeValue.frequency = "A";
        dataItem.values.add(timeValue);

        DataItemMeasure measureValue = new DataItemMeasure();
        measureValue.name = "count";
        measureValue.value = 123;
        measureValue.unit = "number";
        dataItem.values.add(measureValue);

        DataItemDetail detailValue = new DataItemDetail();
        detailValue.name = "description";
        detailValue.value = "detailed description";
        dataItem.values.add(detailValue);

        data.dimensions = new ArrayList<>();
        data.timeSeriesAttributes = new AttributeValues();
        data.timeSeriesAttributes.put("attribute", "attribute value");
        response.data.add(data);

        DimensionItem item = new DimensionItem();
        item.dimensionId = "d1";
        item.name = "Dimension Name 1";
        item.metadataFields = new FieldValues();
        item.metadataFields.put("field_str", "field value 1");
        item.metadataFields.put("field_int", 0);
        data.dimensions.add(item);

        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, response);

        String content = sw.toString();

        objectMapper = ClientCore.getJsonMapper();
        StringReader rd = new StringReader(content);

        response = objectMapper.readValue(rd, FlatTimeSeriesRawDataResponse.class);
        if (response.continuationToken != null && response.data != null) {
            data = response.data.iterator().next();
            if (data != null) {
                if (data.data != null && data.data.size() > 0) {
                    dataItem = data.data.get(0);
                    if (dataItem.values.size() > 0) {
                        for (DataItemValue itemValue : dataItem.values) {
                            if (itemValue instanceof DataItemTime)
                                return ((DataItemTime) itemValue).frequency;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static String testCustomSerialization() throws IOException {
        ObjectMapper objectMapper = ClientCore.getJsonMapper();

        PivotResponse response = new PivotResponse();
        PivotDimensionItem item = new PivotDimensionItem();
        item.members.add(1);
        item.members.add("2");
        CalculatedMember calc = new CalculatedMember();
        calc.name = "a";
        item.members.add(calc);
        response.filter.add(item);
        PivotDataTuple tuple = new PivotDataTuple();
        tuple.put("value", 1.23456);
        tuple.put("unit", 100000);
        tuple.put("Time", "2017M1");
        response.tuples.add(tuple);


        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, response);

        String content = sw.toString();

        objectMapper = ClientCore.getJsonMapper();
        StringReader rd = new StringReader(content);

        calc = null;
        response = objectMapper.readValue(rd, PivotResponse.class);
        if (response.tuples != null) {
            tuple = response.tuples.iterator().next();
            if (tuple != null && tuple.size() == 3) {
                if (response.filter.size() > 0) {
                    item = response.filter.get(0);
                    if ((item.members.get(0) instanceof Integer) && (item.members.get(1) instanceof String)) {
                        if (item.members.get(2) instanceof CalculatedMember)
                            calc = (CalculatedMember) item.members.get(2);
                    }
                }
            }
        }

        return calc != null ? calc.name : null;
    }

    public static String testSerialization() throws IOException {
        ObjectMapper objectMapper = ClientCore.getJsonMapper();

        Dataset dataset = new Dataset();
        dataset.dimensions = new Dimension[1];
        dataset.columns = new Column[1];

        Dimension dimension = new Dimension();
        dataset.dimensions[0] = dimension;
        dimension.fields = new DimensionField[1];

        DimensionField field = new DimensionField();
        dimension.fields[0] = field;
        field.name = "A";
        field.type = DimensionFieldType.Double;

        Column column = new Column();
        dataset.columns[0] = column;
        column.name = "B";
        column.type = "t";

        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, dataset);

        String content = sw.toString().replaceAll("\"key\":0,", "");//.replaceAll("\"dimensions\":", "\"Dimensions\":");

        objectMapper = ClientCore.getJsonMapper();
        StringReader rd = new StringReader(content);
        // byte[] jsonData = sw.toString().getBytes(Charset.forName("UTF-8"));
        dataset = objectMapper.readValue(rd, Dataset.class);
        column = dataset.columns[0];
        dimension = dataset.dimensions[0];
        field = dimension.fields[0];

        sw = new StringWriter();
        objectMapper.writeValue(sw, field);

        return (field != null && field.type == DimensionFieldType.Double) ? field.name : (column != null ? "" : null);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public static boolean testSeries(String host) throws Exception {

        if (host != null)
            Knoema.getInstance().setClient(new Client(host));

        TimeSeriesFrame data = Knoema.get("IMFWEO2017Oct", new PropSet()
            .add("frequency", "A")
            .add("country", "612;614")
            .add("subject", new String[] { "NGDPD", "NGDP" })
        );
        Assert.assertNotNull(data);

        data = Knoema.get("IMFWEO2017Oct", "{ \"frequency\": \"A\", \"country\": \"612;614\", \"subject\": [\"NGDPD\", \"NGDP\"] }");
        Assert.assertNotNull(data);

        data = Knoema.get("IMFWEO2017Oct", new Object() {
            public final String Frequency = "A";
            public final String TimeRange = "2015-2016";
            public final String Country = "612;614";
            public final String[] Subject = new String[] { "NGDPD", "NGDP" };
        });
        Assert.assertNotNull(data);

        final String countryName = "Angola";
        final String subjectName = "Gross domestic product, current prices (National currency)";

        TimeSeriesValues series = data.get(new Object() {
            public final String frequency = "A";
            public final String country = countryName;
            public final String subject = subjectName;
        });

        Assert.assertNotNull(series);

        TimeSeriesId seriesId = data.makeId(Frequency.Annual, new Integer[] { 1000030, 1000040 }, new String[] { countryName, subjectName });

        Assert.assertEquals(seriesId, series);
        Assert.assertEquals(seriesId.toString(), series.toString());

        series = data.get(new PropSet()
            .add("frequency", "A")
            .add("country", "614")
            .add("subject", "NGDP")
        );

        Assert.assertNotNull(series);

        Assert.assertEquals(seriesId, series);
        Assert.assertEquals(seriesId.toString(), series.toString());

        return true;
    }
}
