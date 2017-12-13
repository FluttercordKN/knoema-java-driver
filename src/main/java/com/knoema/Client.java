package com.knoema;

import com.knoema.core.ClientCore;
import com.knoema.core.RequestExecutor;
import com.knoema.core.Utils;
import com.knoema.meta.*;
import com.knoema.search.ConfigResponse;
import com.knoema.search.SearchResponse;
import com.knoema.search.SearchScope;
import com.knoema.upload.DatasetUpload;
import com.knoema.upload.UploadResult;
import com.knoema.upload.VerifyDatasetResult;
import com.knoema.upload.VerifyResult;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("WeakerAccess")
public class Client extends ClientCore {

    private static class SearchClient extends ClientCore {
        final private String communityId;
        final private String accessToken;
        final private String baseHost;

        SearchClient(ClientCore source, ConfigResponse response) {
            super(response.searchHost, source);
            baseHost = source.getHost();
            communityId = response.communityId;
            accessToken = response.accessToken;
        }

        public void close() {
            super.close();
        }

        RequestExecutor<SearchResponse> search(String searchText, SearchScope scope, int count, int version, String lang) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("query", searchText);
            if (!scope.isEmpty())
                parameters.put("scope", SearchScope.toString(scope));
            if (!Utils.isEmpty(communityId))
                parameters.put("communityId", communityId);
            if (!Utils.isEmpty(accessToken))
                parameters.put("access_token", accessToken);
            if (count >= 0)
                parameters.put("count", Integer.toString(count));
            if (version >= 0)
                parameters.put("version", Integer.toString(version));
            if (lang != null)
                parameters.put("lang", lang);
            parameters.put("host", baseHost);

            return apiGet("/api/1.0/search", parameters.entrySet(), ConstructClassType(SearchResponse.class));
        }
    }

    private SearchClient searchClient;

    public Client(String host) {
        super(host, null, null, null);
    }

    public Client(String host, String token) {
        super(host, token, null, null);
    }

    public Client(String host, String clientId, String clientSecret) {
        super(host, null, clientId, clientSecret);
    }

    public void close() {
        super.close();
        if (searchClient != null)
            searchClient.close();
    }

    public RequestExecutor<Dataset> getDataset(String id) {
        return apiGet(String.format("/api/1.0/meta/dataset/%s", id), ConstructClassType(Dataset.class));
    }

    public RequestExecutor<Iterable<Dataset>> listDatasets(String source, String topic, String region) {
        if (Utils.isEmpty(source) && Utils.isEmpty(topic) && Utils.isEmpty(region)) {
            return apiGet("/api/1.0/meta/dataset", ConstructListType(Dataset.class));
        }
        else {
            HashMap<String, String> postData = new HashMap<>();
            if (!Utils.isEmpty(source))
                postData.put("source", source);
            if (!Utils.isEmpty(topic))
                postData.put("topic", topic);
            if (!Utils.isEmpty(region))
                postData.put("region", region);
            return apiPost("/api/1.0/meta/dataset", postData, ConstructListType(Dataset.class));
        }
    }

    public RequestExecutor<Dimension> getDatasetDimension(String dataset, String dimension) {
        return apiGet(String.format("/api/1.0/meta/dataset/%s/dimension/%s", dataset, dimension), ConstructClassType(Dimension.class));
    }

    public RequestExecutor<PivotResponse> getData(PivotRequest request) {
        return apiPost("/api/1.0/data/pivot/", request, ConstructClassType(PivotResponse.class));
    }

    public RequestExecutor<List<PivotResponse>> getData(Iterable<PivotRequest> requests) {
        return apiPost("/api/1.0/data/multipivot", requests, ConstructListType(PivotResponse.class));
    }

    public RequestExecutor<RegularTimeSeriesRawDataResponse> getDataBegin(PivotRequest pivot) {
        return apiPost("/api/1.0/data/raw/", pivot, ConstructClassType(RegularTimeSeriesRawDataResponse.class));
    }

    public RequestExecutor<RegularTimeSeriesRawDataResponse> getDataStreaming(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("continuationToken", token);
        return apiGet("/api/1.0/data/raw/", params.entrySet(), ConstructClassType(RegularTimeSeriesRawDataResponse.class));
    }

    public RequestExecutor<FlatTimeSeriesRawDataResponse> getFlatDataBegin(PivotRequest pivot) {
        return apiPost("/api/1.0/data/raw/", pivot, ConstructClassType(FlatTimeSeriesRawDataResponse.class));
    }

    public RequestExecutor<FlatTimeSeriesRawDataResponse> getFlatDataStreaming(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("continuationToken", token);
        return apiGet("/api/1.0/data/raw/", params.entrySet(), ConstructClassType(FlatTimeSeriesRawDataResponse.class));
    }

    public RequestExecutor<Iterable<UnitMember>> getUnits() {
        return apiGet("/api/1.0/meta/units", ConstructListType(UnitMember.class));
    }

    public RequestExecutor<Iterable<TimeSeriesItem>> getTimeSeriesList(String datasetId, FullDimensionRequest request) {
        return apiPost(String.format("/api/1.0/data/dataset/%s", datasetId), request, ConstructListType(TimeSeriesItem.class));
    }

    public RequestExecutor<PostResult> uploadPost(String fileName) {
        File bin = new File(fileName);
        HttpEntity request = MultipartEntityBuilder.create()
                .addBinaryBody("\"file\"", bin, ContentType.APPLICATION_OCTET_STREAM, fileName)
                .build();
        RequestExecutor<PostResult> result = apiPost("/api/1.0/upload/post", request, ConstructClassType(PostResult.class));
        return result.client(buildClient(DEFAULT_TIMEOUT * 10).build()).autoClose();
    }

    public RequestExecutor<VerifyResult> uploadVerify(String filePath, String existingDatasetIdToModify) {
        HashMap<String, String> params = new HashMap<>();
        params.put("filePath", filePath);
        if (!Utils.isEmpty(existingDatasetIdToModify)) {
            params.put("datasetId", existingDatasetIdToModify);
        }
        return apiGet("/api/1.0/upload/verify", params.entrySet(), ConstructClassType(VerifyResult.class));
    }

    public RequestExecutor<VerifyResult> uploadVerify(String filePath) {
        return uploadVerify(filePath, null);
    }

    public RequestExecutor<UploadResult> uploadSubmit(DatasetUpload upload) {
        return apiPost("/api/1.0/upload/save", upload, ConstructClassType(UploadResult.class));
    }

    public RequestExecutor<UploadResult> uploadStatus(int uploadId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", Integer.toString(uploadId));
        return apiGet("/api/1.0/upload/status", params.entrySet(), ConstructClassType(UploadResult.class));
    }

    public RequestExecutor<VerifyDatasetResult> verifyDataset(String id, Date publicationDate, String source, String refUrl) {
        HashMap<String, Object> postData = new HashMap<>();
        postData.put("id", id);
        if (publicationDate != null) {
            postData.put("publicationDate", publicationDate);
        }
        if (source != null) {
            postData.put("source", source);
        }
        if (refUrl != null) {
            postData.put("refUrl", refUrl);
        }
        return apiPost("/api/1.0/meta/verifydataset", postData, ConstructClassType(VerifyDatasetResult.class));
    }

    public RequestExecutor<VerifyDatasetResult> verifyDataset(String id) {
        return verifyDataset(id, null, null, null);
    }

    public RequestExecutor<DateRange> getDatasetDateRange(String datasetId) {
        return apiGet(String.format("/api/1.0/meta/dataset/%s/daterange", datasetId), ConstructClassType(DateRange.class));
    }

    public UploadResult uploadDataset(String filename, String datasetName) throws InterruptedException, ExecutionException
    {
        PostResult postResult = uploadPost(filename).get();
        if (!postResult.successful)
            return null;

        VerifyResult verifyResult = uploadVerify(postResult.properties.location).get();
        if (!verifyResult.successful)
            return null;

        DatasetUpload upload = new DatasetUpload();
        upload.name = datasetName;
        upload.uploadFormatType = verifyResult.uploadFormatType;
        upload.columns = verifyResult.columns;
        upload.flatDSUpdateOptions = verifyResult.flatDSUpdateOptions;
        upload.fileProperty = postResult.properties;

        UploadResult submitResult = uploadSubmit(upload).get();
        UploadResult statusResult;
        do {
            Thread.sleep(5000);
            statusResult = uploadStatus(submitResult.id).get();
            if (!"in progress".equals(statusResult.status))
                break;
        }
        while (true);

        statusResult = uploadStatus(submitResult.id).get();
        return statusResult;
    }


    private SearchClient getSearchClient() {
        if (searchClient == null)
        {
            RequestExecutor<ConfigResponse> configExec = apiGet("/api/1.0/search/config", ConstructClassType(ConfigResponse.class));
            ConfigResponse configResponse = null;
            try {
                configResponse = configExec.get();
            }
            catch (final InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (configResponse == null) {
                configResponse = new ConfigResponse();
                configResponse.searchHost = getHost();
            }
            searchClient = new SearchClient(this, configResponse);
        }

        return searchClient;
    }

    public RequestExecutor<SearchResponse> search(String searchText, SearchScope scope, int count, String lang) {
        return getSearchClient()
                .search(searchText, scope, count, 0, lang);
    }

    public RequestExecutor<SearchResponse> search(String searchText, SearchScope scope, int count) {
        return search(searchText, scope, count, null);
    }
}
