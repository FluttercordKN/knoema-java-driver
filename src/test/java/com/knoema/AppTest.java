package com.knoema;

import com.knoema.core.Utils;
import com.knoema.search.SearchScope;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    private static final String testHost = "knoema.com";
    
    /**
     * Test getting timeseries
     */
    public void testGetSeries() throws Exception {
        boolean result = Tester.testSeries(testHost);

        Assert.assertTrue(result);
    }

    /**
     * Test search timeseries
     */
    public void testSearchTimeSeries() throws Exception {
        int total = Tester.testSearchTimeSeries(testHost);

        Assert.assertTrue(total > 0);
    }

    /**
     * Test Flat Series JSON serialization
     */
    public void testFlatSeriesJsonSerialization() throws IOException {
        String field = Tester.testFlatSeriesSerialization();

        Assert.assertTrue(!Utils.isEmpty(field));
    }

    /**
     * Test complex JSON serialization
     */
    public void testComplexJsonSerialization() throws IOException {
        String field = Tester.testComplexSerialization();

        Assert.assertTrue(!Utils.isEmpty(field));
    }

    /**
     * Test custom JSON serialization
     */
    public void testCustomJsonSerialization() throws IOException {
        String field = Tester.testCustomSerialization();

        Assert.assertTrue(!Utils.isEmpty(field));
    }

    /**
     * Test JSON serialization
     */
    public void testJsonSerialization() throws IOException {
        String field = Tester.testSerialization();

        Assert.assertTrue(!Utils.isEmpty(field));
    }

    /**
     * Test Dataset Metadata
     */
    public void testDatasetMetadata() throws Exception {
        String datasetName = Tester.testDatasetMetadata(testHost);

        Assert.assertTrue(!Utils.isEmpty(datasetName));
    }

    /**
     * Test Dataset List
     */
    public void testDatasetList() throws Exception {
        String datasetName = Tester.testDatasetList(testHost);

        Assert.assertTrue(!Utils.isEmpty(datasetName));
    }

    /**
     * Test Pivot
     */
    public void testPivot() throws Exception {
        int tupleCount = Tester.testPivot(testHost);

        Assert.assertTrue(tupleCount > 0);
    }

    /**
     * Test SearchScope enum
     */
    public void testSearchScopeEnum() {

        String field = SearchScope.of(SearchScope.Timeseries, SearchScope.Atlas).toString();

        Assert.assertTrue("Timeseries,Atlas".equals(field));
    }

    /**
     * Test TimeFormat expand range
     */
    public void testExpandRange() {

        TimeFormat timeFormat = TimeFormat.INVARIANT_TIME_FORMAT;

        Calendar starDate = timeFormat.getInstance();
        starDate.set(2000, Calendar.JANUARY, 1, 0, 0 ,0);
        Calendar endDate = timeFormat.getInstance();
        endDate.set(2000, Calendar.DECEMBER, 31, 0, 0, 0);

        int points;
        points = timeFormat.expandRangeSelection(starDate.getTime(), endDate.getTime(), Frequency.Annual).size();
        Assert.assertTrue(points == 1);

        points = timeFormat.expandRangeSelection(starDate.getTime(), endDate.getTime(), Frequency.Monthly).size();
        Assert.assertTrue(points == 12);

        points = timeFormat.expandRangeSelection(starDate.getTime(), endDate.getTime(), Frequency.Quarterly).size();
        Assert.assertTrue(points == 4);

        points = timeFormat.expandRangeSelection(starDate.getTime(), endDate.getTime(), Frequency.SemiAnnual).size();
        Assert.assertTrue(points == 2);

        starDate.set(2000, Calendar.FEBRUARY, 1);
        endDate.set(2000, Calendar.MARCH, 1);
        points = timeFormat.expandRangeSelection(starDate.getTime(), endDate.getTime(), Frequency.Daily).size();
        Assert.assertTrue(points == 30);

        starDate.set(1900, Calendar.FEBRUARY, 1);
        endDate.set(1900, Calendar.MARCH, 1);
        points = timeFormat.expandRangeSelection(starDate.getTime(), endDate.getTime(), Frequency.Daily).size();
        Assert.assertTrue(points == 29);
    }


    /**
     * Test property set conversion
     */
    public void testPropertySet() {

        Map<String, Object> propSet = Utils.toPropertySet(new Object() {
            public final String datasetId = "IMFWEO2017Oct";
            public final String frequency = "A";
            public final String timeRange = "2015-2016";
            public final String country = "612;614";
            public final String[] subject = new String[] { "NGDPD", "NGDP"};
        });

        Assert.assertTrue(propSet.containsKey("datasetId"));

        Assert.assertTrue("IMFWEO2017Oct".equals(propSet.get("datasetId")));

        Assert.assertTrue(propSet.containsKey("subject"));

        Assert.assertTrue(propSet.get("subject") instanceof String[]);

        Object[] data = (Object[])(propSet.get("subject"));
        Assert.assertEquals(2, data.length);
        Assert.assertTrue(data[0] instanceof String);
    }
}
