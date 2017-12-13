package com.knoema;

import com.knoema.core.PropSet;
import com.knoema.series.TimeSeriesFrame;
import com.knoema.series.TimeSeriesId;
import com.knoema.series.TimeSeriesValues;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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

    private final String testHost = null;
    /**
     * Test series
     */
    public void testSeries() throws Exception {
        if (testHost != null)
            Knoema.getInstance().setClient(new Client(testHost));

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
    }
}
