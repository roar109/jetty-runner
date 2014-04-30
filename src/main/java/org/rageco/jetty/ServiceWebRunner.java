package org.rageco.jetty;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.webapp.WebAppContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to startup Jetty so that we can run our services in a web container. The command line arguments are given to
 * Jetty's WebAppContext.
 * 
 * This class accepts the following parameters
 * <dl>
 * <dt>-wd &lt;Path to directory relative to project&gt;</dt>
 * <dd>This parameter supplies the war path to use. The default is Services/Deploy/src/main/webapp</dd>
 * <dt>--port &lt;The port the container should use&gt;</dt>
 * <dd>The startup port for Jetty. Should be unique</dd>
 * <dt>--context &lt;Custom context&gt;</dt>
 * <dd>The custom context Jetty should expose. Typically this will be set to the name of the built war. Default is
 * /service</dd>
 * </dl>
 * 
 * @author Marc Foley
 * @version $Id: ServiceWebRunner.java 30728 2010-10-06 16:45:51Z rhernandez $
 */
public class ServiceWebRunner
{
   private static final String DEFAULT_WAR_DIR     = "Services/Deploy/src/main/webapp";
   private static final String DEFAULT_APP_CONTEXT = "/service";


   private static Map <String, String> parseInputArguements (final String[] args)
   {
      final Map <String, String> _inputArgs = new HashMap <String, String> ();
      _inputArgs.put ("PORT", "8080");
      _inputArgs.put ("WEB_DIR", DEFAULT_WAR_DIR);
      _inputArgs.put ("CONTEXT", DEFAULT_APP_CONTEXT);
      if ( (null != args) && (0 < args.length))
      {
         for (int i = 0; i < args.length; i++)
         {
            final String _arg = args[i];
            if ("--port".equalsIgnoreCase (_arg))
            {
               _inputArgs.put ("PORT", extractParam (args, i + 1));
            }

            if ("-wd".equalsIgnoreCase (_arg))
            {
               _inputArgs.put ("WEB_DIR", extractParam (args, i + 1));
            }

            if ("--context".equalsIgnoreCase (_arg))
            {
               _inputArgs.put ("CONTEXT", extractParam (args, i + 1));
            }
         }
      }

      return _inputArgs;
   }


   private static String extractParam (final String[] values, final int location)
   {
      String _value = null;
      if (values.length >= location - 1)
      {
         _value = values[location];
      }
      return _value;

   }


   /**
    * main class method to start the jetty launcher. If there is any exception it will be reported.
    * 
    * @param args the arguments for the runner
    * @since Apr 1, 2008
    */
   public static void main (final String[] args)
   {

      final Map <String, String> _args = parseInputArguements (args);

      try
      {
         final Server server = new Server ();
         server.setStopAtShutdown (true);
         final ContextHandlerCollection contexts = new ContextHandlerCollection ();
         server.setHandler (contexts);
         final SocketConnector connector = new SocketConnector ();
         connector.setPort (Integer.parseInt (_args.get ("PORT")));
         server.setConnectors (new Connector[] {connector});
         final WebAppContext webapp = new WebAppContext (_args.get ("WEB_DIR"), _args.get ("CONTEXT"));
         contexts.addHandler (webapp);
         server.start ();
         webapp.start ();
      }
      catch (final Exception e)
      {
         System.err.print ("Unable to start Jetty");
         e.printStackTrace ();
      }

   }
}
