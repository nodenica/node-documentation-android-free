package com.mc.nad.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnpackZip {
    private static final String TAG = "UnpackZip";

    public boolean execute(File filePath, InputStream is) {
        if (!filePath.exists()) {
            if (filePath.mkdirs()) {
                Log.d(TAG, "Successfully created the parent dir:" + filePath.getName());
            } else {
                Log.d(TAG, "Failed to create the parent dir:" + filePath.getName());
            }
        }


        String filePathString = filePath.toString() + File.separator;

        ZipInputStream zipInputStream;
        try
        {
            String filename;
            zipInputStream = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry zipEntry;
            byte[] buffer = new byte[1024];
            int count;

            while ((zipEntry = zipInputStream.getNextEntry()) != null)
            {
                filename = zipEntry.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (zipEntry.isDirectory()) {
                    File file = new File(filePathString + filename);
                    file.mkdirs();
                    continue;
                }

                FileOutputStream fileOutputStream = new FileOutputStream(filePathString + filename);

                // cteni zipu a zapis
                while ((count = zipInputStream.read(buffer)) != -1)
                {
                    fileOutputStream.write(buffer, 0, count);
                }

                fileOutputStream.close();
                zipInputStream.closeEntry();
            }

            zipInputStream.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}