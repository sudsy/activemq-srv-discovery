package au.com.pvlighthouse.activemq;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.apache.activemq.transport.discovery.simple.SimpleDiscoveryAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SrvDiscoveryAgent extends SimpleDiscoveryAgent {

    private final static Logger LOG = LoggerFactory.getLogger(SrvDiscoveryAgent.class);

    private URI[] srvHosts = new URI[]{};

    @Override
    public String[] getServices() {
        return Arrays.copyOf(this.srvHosts, this.srvHosts.length, String[].class);
    }

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

        for (int i = 0; i < this.srvHosts.length; i++) {
            if(!this.srvHosts[i].getScheme().equals("srv")){
                throw new IllegalArgumentException("Expecting srv protocol found : " + this.srvHosts[i].getScheme());
            }
            
        }
        StringBuffer buf = new StringBuffer();

        buf.append("failover:(");

        for (int i = 0; i < (srvHosts.length - 1); i++) {
            buf.append(srvHosts[i]);
            buf.append(',');
        }
        buf.append(srvHosts[srvHosts.length - 1]);

        buf.append(")?randomize=false&maxReconnectAttempts=0");

        super.setServices(new String[]{buf.toString()});
    }
}
