package au.com.pvlighthouse.activemq;

import org.apache.activemq.transport.discovery.DiscoveryAgent;
import org.apache.activemq.transport.discovery.DiscoveryAgentFactory;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.util.URISupport.CompositeData;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class SrvDiscoveryAgentFactory extends DiscoveryAgentFactory {

    

    @Override
    protected DiscoveryAgent doCreateDiscoveryAgent(URI uri) throws IOException {
        try {

            CompositeData data = URISupport.parseComposite(uri);
            Map options = data.getParameters();

            SrvDiscoveryAgent rc = new SrvDiscoveryAgent();
            IntrospectionSupport.setProperties(rc, options);
            rc.setServices(data.getComponents());

            return rc;

        } catch (Throwable e) {
            throw IOExceptionSupport.create("Could not create discovery agent: " + uri, e);
        }
    }

}