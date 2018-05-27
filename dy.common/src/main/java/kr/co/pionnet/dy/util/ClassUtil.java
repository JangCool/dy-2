package kr.co.pionnet.dy.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ClassUtil {

	public static byte[] getByteCode(Class c) {
		if (c == null)
			return null;
		String clsAsResource = "/" + c.getName().replace('.', '/').concat(".class");
		InputStream in = null;
		try {
			in = c.getResourceAsStream(clsAsResource);
			if (in == null) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			byte[] buff = new byte[1024];
			int n = 0;
			while ((n = in.read(buff, 0, 1024)) >= 0) {
				out.write(buff, 0, n);
			}
			return out.toByteArray();
		} catch (Exception e) {
			
		} finally {
			FileUtil.close(in);
		}
		return null;
	}

}

