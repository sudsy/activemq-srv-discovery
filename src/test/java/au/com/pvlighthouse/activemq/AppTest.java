package au.com.pvlighthouse.activemq;

import java.net.URI;
import java.net.URISyntaxException;


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

    /**
     * Rigourous Test :-)
     */
    public void testServiceDiscovery()
    {
        // consul:(http://consul.example.com?service=active-mq&amp;address=amq.example.com&amp;port=61616)
        URI services[] = new URI[1];
        
        try {
            services[0] = new URI("srv:test");
            System.out.println("URI created: " + services[0]);
        }
        catch (URISyntaxException e) {
            System.out.println("URI Syntax Error: " + e.getMessage());
        }
 

        SrvDiscoveryAgent discoveryAgent = new SrvDiscoveryAgent();
        discoveryAgent.setServices(services);
    }
}
