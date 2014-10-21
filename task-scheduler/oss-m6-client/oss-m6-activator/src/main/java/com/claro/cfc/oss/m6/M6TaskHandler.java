package com.claro.cfc.oss.m6;

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
public class M6TaskHandler implements TaskHandler {

    private static final String HANDLER_NAME = "M6 OSS Client Service";
    private ActivationContext context;

    public M6TaskHandler(ActivationContext context) {
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
        final Logger logger = Logger.getLogger(M6TaskHandler.class);
        logger.info(HANDLER_NAME + "(: perform work for task ) " + task);
        return run(task,logger);
    }

    private TaskExecutionResult run(Task task,Logger logger) {
        String message = getMessage(task,logger);
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
        if( null != taskResult ) {
            taskResult.setOutgoingXml(message);
            taskResult.setIngoingXml(result);
        }

        return taskResult;
    }

    private String getMessage(Task task,Logger logger) {
        List<Attribute> attrs = task.getAttributes();
        if (null == attrs || 0 == attrs.size()) {
            logger.info(HANDLER_NAME + "(:getMessage:) getting not valid attributes from task ( " + task + " )");
            return null;
        }

        int index = 0;

        String phone = "empty";
        String port = "empty";
        String dslam = "empty";
        String userId = "empty";

        if (0 <= (index = attrs.indexOf(Attribute.createAttribute("TELEFONO", "")))) {
            phone = StringUtils.asString(attrs.get(index).getValue());
            if( null != phone ){
                phone = getPhone(phone);
            }
        }
        if (0 <= (index = attrs.indexOf(Attribute.createAttribute("PUERTO", ""))))
            port = StringUtils.asString(attrs.get(index).getValue());
        if (0 <= (index = attrs.indexOf(Attribute.createAttribute("DSLAM", ""))))
            dslam = StringUtils.asString(attrs.get(index).getValue());
        if (0 <= (index = attrs.indexOf(Attribute.createAttribute("TARJETA", ""))))
            userId = StringUtils.asString(attrs.get(index).getValue());

        String message =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:open=\"http://www.openuri.org/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "       <open:updateClientFacility>\n" +
                        "           <open:request>\n" +
                        "                <open:tn>" + phone + "</open:tn>\n" +
                        "                <open:puerto>" + port + "</open:puerto>\n" +
                        "                <open:dslam>" + dslam + "</open:dslam>\n" +
                        "                <open:userId>" + userId + "</open:userId>\n" +
                        "           </open:request>\n" +
                        "       </open:updateClientFacility>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        return message;
    }

    private String sendMessage(String message,Logger logger) {
        try {
            Environment env = Environments.getDefault();
            logger.info(HANDLER_NAME + "(:sendMessage:) trying to send message to endpoint on " + env.m6Host());

            HttpURLConnection con = (HttpURLConnection) new URL(env.m6Host()).openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setRequestProperty("SOAPAction", "http://www.openuri.org/updateClientFacility");
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


    private String getPhone( String phone){
        if( null == phone )
            return "empty.null";
        phone = phone.replace("(","").replace(")","").replace("-","").replace(" ","");
        try{
            phone = phone.substring(0,3) + "-" + phone.substring(3,6) +"-"+ phone.substring(6);
        }catch( Exception ex ){
            ex.printStackTrace();
        }
        return phone;
    }
}
