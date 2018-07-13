package au.com.pvlighthouse.activemq;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuffer;
import org.apache.activemq.transport.discovery.simple.SimpleDiscoveryAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.spotify.dns.DnsException;
import com.spotify.dns.DnsSrvResolver;
import com.spotify.dns.DnsSrvResolvers;
import com.spotify.dns.LookupResult;


public class SrvDiscoveryAgent extends SimpleDiscoveryAgent {

    private final static Logger LOG = LoggerFactory.getLogger(SrvDiscoveryAgent.class);

    private URI[] srvHosts = new URI[]{};

    // @Override
    // public String[] getServices() {
    //     return Arrays.copyOf(this.srvHosts, this.srvHosts.length, String[].class);
    // }

    @Override
    public void setServices(String services) {
        String[] svcArray = services.split(",");
        this.srvHosts = new URI[svcArray.length];
        for (int i = 0; i < svcArray.length; i++) {
            try {
                srvHosts[i] = new URI(svcArray[i]);
                System.out.println("URI created: " + svcArray[i]);
            }
            catch (URISyntaxException e) {
                System.out.println("URI Syntax Error: " + e.getMessage());
            }      
        }
        configureServices();
    }

    @Override
    public void setServices(String services[]) {
        this.srvHosts = new URI[services.length];
        for (int i = 0; i < services.length; i++) {
            try {
                srvHosts[i] = new URI(services[i]);
                System.out.println("URI created: " + services[i]);
            }
            catch (URISyntaxException e) {
                System.out.println("URI Syntax Error: " + e.getMessage());
            }      
        }
        configureServices();
    }

    @Override
    public void setServices(URI services[]) {
        this.srvHosts = services;
        configureServices();
    }

    protected void configureServices() {
        if ((srvHosts == null)) {
            LOG.error("masterSlave requires at least 1 host");
            srvHosts = new URI[]{};
            throw new IllegalArgumentException("Expecting at least 1 argument");
        }

        DnsSrvResolver resolver = DnsSrvResolvers.newBuilder()
            .cachingLookups(true)
            .retainingDataOnFailures(true)
            .dnsLookupTimeoutMillis(1000)
            .build();

        List<URI> staticHosts = new ArrayList<URI>();

        
        for (int i = 0; i < this.srvHosts.length; i++) {
            if(!this.srvHosts[i].getScheme().equals("srv") || this.srvHosts[i].getSchemeSpecificPart() == null){
                throw new IllegalArgumentException("Expecting srv:host found : " + this.srvHosts[i].getScheme() + ":" + this.srvHosts[i].getSchemeSpecificPart());
            }
            
            
            
            List<LookupResult> nodes = resolver.resolve(this.srvHosts[i].getSchemeSpecificPart());
            for(LookupResult currentNode : nodes){
                try {
                    String host = currentNode.host();
                    staticHosts.add(new URI("tcp://" + host.substring(0, host.length() - 1) + ":" + currentNode.port() ));
                    
                }
                catch (URISyntaxException e) {
                    System.out.println("URI Syntax Error: " + e.getMessage());
                }     
                
                
            }
            
        }


        super.setServices(staticHosts.toArray(new URI[staticHosts.size()]));
    }
}
