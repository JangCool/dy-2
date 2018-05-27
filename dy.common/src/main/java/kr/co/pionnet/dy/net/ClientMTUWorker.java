package kr.co.pionnet.dy.net;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientMTUWorker extends Thread{

	private static ClientMTUWorker cmw;
	
	private Map<Long,MTUPack> mtuPacket = new ConcurrentHashMap<Long,MTUPack>();
	
	private long THREAD_TIME = 2000;
	private long LIMIT_TIME = 5000;
		
	public static synchronized ClientMTUWorker getInstance() {
		
		if(cmw == null ) {
			cmw = new ClientMTUWorker();
			cmw.setDaemon(true);
			cmw.setName("Client MTU Worker");
			cmw.start();
		}
		
		return cmw;
	}
	
	
	@Override
	public void run() {
	
		while(true) {

			synchronized (this) {

				for (Long key : mtuPacket.keySet()) {
					
					MTUPack mtuPack = mtuPacket.get(key);
					long elapsed =System.currentTimeMillis()-mtuPack.time;

					if(elapsed > LIMIT_TIME) {
						mtuPacket.remove(key);
					}
					
				}
				
			}
			
			try {
				Thread.sleep(THREAD_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	
	}
	
	/**
	 * packet이 MTU로 쪼개서 들어오는 것인지 확인
	 * 
	 * @param b client에서 mtu 값 만큼 전송 된  packet byte[]
	 * @return boolean
	 */
	public boolean isMtuPacket(byte[] b) {
	
		if(b != null && b[0] == 0x01) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * agent에서 packet이 나누어 전송된 경우 해당 메소드를 통하여 결합 시켜준다.
	 * 온전히 packet이 다 전송 되어 하나로 합쳐질 경우에는 byte[] 값을 아닐 경우에는 null를 리턴 한다.
	 * 
	 * @param b client에서 mtu 값 만큼 전송 된  packet byte[]
	 * @return byte[]
	 */
	public NetData merge(long mtuId,int total,int index,int payLoadSize,byte[] packet) {
		NetData netData = new NetData();
			
		if(!mtuPacket.containsKey(mtuId)) {

			setMtuPack(mtuId, total, index, payLoadSize, packet);
			return null;
		}
		
		
		MTUPack mtuPack = mtuPacket.get(mtuId);
		
		if(mtuPack != null) {

			mtuPack.payLoadSize += payLoadSize;
			
			MTUElement mtuElem = new MTUElement();
			mtuElem.index = index;
			mtuElem.bytes = packet;
			
			mtuPack.elem.add(mtuElem);
			
			//검증
			if(mtuPack.total == mtuPack.elem.size()) {
								
				//index순으로 오름차순 정렬
				Collections.sort(mtuPack.elem, new Comparator<Object>() {

					@Override
					public int compare(Object o1, Object o2) {
						
				        int firstValue = 0;
				        int secondValue = 0;

				        firstValue = (int) ((MTUElement)o1).index;
				        secondValue = (int)((MTUElement)o2).index;
				        
				        // 내림차순 정렬
				        if (firstValue < secondValue) {
				            return -1;
				        } else if (firstValue > secondValue) {
				            return 1;
				        } else /* if (firstValue == secondValue) */ {
				            return 0;
				        }
					}
				});
				
				
				int elementSize = mtuPack.elem.size();
				int destPos = 0;
				byte[] out = new byte[mtuPack.payLoadSize];
				for (int i = 0; i < elementSize; i++) {
					
					MTUElement element = mtuPack.elem.get(i);
					System.arraycopy(element.bytes, 0, out, destPos, element.bytes.length);
					
					destPos += element.bytes.length;
				}
									
				netData.setBytes(out);

				
				return netData;
			}
			
		}

		
		
		return null;
	}
	
	
	
	private void setMtuPack(long mtuId,int total, int index, int payLoadSize,byte[] packet) {
		
		MTUPack mtuPack = new MTUPack();
		mtuPack.id = mtuId;
		mtuPack.total = total;
		mtuPack.payLoadSize += payLoadSize;
		mtuPack.time = System.currentTimeMillis();

		MTUElement mtuElem = new MTUElement();
		mtuElem.index = index;
		mtuElem.bytes = packet;
		
		mtuPack.elem.add(mtuElem);

		mtuPacket.put(mtuId, mtuPack);
	}
	
	
	
	public class MTUPack{
		
		long id;
		int total;
		int payLoadSize;
		long time;
		
		List<MTUElement> elem = new ArrayList<MTUElement>();
	}
	
	public class MTUElement{
		int index;
		byte[] bytes;		
	}
	
	class ByteUnpack implements DataInput{
		
		private DataInputStream dis;
		private ByteArrayInputStream bais;
		
		
		public ByteUnpack(byte[] b) {
			bais = new ByteArrayInputStream(b);	
			dis = new DataInputStream(bais);
		}


		@Override
		public void readFully(byte[] b) throws IOException {
			dis.readFully(b);
		}

		@Override
		public void readFully(byte[] b, int off, int len) throws IOException {
			dis.readFully(b, off, len);
		}

		@Override
		public int skipBytes(int n) throws IOException {
			return dis.skipBytes(n);
		}

		@Override
		public boolean readBoolean() throws IOException {
			return dis.readBoolean();
		}

		@Override
		public byte readByte() throws IOException {
			return dis.readByte();
		}

		@Override
		public int readUnsignedByte() throws IOException {
			return dis.readUnsignedByte();
		}

		@Override
		public short readShort() throws IOException {
			return dis.readShort();
		}

		@Override
		public int readUnsignedShort() throws IOException {
			return dis.readUnsignedShort();
		}

		@Override
		public char readChar() throws IOException {
			return dis.readChar();
		}

		@Override
		public int readInt() throws IOException {
			return dis.readInt();
		}

		@Override
		public long readLong() throws IOException {
			return dis.readLong();
		}

		@Override
		public float readFloat() throws IOException {
			return dis.readFloat();
		}

		@Override
		public double readDouble() throws IOException {
			return dis.readDouble();
		}

		@Override
		public String readLine() throws IOException {
			return dis.readLine();
		}

		@Override
		public String readUTF() throws IOException {
			return dis.readUTF();
		}
		
		public int available() {
			return bais.available();
		}
		
		public void close() {
			try {
				dis.close();
				bais.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {
		
		byte[] src = { 0x01, 0x02, 0x03, 0x04, 0x05,0x06,0x07,0x0F };
		byte[] dest = new byte[8];
		
		System.arraycopy(src, 2, dest, 0, 6);
		
		
		for (byte b : dest) {
			System.out.println(b);

		}
		
		System.out.println(dest);
	}
	
}
