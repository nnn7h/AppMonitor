package util;

import android.telephony.PhoneNumberUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SmsTool {
    public static byte[] createFakeSms(String sender,
                                       String body) {
        byte[] pdu = null;
        byte[] scBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD("0000000000");
        byte[] senderBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD(sender);
        int lsmcs = scBytes.length;
        // 时间处理，包括年月日时分秒以及时区和夏令时
        byte[] dateBytes = new byte[7];
        Calendar calendar = new GregorianCalendar();
        dateBytes[0] = reverseByte((byte) (calendar.get(Calendar.YEAR)));
        dateBytes[1] = reverseByte((byte) (calendar.get(Calendar.MONTH) + 1));
        dateBytes[2] = reverseByte((byte) (calendar.get(Calendar.DAY_OF_MONTH)));
        dateBytes[3] = reverseByte((byte) (calendar.get(Calendar.HOUR_OF_DAY)));
        dateBytes[4] = reverseByte((byte) (calendar.get(Calendar.MINUTE)));
        dateBytes[5] = reverseByte((byte) (calendar.get(Calendar.SECOND)));
        dateBytes[6] = reverseByte((byte) ((calendar.get(Calendar.ZONE_OFFSET) + calendar
                .get(Calendar.DST_OFFSET)) / (60 * 1000 * 15)));
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            bo.write(lsmcs);// 短信服务中心长度
            bo.write(scBytes);// 短信服务中心号码
            bo.write(0x04);
            bo.write((byte) sender.length());// 发送方号码长度
            bo.write(senderBytes);// 发送方号码
            bo.write(0x00);// 协议标示，00为普通GSM，点对点方式

            try {
                String sReflectedClassName = "com.android.internal.telephony.GsmAlphabet";
                Class cReflectedNFCExtras = Class.forName(sReflectedClassName);
                Method stringToGsm7BitPacked = cReflectedNFCExtras.getMethod(
                        "stringToGsm7BitPacked", new Class[]{String.class});
                stringToGsm7BitPacked.setAccessible(true);
                byte[] bodybytes = (byte[]) stringToGsm7BitPacked.invoke(null,
                        body);
                bo.write(0x00); // encoding: 0 for default 7bit
                bo.write(dateBytes);
                bo.write(bodybytes);
            } catch (Exception e) {
                try {
                    // try UCS-2
                    byte[] bodybytes = encodeUCS2(body, null);
                    bo.write(0x08); // encoding: 0x08 (GSM_UCS2) for UCS-2
                    bo.write(dateBytes);
                    bo.write(bodybytes);
                } catch(UnsupportedEncodingException uex) {
                }
            }
            pdu = bo.toByteArray();
        } catch (IOException e) {
        }
        return pdu;
    }

    /**
     * Packs header and UCS-2 encoded message. Includes TP-UDL & TP-UDHL if
     * necessary
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] encodeUCS2(String message, byte[] header)
            throws UnsupportedEncodingException {
        byte[] userData, textPart;
        textPart = message.getBytes("utf-16be");

        if (header != null) {
            // Need 1 byte for UDHL
            userData = new byte[header.length + textPart.length + 1];

            userData[0] = (byte) header.length;
            System.arraycopy(header, 0, userData, 1, header.length);
            System.arraycopy(textPart, 0, userData, header.length + 1,
                    textPart.length);
        } else {
            userData = textPart;
        }
        byte[] ret = new byte[userData.length + 1];
        ret[0] = (byte) (userData.length & 0xff);
        System.arraycopy(userData, 0, ret, 1, userData.length);
        return ret;
    }

    private static byte reverseByte(byte b) {
        return (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
    }
}
