package com.knoema;

import com.knoema.core.Utils;
import com.knoema.meta.Dataset;
import com.knoema.meta.PivotRequest;
import com.knoema.meta.RegularTimeSeriesRawDataResponse;
import com.knoema.series.TimeSeriesFrame;
import com.knoema.series.TimeSeriesFrameBuilder;
import com.knoema.series.TimeSeriesRequestBuilder;

import java.util.concurrent.ExecutionException;

public class Knoema {
    private static final Knoema OUR_INSTANCE = new Knoema();

    public static Knoema getInstance() {
        return OUR_INSTANCE;
    }

    private Client client;

    public Client getClient() {
        if (client == null)
            client = new Client("knoema.com");
        return client;
    }

    public void setClient(Client value) {
        client = value;
    }

    private Knoema() {
    }

    public static TimeSeriesFrame get(String datasetId, Object request) throws InterruptedException, ExecutionException {
        return get(getInstance().getClient(), datasetId, request);
    }

    public static TimeSeriesFrame get(Client client, String datasetId, Object request) throws InterruptedException, ExecutionException {

        Dataset dataset = client.getDataset(datasetId).get();

        TimeSeriesRequestBuilder requestBuilder = new TimeSeriesRequestBuilder(client, dataset, Utils.toPropertySet(request));
        PivotRequest dataRequest = requestBuilder.getRequest();

        RegularTimeSeriesRawDataResponse dataBegin = client.getDataBegin(dataRequest).get();
        if (dataBegin == null)
            return null;

        TimeSeriesFrameBuilder frameBuilder = new TimeSeriesFrameBuilder(dataset, requestBuilder.getDimensionIdsMap(), requestBuilder.getDimensionMembersMapping());
        if (dataBegin.data != null)
            frameBuilder.addRange(dataBegin.data);

        for (String continuationToken = dataBegin.continuationToken; !Utils.isEmpty(continuationToken); )
        {
            RegularTimeSeriesRawDataResponse dataNext = client.getDataStreaming(continuationToken).get();
            if (dataNext == null)
                break;
            if (dataNext.data != null)
                frameBuilder.addRange(dataNext.data);
            continuationToken = dataNext.continuationToken;
        }

        return frameBuilder.getResult();
    }
}
