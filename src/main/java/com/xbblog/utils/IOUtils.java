package com.xbblog.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    public static byte[] InputStreamToByte(InputStream iStrm) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = iStrm.read()) != -1)
        {
            bytestream.write(ch);
        }
        byte imgdata[]=bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

}
