Interface to the Knoema API
========

This is the official documentation for Knoema's Java Driver Package. The package can be used for obtaining data from the datasets from the site knoema.com.

# Reference

To add reference to this Knoema package from your Maven project please use following dependency

        <dependency>
            <groupId>com.knoema</groupId>
            <artifactId>knoema-java-driver</artifactId>
            <version>1.0.7</version>
        </dependency>
    
# Retrieving series from datasets
There is one of the methods for retrieving series from datasets in Java: the Knoema.get method. The method works with knoema datasets.

The following quick call can be used to retrieve a timeseries from dataset:

        import com.knoema.core.PropSet;
        import com.knoema.series.TimeSeriesFrame;

        TimeSeriesFrame data = Knoema.get("IMFWEO2017Oct", new PropSet()
                .add("frequency", "A")
                .add("country", "612;914")
                .add("subject", new String[] { "NGDP", "NGDPD" })
        );
   
where:

* "IMFWEO2017Apr" this is a public dataset, that available for all users by reference https://knoema.com/IMFWEO2017Apr
* country and subject are dimensions names
* "612" is the code of country *Angola*, "914" is code of country *Albania*
* "ngdp" is code of subject *Gross domestic product, current prices (U.S. dollars)*
* you can specify multiple dimension elements as a string with ';'-separated values or as an array of strings
* please note that you need to identify all dimensions of the dataset, and for each dimension to indicate the selection. Otherwise, the method returns an error.

This example finds all data points for the dataset IMFWEO2017Apr with selection by countries = *Angola*, *Albania* and subject =  *Gross domestic product, current prices (U.S. dollars)*. 
To get single timeseries from the fetched data you can access this data as a map of time series id to time series values 

        import com.knoema.core.PropSet;
        import com.knoema.series.TimeSeriesValues;
        
        TimeSeriesValues series = data.get(new PropSet()
            .add("frequency", "A")
            .add("country", "612")
            .add("subject", "NGDP")
        );

# Authentication
By default, the package allows you to work only with public datasets from the site knoema.com and has a limit on the number of requests.
To make full use of the package we recommend you to use parameters clientId and clientSecret.
You can get these parameters after registering on the site knoema.com, in the section "My profile - Apps - create new" (or use existing applications).
For a quick call you can use the link https://knoema.com/user/apps. 
If on this page you have some applications - open one of them or create a new one.
You can see the parameters client id and client secret at the bottom of the page and then use them in the functions.
How to use these parameters in the functions will be shown below.

In order to get access to private datasets please you need to create client with the respective parameters and set it as default Knoema instance client.

        import com.knoema.Client;
        
        Client client = new Client(host, clientId, clientSecret);
        Knoema.getInstance().setClient(client);
