package com.ruyicai.charge.dna.v2.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
*
* @author: XieminQuan
* @time  : 2007-11-20 ����04:10:22
*
* DNAPAY
*/

public class Util {

	private static char byte2Char(byte no) {

		char[] table = {
				0x00+'0',
				0x01+'0',
				0x02+'0',
				0x03+'0',
				0x04+'0',
				0x05+'0',
				0x06+'0',
				0x07+'0',
				0x08+'0',
				0x09+'0',
				0x00+'A',
				0x01+'A',
				0x02+'A',
				0x03+'A',
				0x04+'A',
				0x05+'A'
		};
		return table[no];
	}
	private static byte byte2OByte(byte c) {

		if(c >= '0' && c <= '9') {
			c = (byte)(c - '0');
		} else if(c >= 'a' && c <= 'z') {
			c = (byte)(c - 'a' + 0x0A);
		} else if(c >= 'A' && c <= 'Z') {
			c = (byte)(c - 'A' + 0x0A);
		}

		return c;
	}

	public static boolean isNumber(String num) {
		
		byte[] temp = num.getBytes();
		
		for(int i = 0;i < temp.length;i++) {
			byte a = temp[i];
			if(a < '0' || a > '9') return false;
		}
		return true;
	}
	
	/**
	 * 
	 * �ַ���תʮ����byte�����룺98ABC�������00001001 00001000 00001010 00001011 00001100
	 * ����ı�����0-9 a-f A-F��22������������ַ���תʮ���ƺ���ֽ�����
	 */
	public static byte[] str2OBytes(String str) {

		byte[] result = str.getBytes();

		for(int i = 0;i < result.length;i++) {

			result[i] = byte2OByte(result[i]);
		}
		return result;
	}

	/**
	 * 
	 * bcd strתbytes,���룺86AE�������10000110 10011110
	 * ����ı�����0-9 a-f A-F��22�����������BCD�ַ���ת�ֽڲ��ϲ�����ֽ�����
	 */
	public static byte[] bcdStr2Bytes(String bcd,boolean leftAdd0) {

		if(leftAdd0) {
			while(bcd.length()%2 != 0) bcd = "0" + bcd;
		} else {
			while(bcd.length()%2 != 0) bcd += "0";
		}

		byte[] temp = bcd.getBytes();
		byte[] result = new byte[temp.length/2];

		for(int i = 0;i < result.length;i++) {

			byte h = byte2OByte(temp[2*i]);
			byte l = byte2OByte(temp[2*i+1]);

			result[i] = (byte)((h << 4) + l);
		}
		return result;
	}

	/**
	 * 
	 * bytesתBCD�������룺01001100 00011000(bytes[0]=01001100,bytes[1]=00011000)�������4C18
	 */
	public static String bcdBytes2Str(byte[] bytes) {
		return bcdBytes2Str(bytes,false,false);
	}
	/**
	 * 
	 * bytesתBCD�������룺01001100 00011000(bytes[0]=01001100,bytes[1]=00011000)�������4C18
	 */
	public static String bcdBytes2Str(byte[] bytes,boolean cut,boolean cutLeft) {

		StringBuffer temp = new StringBuffer(bytes.length * 2); 

		for(int i = 0;i < bytes.length;i++) {

			byte h = (byte)((bytes[i]&0xf0) >>> 4);
			byte l = (byte)(bytes[i]&0x0f);

			temp.append(byte2Char(h)).append(byte2Char(l));
		}

		return cut?(cutLeft?temp.toString().substring(1):temp.toString().substring(0,temp.length()-1)):temp.toString();
	}

	/**
	 * 
	 *������ת�����ƴ������룺00010000 00000001(bytes[0]=00010000,bytes[1]=00000001)�������0001000000000001
	 */
	public static String bin2BinStr(byte[] bytes) {

		StringBuffer temp = new StringBuffer(bytes.length*8); 

		for(int i = 0;i < bytes.length;i++) {

			temp.append((byte)((bytes[i]&0x80) >>> 7));
			temp.append((byte)((bytes[i]&0x40) >>> 6));
			temp.append((byte)((bytes[i]&0x20) >>> 5));
			temp.append((byte)((bytes[i]&0x10) >>> 4));
			temp.append((byte)((bytes[i]&0x08) >>> 3));
			temp.append((byte)((bytes[i]&0x04) >>> 2));
			temp.append((byte)((bytes[i]&0x02) >>> 1));
			temp.append((byte)((bytes[i]&0x01)));
		}

		return temp.toString();
	}

	public static byte[] str2Bcd(String str) {
		return str2Bcd(str,true);
	}
	/**
	 * 
	 * 10���ƴ�תΪBCD��(�ֽ�����)�����룺0123456789�������00000001 00100011 01000101 01100111 10001001
	 * ����ı�����0-9 a-f A-F��22���ַ�
	 * ��������󲹡�0���������Ҳ���F��
	 */
	public static byte[] str2Bcd(String str,boolean leftAdd0) {

		if(leftAdd0) {
			while(str.length()%2 != 0) str = "0" + str;
		} else {
			while(str.length()%2 != 0) str += "F";
		}

		byte[] temp = str.getBytes();
		byte[] result = new byte[temp.length/2];

		for(int i = 0;i < result.length;i++) {

			byte h = byte2OByte(temp[2*i]);
			byte l = byte2OByte(temp[2*i+1]);

			result[i] = (byte)((h << 4) + l);
		}

		return result;
	}

	/**
	 * 
	 * �����ƴ�ת������(�ֽ�����)�����룺00110100 01110001 11000001(24�ַ���)�������00110100 01110001 11000001(3�ֽ�)
	 * ������ַ���ֻ�ܰ���0��1
	 */
	public static byte[] binStr2Bin(String str) {

		byte[] temp = str.getBytes();
		if(str.length() == 0) throw new RuntimeException("�ַ�������Ϊ�գ�ת��������ʧ��");
		if(temp.length%8 != 0) throw new RuntimeException("�ַ���'"+str+"'���Ȳ���8�ı�����ת��������ʧ��");
		for(int i= 0;i < temp.length;i++) {
			if((temp[i]-'0') < 0 || (temp[i]-'0') > 1) throw new RuntimeException("�ַ���'"+str+"'����'0'��'1'������ַ���ת��������ʧ��");
		}

		byte[] result = new byte[temp.length/8];

		int a0,a1,a2,a3,a4,a5,a6,a7;

		for(int i = 0;i < str.length()/8;i++) {

			a0 = temp[8*i] - '0';
			a1 = temp[8*i + 1] - '0';
			a2 = temp[8*i + 2] - '0';
			a3 = temp[8*i + 3] - '0';
			a4 = temp[8*i + 4] - '0';
			a5 = temp[8*i + 5] - '0';
			a6 = temp[8*i + 6] - '0';
			a7 = temp[8*i + 7] - '0';

			result[i] = (byte)((a0<<7) + (a1<<6) + (a2<<5) + (a3<<4) + (a4<<3) + (a5<<2) + (a6<<1) + a7);
		}

		return result;
	}

	/**
	 * ���룺1001(4bits)�������00000001 00000000 00000000 00000001(4bytes)
	 */
	public static byte[] binBytes2AscBytes(byte[] bin) {
		
		byte[] result = new byte[bin.length*8];
		
		for(int i = 0;i < bin.length;i++) {
			
			result[8*i]     = (byte)((bin[i]&0x80) >>> 7);
			result[8*i + 1] = (byte)((bin[i]&0x40) >>> 6);
			result[8*i + 2] = (byte)((bin[i]&0x20) >>> 5);
			result[8*i + 3] = (byte)((bin[i]&0x10) >>> 4);
			result[8*i + 4] = (byte)((bin[i]&0x08) >>> 3);
			result[8*i + 5] = (byte)((bin[i]&0x04) >>> 2);
			result[8*i + 6] = (byte)((bin[i]&0x02) >>> 1);
			result[8*i + 7] = (byte)((bin[i]&0x01));
		}
		
		return result;
	}
	/**
	 * ���룺00000001 00000000 00000000 00000001(4bytes)�������1001(4bits)
	 */
	public static byte[] ascBytes2BinBytes(byte[] asc) {
		
		byte[] result = new byte[asc.length/8];
		
		int a0,a1,a2,a3,a4,a5,a6,a7;

		for(int i=0;i < asc.length/8;i++) {
			
			a0 = asc[8*i];
			a1 = asc[8*i + 1];
			a2 = asc[8*i + 2];
			a3 = asc[8*i + 3];
			a4 = asc[8*i + 4];
			a5 = asc[8*i + 5];
			a6 = asc[8*i + 6];
			a7 = asc[8*i + 7];

			result[i] = (byte)((a0<<7) + (a1<<6) + (a2<<5) + (a3<<4) + (a4<<3) + (a5<<2) + (a6<<1) + a7);
		}
		
		return result;
	}
	
	/**
	 * ���룺95BE5779 04DC9CF7�������39 35 42 45 35 37 37 39  30 34 44 43 39 43 46 37
	 */
	public static byte[] bcdBytes2AscBytes(byte[] bcd) {

		byte[] result = new byte[bcd.length*2]; 

		for(int i = 0;i < bcd.length;i++) {

			byte h = (byte)((bcd[i]&0xf0) >>> 4);
			byte l = (byte)(bcd[i]&0x0f);

			result[2*i] = (byte)byte2Char(h);
			result[2*i + 1] = (byte)byte2Char(l);
		}

		return result;
	}
	
	/**
	 * ����ת2���ƣ��ҿ�����'0'(bit)
	 * ���룺60��2 �����00000000 00111100 (0x00,0x3C)
	 */
	public static byte[] int2BcdBytes(int num,int length) {
		
		String hex = Integer.toBinaryString(num);
		
		try {
			hex = StringFormatter.format(hex,length*8-hex.length(),length*8,"0","0",true);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		return binStr2Bin(hex);
	}

	
	/**
	 * ���룺00000000 00111100 (0x00,0x3C)  �����60
	 */
	public static int bcdBytes2Int(byte[] bcd) {
		
		return Integer.valueOf(Util.bcdBytes2Str(bcd),16);
	}
	/**
	 * �����ֽ����飬���ظ��ֽ�������ַ���
	 */
	public static String getBcdString(byte[] src,int srcPos,int length) {
		return getBcdString(src,srcPos,0,length,false,false);
	}
	public static String getBcdString(byte[] src,int srcPos,int destPos,int length,boolean cut,boolean cutLeft) {
		
		byte[] temp = new byte[length];
		System.arraycopy(src,srcPos,temp,destPos,length);
		return bcdBytes2Str(temp,cut,cutLeft);
	}


	public static String getAscString(byte[] src,int srcPos,int length) {
		return getAscString(src,srcPos,0,length,false,false);
	}
	public static String getAscString(byte[] src,int srcPos,int destPos,int length,boolean cut,boolean cutLeft) {
		
		byte[] temp = new byte[length];
		System.arraycopy(src,srcPos,temp,destPos,length);
		String result = "";
		try {
			result = new String(temp,"ASCII");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static String getString(byte[] src,int srcPos,int length) {
		byte[] temp = new byte[length];
		System.arraycopy(src,srcPos,temp,0,length);
		return new String(temp);
	}
	
	public static String round(String amt) {
		
		Double amount = Double.parseDouble(amt);
		
		int a = (int)Math.round(amount*100);
		
		double b = (double)a/100.00;
		if(b <= 0.0) b = 0.01;
		
		return String.valueOf(b);
	}
    public static String getElementValue(String elemName, Document doc) {
        String elemValue = "";
        if (null != doc) {
            Element elem = null;
            elem = (Element) doc.getElementsByTagName(elemName).item(0);
            if (null != elem && null != elem.getFirstChild()) {
                elemValue = elem.getFirstChild().getNodeValue();
            }
        }
        return elemValue;
    }
}