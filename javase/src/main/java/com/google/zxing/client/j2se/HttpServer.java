package com.google.zxing.client.j2se;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.MultiPartInputStreamParser.MultiPart;

// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

public class HttpServer extends HttpServlet {

    private DecodeWorker decodeWorker = new DecodeWorker();

    public HttpServer() {
    }

    public static void main(String[] args) throws Exception {
        String hostname = "0.0.0.0";
        int port = 8080;
        if (args.length > 1) {
            hostname = args[0].trim();
            port = Integer.parseInt(args[1]);
        } else if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        Server server = new Server();
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setHost(hostname);
        serverConnector.setPort(port);
        server.setConnectors(new Connector[] { serverConnector });
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        ServletHolder uploadHolder = context.addServlet(HttpServer.class, "/");
        MultipartConfigElement multipartConfig = new MultipartConfigElement(null, -1L, -1L, 1);
        uploadHolder.getRegistration().setMultipartConfig(multipartConfig);
        server.setHandler(context);
        server.start();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MultiPart part = (MultiPart) req.getParts().iterator().next();
        String result = "-1";
        try {
            boolean canDecode = decodeWorker.canDecode(part.getFile().toURI());
            if (canDecode) {
                result = "1";
            } else {
                result = "0";
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "-1";
        }
        resp.getWriter().write(result);
        resp.getWriter().flush();
    }

    // @Override
    // public Response serve(IHTTPSession session) {
    // InputStream is = session.getInputStream();
    // String result = null;
    // try {
    // boolean canDecode = decodeWorker.canDecode(is);
    // if(canDecode) {
    // result = "1";
    // } else {
    // result = "0";
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // result = "-1";
    // }

    // return newFixedLengthResponse(result);
    // }
}
