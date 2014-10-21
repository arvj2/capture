package com.claro.cfc.oss.geff;

import com.claro.cfc.oss.environment.Environment;
import com.claro.cfc.oss.environment.Environments;
import com.claro.cfc.oss.utils.GenericTaskExecutionResult;
import com.claro.cfc.oss.utils.StringUtils;
import com.claro.cfc.oss.utils.TaskResponseParser;
import com.claro.cfc.scheduler.activation.ActivationContext;
import com.claro.cfc.scheduler.tasks.*;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/29/2014.
 */
public class GeffTaskHandler implements TaskHandler {

    private static final String HANDLER_NAME = "GEFF OSS Client Service";
    private ActivationContext context;

    public GeffTaskHandler(ActivationContext context) {
        this.context = context;
    }

    @Override
    public Configuration getConfiguration() {
        return new Configuration() {
        };
    }

    @Override
    public String getDisplayName() {
        return HANDLER_NAME;
    }


    @Override
    public TaskExecutionResult perform(Task task) {
        Logger logger = Logger.getLogger(GeffTaskHandler.class);
        logger.info(HANDLER_NAME + "(: perform work for task ) " + task);
        return run(task, logger);
    }

    private TaskExecutionResult run(Task task, Logger logger) {
        String message = getMessage(task, logger);
        logger.info(HANDLER_NAME + "(:perform:) sending message");
        logger.info(HANDLER_NAME + "(:perform:) "+message);

        if (null == message) {
            logger.info(HANDLER_NAME + "(:perform:) skipping work due on null message");
            return null;
        }

        String result = null;

        try{
            result = sendMessage(message, logger);
        }catch ( Exception ex ){
           logger.error( "***** Error trying to send update message for "+HANDLER_NAME );
           ex.printStackTrace();
        }

        if (null == result) {
            logger.warn(HANDLER_NAME + "Some error trying to fetch response for service");
            return null;
        }

        GenericTaskExecutionResult taskResult = TaskResponseParser.toResult(result);
        if (null != taskResult) {
            taskResult.setOutgoingXml(message);
            taskResult.setIngoingXml(result);
        }

        return taskResult;
    }

    private String getMessage(Task task, Logger logger) {
        List<Attribute> attrs = task.getAttributes();
        if (null == attrs || 0 == attrs.size()) {
            logger.info(HANDLER_NAME + "(:getMessage:) getting not valid attributes from task ( " + task + " )");
            return null;
        }

        int index = 0;

        String splitterPort = "empty";
        String splitter = "empty";
        String terminal = "empty";
        String cabina = "empty";
        String phone = "empty";

        if (0 <= (index = attrs.indexOf(Attribute.createAttribute("SPLITTER_PORT", ""))))
            splitterPort = StringUtils.asString(attrs.get(index).getValue());
        if (0 <= (index = attrs.indexOf(Attribute.createAttribute("SPLITTER", ""))))
            splitter = StringUtils.asString(attrs.get(index).getValue());
        if (0 <= (index = attrs.indexOf(Attribute.createAttribute("TERMINAL_FO", ""))))
            terminal = StringUtils.asString(attrs.get(index).getValue());
        if (0 <= (index = attrs.indexOf(Attribute.createAttribute("CABINA", ""))))
            cabina = StringUtils.asString(attrs.get(index).getValue());
        if (0 <= (index = attrs.indexOf(Attribute.createAttribute("TELEFONO", "")))) {
            phone = StringUtils.asString(attrs.get(index).getValue());
            if( null != phone ){
                phone = phone.replace( "-","" ).replace("(","").replace(")","").replace(" ","");
            }
        }

        String message =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:clar=\"http://claro.gpon.webservice/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "       <clar:UpdateFacilities>\n" +
                        "           <clar:Splitterport>" + splitterPort + "</clar:Splitterport>\n" +
                        "           <clar:Splitter>" + splitter + "</clar:Splitter>\n" +
                        "           <clar:Terbox>" + terminal + "</clar:Terbox>\n" +
                        "           <clar:Cabin>" + cabina + "</clar:Cabin>\n" +
                        "           <clar:TN>" + phone + "</clar:TN>\n" +
                        "       </clar:UpdateFacilities>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return message;
    }

    private String sendMessage(String message, Logger logger) {
        try {
            Environment env = Environments.getDefault();
            logger.info(HANDLER_NAME + "(:sendMessage:) trying to send message to endpoint on " + env.geffHost());

            HttpURLConnection con = (HttpURLConnection) new URL(env.geffHost()).openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setRequestProperty("SOAPAction", "http://claro.gpon.webservice/UpdateFacilities");
            con.connect();

            OutputStream out = con.getOutputStream();
            if (null != out) {
                out.write(message.getBytes());
            }
            out.flush();
            out.close();

            byte[] buffer = new byte[8192];
            InputStream in = con.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (null != in) {
                int i = 0;
                for (; ; ) {
                    i = in.read(buffer);
                    if (0 >= i)
                        break;
                    builder.append(new String(buffer));
                }
            }
            in.close();
            con.disconnect();
            String result = builder.toString().trim();
            logger.info(HANDLER_NAME + "(:sendMessage:) getting response ( " + result + " )\n");
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
