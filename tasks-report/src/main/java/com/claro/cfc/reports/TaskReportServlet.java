package com.claro.cfc.reports;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 10/3/2014.
 */
public class TaskReportServlet extends HttpServlet{
    private final Logger log = Logger.getLogger( this.getClass() );

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=tasks-report.csv");

        log.info( "Getting task  reports" );

        List<String> lines = getReportLines();
        OutputStream out = res.getOutputStream();

        for( String line : lines )
             out.write( line.getBytes() );
    }

    private List<String> getReportLines(){

        final String stringConnection = "jdbc:oracle:thin:@//172.27.6.78:1521/appceltec";
        final String user = "APPCAPFAC";
        final String pass = "NRe58TMLdk";

        try{
            Class.forName( "oracle.jdbc.driver.OracleDriver" );
        }catch ( ClassNotFoundException e){
            log.error( e );
			e.printStackTrace();
        }

        List<String> result = new ArrayList<String>();
        StringBuilder collect = new StringBuilder();
        injectHeaders(collect);
        result.add( collect.toString() );


        try {
            Connection con = DriverManager.getConnection(stringConnection, user, pass);
            Statement stm = con.createStatement();
            ResultSet set = stm.executeQuery( query() );

            if( null != set ){
                while( set.next() ){
                    collect.setLength(0);

                    String di = set.getString( "CODE" );
                    String ft = set.getString( "ITYPE" );
                    String status = set.getString( "STATUS" );
                    String created = set.getString( "CREATED" );
                    String modified = set.getString( "MODIFIED" );
                    String response = set.getString( "RTYPE" );
                    String success = set.getString( "SUCCESS" );
                    String message = set.getString( "MESSAGE" );

                    collect.append( di ).append( "," )
                           .append( ft ).append( "," )
                           .append( status ).append( "," )
                           .append( created ).append( "," )
                           .append( modified ).append( "," )
                           .append( response ).append( "," )
                           .append( success ).append( "," )
                           .append( message ).append("\r\n");

                   result.add( collect.toString() );
                }
            }
        }catch( Exception ex ){
            log.error( ex );
			ex.printStackTrace();
			throw  new RuntimeException( ex );
        }
        return result;
    }



    private void injectHeaders(StringBuilder builder) {
        if (null == builder)
            return;
        builder.append("DISPATCH_ITEM").append(",").append("FORM_TYPE").append(",").append("STATUS")
                .append(",").append("TASK_CREATED").append(",").append("TASK_MODIFIED").append(",").append("RESPONSE_TYPE")
                .append(",").append("SUCCESS").append(",").append("RESPONSE_MESSAGE").append("\r\n");
    }


    private String query(){
        return "SELECT AC.CODE, AC.ITYPE, TS.STATUS, T.CREATED, T.MODIFIED, TRT.RTYPE, TL.SUCCESS, TL.MESSAGE FROM TASK T JOIN ACTIONS AC ON T.ACTION_ID = AC.ACTION_ID LEFT JOIN TASK_LOG TL ON T.TASK_ID = TL.TASK_ID JOIN TASK_STATUS TS ON T.STATUS = TS.STATUS_ID LEFT JOIN TASK_RESPONSE_TYPE TRT ON TL.RESPONSE_TYPE = TRT.RID";
    }
}
