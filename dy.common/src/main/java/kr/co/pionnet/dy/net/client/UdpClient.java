package kr.co.pionnet.dy.net.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import kr.co.pionnet.dependency.io.netty.buffer.ByteBuf;
import kr.co.pionnet.dependency.io.netty.buffer.Unpooled;
import kr.co.pionnet.dependency.io.netty.channel.Channel;
import kr.co.pionnet.dependency.io.netty.channel.socket.DatagramPacket;
import kr.co.pionnet.dy.net.NetConst;
import kr.co.pionnet.dy.type.DataType;
import kr.co.pionnet.dy.util.IdGen;
import kr.co.pionnet.dy.vo.pack.Pack;
import kr.co.pionnet.dy.vo.pack.TransXviewPack;

public class UdpClient extends BaseClient implements Client{
	
	
	private int packetSize = 60000;
	private Channel channel;
	private InetSocketAddress remote;
	
	public UdpClient() {}

	public UdpClient(String ip, int port) {
		super(ip,port);
		setClient();
	}
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {		
		this.channel = channel;
	}
	
	public int getPacketSize() {
		return packetSize;
	}

	public void setPacketSize(int packetSize) {
		this.packetSize = packetSize;
	}

	public void setClient() {
		
		try {
			this.remote = new InetSocketAddress(super.getIp(), super.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public InetSocketAddress getRemote() {
		return remote;
	}

	public void setRemote(InetSocketAddress remote) {
		this.remote = remote;
	}

	@Override
	public boolean send(byte[] bytes) {	
		return send(false, NetDataType.NORMAL, bytes);
	}
	
	
	@Override
	public boolean send(NetDataType netDataType, byte[] bytes) {
		return send(false, netDataType, bytes);
	}
	
	private boolean send(boolean isMtu, NetDataType netDataType, byte[] bytes) {
		
		boolean result  = false;
		
		if(channel == null) {
			return false;
		}
		
		
		if(!isMtu && bytes.length > packetSize) {
			return sendMTU(netDataType,bytes);
		}
		
		try {

			BytePack packer = new BytePack();

			//mtu 일 경우에는 sendMTU 에서 이미 Send type 과 data type을 설정 하여 보내기 때문에 mtu가 아닐 경우에만 설정 한다.
			if(!isMtu) {
			
				packer.writeByte(NetConst.SEND_TYPE_NORNAL);
				
				switch (netDataType) {
				case NORMAL:
					packer.writeByte(NetConst.SEND_DATA_TYPE_NORNAL);
					break;
				case PACK:
					packer.writeByte(NetConst.SEND_DATA_TYPE_PACK);
					break;
				case JSON:
					packer.writeByte(NetConst.SEND_DATA_TYPE_JSON);
					break;
				default:
					packer.writeByte(NetConst.SEND_DATA_TYPE_NORNAL);
					break;
				}
				packer.writeInt(bytes.length);		
			}
			packer.write(bytes);
			packer.close();
		
			byte[] arrayBytes = packer.toByteArray();
			
							
			ByteBuf buf = Unpooled.wrappedBuffer(arrayBytes);
			DatagramPacket packet = new DatagramPacket(buf, remote);		
			channel.writeAndFlush(packet);		

			result = true;
			
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}	
		
		return result;
	}

	@Override
	public boolean send(String msg) {
		
		byte[] bytes = msg.getBytes();
		
		if(bytes.length > packetSize) {
			return sendMTU(bytes);
		}

		BytePack packer = new BytePack();
		
		try {
			packer.writeByte(NetConst.SEND_TYPE_NORNAL);
			packer.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		packer.close();
		
		return send(packer.toByteArray());
	}
	
	@Override
	public boolean sendPACK(byte[] bytes) {
		return send(NetDataType.PACK, bytes);
	}
	
	@Override
	public boolean sendJSON(byte[] bytes) {
		return send(NetDataType.JSON, bytes);
	}

	@Override
	public boolean sendMTU(String msg) {
		return sendMTU(msg.getBytes());
	}

	@Override
	public boolean sendMTU(byte[] bytes) {
		return sendMTU(NetDataType.NORMAL ,bytes);
	}
	
	@Override
	public boolean sendMTU(NetDataType netDataType,byte[] bytes) {
		
		boolean result = true;
		
		int total = (int) Math.ceil((double)bytes.length / packetSize);
		int remainder = bytes.length % packetSize;
		
		long mtuId = IdGen.next();
		

		try {
						
			for (int i = 0; i < total; i++) {

				BytePack packer = new BytePack();
				
				packer.writeByte(NetConst.SEND_TYPE_MTU);
				
				switch (netDataType) {
				case NORMAL:
					packer.writeByte(NetConst.SEND_DATA_TYPE_NORNAL);
					break;
				case PACK:
					packer.writeByte(NetConst.SEND_DATA_TYPE_PACK);
					break;
				case JSON:
					packer.writeByte(NetConst.SEND_DATA_TYPE_JSON);
					break;
				default:
					packer.writeByte(NetConst.SEND_DATA_TYPE_NORNAL);
					break;
				}
				
				packer.writeLong(mtuId);
				packer.writeInt(total);
				packer.writeInt(i);
				
				boolean isFinal = ((i+1) == total);

				if(!isFinal) {
					packer.writeInt(packetSize);

					byte[] out = new byte[packetSize];
					System.arraycopy(bytes, i * packetSize , out, 0, packetSize);
					packer.write(out);
					
				} else {
					
					if(remainder == 0) {
						remainder = packetSize;
					}
					packer.writeInt(remainder);

					byte[] out = new byte[remainder];
					System.arraycopy(bytes, bytes.length - remainder, out, 0, remainder);
					packer.write(out);
				}
				
				packer.close();
								
				if(!send(true,netDataType, packer.toByteArray())) {
					result = false;

					break;
				}
				
			}

			result = true;
			
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	@Override
	public boolean sendMTU(Object object) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void close() {
		if(!isClose()) {			
			try {
				channel.close();				
				setClose(true);
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	class BytePack implements DataOutput{
		
		private DataOutputStream dos;
		private ByteArrayOutputStream baos;
		
		
		public BytePack() {
			baos = new ByteArrayOutputStream();			
			dos = new DataOutputStream(baos);
		}

		@Override
		public void write(int b) throws IOException {
			dos.write(b);			
		}

		@Override
		public void write(byte[] b) throws IOException {
			dos.write(b);						
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			dos.write(b, off, len);
		}

		@Override
		public void writeBoolean(boolean v) throws IOException {
			dos.writeBoolean(v);
		}

		@Override
		public void writeByte(int v) throws IOException {
			dos.writeByte(v);
		}

		@Override
		public void writeShort(int v) throws IOException {
			dos.writeShort(v);
		}

		@Override
		public void writeChar(int v) throws IOException {
			dos.writeChar(v);			
		}

		@Override
		public void writeInt(int v) throws IOException {
			dos.writeInt(v);
		}

		@Override
		public void writeLong(long v) throws IOException {
			dos.writeLong(v);
		}

		@Override
		public void writeFloat(float v) throws IOException {
			dos.writeFloat(v);
		}

		@Override
		public void writeDouble(double v) throws IOException {
			dos.writeDouble(v);			
		}

		@Override
		public void writeBytes(String s) throws IOException {
			dos.writeBytes(s);						
		}

		@Override
		public void writeChars(String s) throws IOException {
			dos.writeChars(s);									
		}

		@Override
		public void writeUTF(String s) throws IOException {
			dos.writeUTF(s);												
		}
		
		public byte[] toByteArray() {
			return baos.toByteArray();
		}
		
		public void close() {
			try {
				dos.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	public static void main(String[] args) {
		byte[] b= null;
		try {
			b = "abcdefghijklmnopqrstu가나다라마바사아자차카타파하햏햏".getBytes("UTF-8");
			
			TransXviewPack xviewpack = new TransXviewPack();

			xviewpack.dataType = DataType.TRACKER;
			xviewpack.txid = -4036160422503608319L;			
			xviewpack.serv_h=28171712;
			xviewpack.uagent_h=-1171722;
			xviewpack.qst_h=-9817172;
			xviewpack.aid="a15";
			xviewpack.sql_t=166;
			xviewpack.sql_c=50;
			xviewpack.fetch_c = 1234;
			xviewpack.cpu_t =  1466;
			xviewpack.l_ip="127.0.0.1";
			xviewpack.r_ip = "121.169.21.115";
			xviewpack.method = "GET";
			xviewpack.ref_h = 18171722;
			xviewpack.eTime = System.currentTimeMillis();
			xviewpack.elapsed = 1234;
			xviewpack.gid = 1;	
			xviewpack.e_type  = 1;
			xviewpack.e = -1;
			xviewpack.e_cls = 1;
			xviewpack.uid = 1823;
			
			b  = xviewpack.toByteArray();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int packetSize = 8;
		
		int total = (int) Math.ceil((double)b.length / packetSize);
		int remainder = b.length % packetSize;
		
		byte[] appendStr = new byte[b.length];
		
		for (int i = 0; i < total; i++) {

			
			boolean isFinal = ((i+1) == total);

			if(!isFinal) {
//				byte[] out = new byte[packetSize];
				
				System.arraycopy(b, i * packetSize , appendStr, i * packetSize, packetSize);
//				System.out.println(new String(out));
				
			} else {
				byte[] out = new byte[remainder];
				System.arraycopy(b, b.length - remainder, appendStr, b.length - remainder, remainder);
				
			}

			
		}


		
		
		Pack p = new TransXviewPack().unPack(appendStr);
		TransXviewPack txp = (TransXviewPack)p;
		
		System.out.println(txp.aid);
		System.out.println("byte dataType => " + p.dataType);
		System.out.println("byte txid => " + txp.txid);
		System.out.println("byte serv_h => " + txp.serv_h);
		System.out.println("byte uagent_h => " + txp.uagent_h);
		System.out.println("byte qst_h => " + txp.qst_h);
		System.out.println("byte aid => " + txp.aid);
//		System.out.println(Math.ceil(((double)46 / packetSize)));
	}

	
	

	

}
