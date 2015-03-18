/*
* Copyright (c) Duncan Jauncey 2013.   Free for non-commercial use.
*/

package xplanedata;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import xpdisplay.http.JSONServer;
import xpdisplay.io.provider.UDPReaderDataProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;




public class Main {
    
    private static JSONServer jsonServer;
    
    public Main() {
    }
    
    private static void usage() {
        System.out.println("Note: you can provide command line arguments [port] [address] to bind to a specific port and IP address, e.g. java -jar XPDisplay.jar 49050 192.168.0.5");
    }
    
    public static void main(String[] args) throws Exception {
        if( args.length == 0 ) {
            usage();
        }
        
        int port = 49003;
        int webPort = 8888;
        InetAddress address = InetAddress.getLocalHost();
        
        try {
            
            if( args.length == 1 ) {
                port = Integer.parseInt(args[0]);
            }
            if( args.length == 2 ) {
                address = InetAddress.getByName(args[1]);
            }
            if( args.length > 2 ) {
                System.out.println("Unexpected number of arguments.  Got "+args.length+", expected none, 1 or 2.");
                usage();
                return;
            }
        } catch( Throwable t ) {
            System.out.println("Sorry, I couldn't understand the command-line arguments.  If in doubt, specify both port and IP address, e.g. java -jar XPDisplay.jar 49005 192.168.0.5");
            System.out.println(" >>> Underlying error: "+t.getMessage());
            return;
        }
        
        final int portF = port;
        final InetAddress addressF = address;
        System.out.println("Setting listen port to "+portF);
        System.out.println("Setting bind address to "+addressF.getHostAddress());

        UDPReaderDataProvider dp = new UDPReaderDataProvider(portF, addressF);
        jsonServer = new JSONServer(dp);
        
        HttpServer server = HttpServer.create(new InetSocketAddress(webPort), 0);
        server.createContext("/data.json", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        
        
        Server serverJetty = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        serverJetty.addConnector(connector);
 
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
 
        resource_handler.setResourceBase("html/");
 
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        serverJetty.setHandler(handlers);
 
        serverJetty.start();
        serverJetty.join();

    }
    
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = jsonServer.getJSONData();
            String requestMethod = t.getRequestMethod();
        
            Headers h = t.getResponseHeaders();
            //h.add("Content-Type", "application/jsonp; charset=UTF-8");
            h.add("Access-Control-Allow-Origin","*");
            h.add("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
            h.add("Access-Control-Allow-Methods","POST, GET, OPTIONS");

            
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
            t.close();
        }

        
       
    }
    
}