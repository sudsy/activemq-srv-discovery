package au.com.pvlighthouse.activemq;

import java.net.URI;
import java.net.URISyntaxException;

import org.hamcrest.Matcher;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.containsString;

import static org.junit.Assert.assertThat;
import org.junit.Test;
import java.util.Arrays;



/**
 * Unit test for simple App.
 */
public class AppTest 
{
   

    @Test
    public void testServiceDiscovery()
    {
        // consul:(http://consul.example.com?service=active-mq&amp;address=amq.example.com&amp;port=61616)
        URI services[] = new URI[1];
        
        try {
            services[0] = new URI("srv:_jabber._tcp.gmail.com");
            System.out.println("URI created: " + services[0]);
        }
        catch (URISyntaxException e) {
            System.out.println("URI Syntax Error: " + e.getMessage());
        }
 

        SrvDiscoveryAgent discoveryAgent = new SrvDiscoveryAgent();
        discoveryAgent.setServices(services);
        String[] resultServices = discoveryAgent.getServices();
        // Matcher items = hasItems("a", "b");
        assertThat(Arrays.asList(resultServices), hasItems("tcp://xmpp-server.l.google.com:5269","tcp://alt1.xmpp-server.l.google.com:5269"));
        
    }
}
