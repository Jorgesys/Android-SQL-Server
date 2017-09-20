package com.jorgesys.sqlserverconnection;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {

    /*
    For more information to use JDBC Drivers for Microsoft SQL Server go to:
    https://www.dbvis.com/doc/sqlserver-database-drivers/
     */

    private static final String TAG = "MainActivity";
    private static final int MICROSOFT = 0, JTDS = 1;
    private String id_access = "";
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Set the option MICROSOFT or JTDS.

        findViewById(R.id.btnConnection1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeQuery(MICROSOFT);
            }
        });

        findViewById(R.id.btnConnection2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeQuery(JTDS);
            }
        });



    }


    public Connection connectionBD(int driver) {
        Connection conexion = null;
        try{

            /* Very important: Never disable this policy in production! */
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /* Never disable this policy in production! */

            switch (driver) {
                case MICROSOFT:
                    //To use this driver download sqljdbc.jar to /libs folder.
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                conexion = DriverManager.getConnection("jdbc:sqlserver://10.1.2.138:80;databasename=ENTERPRISE;user=jorgesys;password=Cot0t1ta!;");
                Log.i(TAG, "Succesfully connected using \"com.microsoft.sqlserver.jdbc.SQLServerDriver\"!");
                    break;
                case JTDS:
                    //To use this driver download jtds.jar to /libs folder.
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                conexion = DriverManager.getConnection("jdbc:jtds:sqlserver://10.1.2.138:80;databasename=ENTERPRISE;user=jorgesys;password=Cot0t1ta!;");
                Log.i(TAG, "Succesfully connected using \"net.sourceforge.jtds.jdbc.Driver\"!");
                break;
                default:
                    break;
            }

        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return conexion;
    }

    public void makeQuery(int driver) {
        try {
            Connection conn = connectionBD(driver);
            if(conn != null) {
                PreparedStatement pat = conn.prepareStatement("select top 10 * from Employees");
                pat.executeUpdate();
                ResultSet rs = pat.executeQuery();
                if (rs.next()) {
                    id_access = rs.getString(1);
                    email = rs.getString(7);
                    Log.i(TAG, "id_access: " + id_access + " , email: " + email);
                }
                //Closing connection
                pat.close();
                Toast.makeText(getApplicationContext(), "Query executed succesfully", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
        }
    }

}
