package com.example.gr33nsn4ck.checkhistorymotorcycle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class BackupDatabaseActivity extends AppCompatActivity {

    String foldername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_database);


        if(checkNetworkState()) {
            if (checkBackupAccountSignin()) {
                Toast.makeText(getApplicationContext(), "ยินดีต้อนรับเข้าสู่ระบบ", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(BackupDatabaseActivity.this, BackupSigninActivity.class));
                BackupDatabaseActivity.this.finish();
                Toast.makeText(getApplicationContext(), "Sign-in Please !", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(BackupDatabaseActivity.this, "กรุณาเชื่อมต่อ Internet !", Toast.LENGTH_SHORT).show();
            BackupDatabaseActivity.this.finish();
        }
    }





    // Upload Zone

    class SendFile2Server extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(BackupDatabaseActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("... กำลังส่งไฟล์ขึ้นสู่ระบบ ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... s) {

            try {
                // step 1 zipfiles
                zipFolder(Environment.getExternalStorageDirectory().toString() + "/history_pictures", Environment.getExternalStorageDirectory().toString() + "/history_pictures.zip");
                zipFolder(Environment.getExternalStorageDirectory().toString() + "/customers_pictures", Environment.getExternalStorageDirectory().toString() + "/customers_pictures.zip");
                zipFolder("/data/data/com.example.gr33nsn4ck.checkhistorymotorcycle/databases", Environment.getExternalStorageDirectory().toString() + "/dbfiles.zip");

                // step 2 send to server
                String response = send2Server();

                return response;
            }catch (Exception e){
                Log.e("BackupDatabaseActivity", "zip file fail");
                e.printStackTrace();
                return "ZipFile Fail !";
            }

        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progressDialog.dismiss();

            Log.e("response : ", String.valueOf(response));
            if(!response.isEmpty()){
                Toast.makeText(getApplicationContext(), "Upload Success !", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Upload Fail !", Toast.LENGTH_SHORT).show();
            }
        } // end task



        void zipFolder(String inputFolderPath, String outZipPath) {

            try {
                FileOutputStream fos = new FileOutputStream(outZipPath);
                ZipOutputStream zos = new ZipOutputStream(fos);

                File srcFile = new File(inputFolderPath);
                File[] files = srcFile.listFiles();
                for (int i = 0; i < files.length; i++) {
                    byte[] buffer = new byte[1024];
                    FileInputStream fis = new FileInputStream(files[i]);
                    zos.putNextEntry(new ZipEntry(files[i].getName()));
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    fis.close();
                }
                zos.close();
            } catch (IOException e) {
                Log.e("ZipFolder", " Zip Folder Fail ! ");
                e.printStackTrace();
            }
        }


        String send2Server() throws IOException {

            String response = null;
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("history_pictures", new FileBody(new File(Environment.getExternalStorageDirectory().toString() + "/history_pictures.zip")));
            entity.addPart("customers_pictures", new FileBody(new File(Environment.getExternalStorageDirectory().toString() + "/customers_pictures.zip")));
            entity.addPart("dbfiles", new FileBody(new File(Environment.getExternalStorageDirectory().toString() + "/dbfiles.zip")));
            entity.addPart("folder_name", new StringBody(foldername));

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://trymycmh.16mb.com/uploadfilesystem.php");
            postRequest.setEntity(entity);

            HttpResponse httpResponse = httpClient.execute(postRequest);
            response = EntityUtils.toString(httpResponse.getEntity());

            return response;
        }

    } // end Upload Zone




    // DownloadHistoryFilesTask

    class DownloadHistoryFilesTask extends AsyncTask<String, Void,String>{

        public ProgressDialog progressDialog = new ProgressDialog(BackupDatabaseActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("... Loading ...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            SharedPreferences spf = getSharedPreferences("backup_preference", Context.MODE_PRIVATE);
            String foldername = spf.getString("foldername", null);

            int count;
            try {
                URL url = new URL("http://trymycmh.16mb.com/Storage/" + foldername.trim() + "/history.zip");
                URLConnection connection = url.openConnection();
                InputStream input = connection.getInputStream();

                Log.e("url : ", String.valueOf(url));

                // Output stream to write file
                File file = new File(Environment.getExternalStorageDirectory().toString(), "bkdown_history.zip");
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];
                while ((count = input.read(data)) != -1) {
                    // writing data to file
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            new DownloadCustomerFilesTask().execute();
        }

    } // End DownloadHistoryFilesTask Zone




    // DownloadCustomerFilesTask

    class DownloadCustomerFilesTask extends AsyncTask<String, Void,String>{

        public ProgressDialog progressDialog = new ProgressDialog(BackupDatabaseActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("... Loading ...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            SharedPreferences spf = getSharedPreferences("backup_preference", Context.MODE_PRIVATE);
            String foldername = spf.getString("foldername", null);

            int count;
            try {
                URL url = new URL("http://trymycmh.16mb.com/Storage/" + foldername.trim() + "/customers.zip");

                Log.e("url : ", String.valueOf(url));

                URLConnection connection = url.openConnection();
                InputStream input = connection.getInputStream();

                // Output stream to write file
                File file = new File(Environment.getExternalStorageDirectory().toString(), "bkdown_customers.zip");
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];
                while ((count = input.read(data)) != -1) {
                    // writing data to file
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            new DownloadDatabaseFilesTask().execute();
        }

    } // End DownloadCustomerFilesTask Zone



    // DownloadDatabaseFilesTask

    class DownloadDatabaseFilesTask extends AsyncTask<String, Void,String>{

        public ProgressDialog progressDialog = new ProgressDialog(BackupDatabaseActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("... Loading ...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            SharedPreferences spf = getSharedPreferences("backup_preference", Context.MODE_PRIVATE);
            String foldername = spf.getString("foldername", null);

            int count;
            try {
                URL url = new URL("http://trymycmh.16mb.com/Storage/" + foldername.trim() + "/dbfiles.zip");

                Log.e("url : ", String.valueOf(url));

                URLConnection connection = url.openConnection();
                InputStream input = connection.getInputStream();

                // Output stream to write file
                File file = new File(Environment.getExternalStorageDirectory().toString(), "bkdown_dbfiles.zip");
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];
                while ((count = input.read(data)) != -1) {
                    // writing data to file
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            new UnzipAllFilesTask().execute();
        }


    } // End DownloadDatabaseFilesTask Zone


    private class UnzipAllFilesTask extends AsyncTask<String,Void,String> {

        public ProgressDialog progressDialog = new ProgressDialog(BackupDatabaseActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("... Loading ...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                unzip(new File(Environment.getExternalStorageDirectory() + "/bkdown_history.zip"), new File(Environment.getExternalStorageDirectory() + "/history_pictures"));
                unzip(new File(Environment.getExternalStorageDirectory() + "/bkdown_customers.zip"), new File(Environment.getExternalStorageDirectory() + "/customers_pictures"));
                unzip(new File(Environment.getExternalStorageDirectory() + "/bkdown_dbfiles.zip"), new File("/data/data/com.example.gr33nsn4ck.checkhistorymotorcycle/databases"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }

    } // End UnzipFileTask Zone






    private boolean checkBackupAccountSignin() {
        SharedPreferences spf = getSharedPreferences("backup_preference", Context.MODE_APPEND);
        boolean state = spf.getBoolean("bk_signin_state", false);
        if (state) {
            Log.e("bk_signin_state", "true");
            return true;
        } else {
            Log.e("bk_signin_state", "false");
            return false;
        }
    }


    private boolean checkNetworkState() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        if (isWifi || is3g) {
            return true;
        } else {
            return false;
        }
    }



    public void btn_backupdb(View view) {
        SharedPreferences preferences = getSharedPreferences("backup_preference", MODE_PRIVATE);
        foldername = preferences.getString("foldername", null);
        new SendFile2Server().execute(foldername);
    }

    public void btn_pullallfiles(View view) {
        new DownloadHistoryFilesTask().execute();
    }

    public void btn_bklogout(View view) {
        SharedPreferences spf = getSharedPreferences("backup_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("bk_signin_state", false);
        editor.commit();

        finish();
        startActivity(getIntent());
    }


}
