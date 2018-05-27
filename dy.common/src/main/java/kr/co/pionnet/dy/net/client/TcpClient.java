package kr.co.pionnet.dy.net.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import kr.co.pionnet.dy.net.NetConst;
import kr.co.pionnet.dy.util.IdGen;

public class TcpClient extends BaseClient{

	private int packetSize = 5;
	private Socket socket;
	
	public TcpClient() {}
	
	public TcpClient(String ip, int port) {
		super(ip,port);
		setClient();
	}

	@Override
	void setClient() {
		
		try {
//			setIpInet(InetAddress.getByName(getIp()));
			this.socket = new Socket(getIp(),getPort());

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public Socket getSocket() {
		return socket;
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

		if(socket == null) {
			return false;
		}
				
//		if(!isMtu && bytes.length > packetSize) {
//			return sendMTU(netDataType,bytes);
//		}
//		
		ByteArrayInputStream bos = null;
		
		try {
			
			MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
			packer.packByte(NetConst.SEND_TYPE_NORNAL);
			
			switch (netDataType) {
			case NORMAL:
				packer.packByte(NetConst.SEND_DATA_TYPE_NORNAL);
				break;
			case PACK:
				packer.packByte(NetConst.SEND_DATA_TYPE_PACK);
				break;
			case JSON:
				packer.packByte(NetConst.SEND_DATA_TYPE_JSON);
				break;
			default:
				packer.packByte(NetConst.SEND_DATA_TYPE_NORNAL);
				break;
			}

			packer.packInt(bytes.length);
			packer.addPayload(bytes);
			packer.close();

			getSocket().getOutputStream().write(packer.toByteArray());

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
		
//		if(bytes.length > packetSize) {
//			return sendMTU(bytes);
//		}

		MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
		
		try {
			packer.packByte(NetConst.SEND_TYPE_NORNAL);
			packer.packString(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		packer.clear();
		
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
	public boolean sendMTU(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendMTU(NetDataType netDataType,byte[] bytes) {
		
		boolean result = true;
		
		int total = (int) Math.round((double)bytes.length / packetSize);
		int remainder = bytes.length % packetSize;
		
		long mtuId = IdGen.next();
		
		try {
			
			for (int i = 0; i < total; i++) {

				MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
				
				packer.packByte(NetConst.SEND_TYPE_MTU);
				
				switch (netDataType) {
				case NORMAL:
					packer.packByte(NetConst.SEND_DATA_TYPE_NORNAL);
					break;
				case PACK:
					packer.packByte(NetConst.SEND_DATA_TYPE_PACK);
					break;
				case JSON:
					packer.packByte(NetConst.SEND_DATA_TYPE_JSON);
					break;
				default:
					packer.packByte(NetConst.SEND_DATA_TYPE_NORNAL);
					break;
				}
				
				System.out.println(mtuId);
				packer.packLong(mtuId);
				packer.packInt(total);
				packer.packInt(i);
				
				boolean isFinal = ((i+1) == total);

				if(!isFinal) {
					
					packer.packInt(packetSize);
					packer.addPayload(bytes, i * packetSize, (i+1 * packetSize));		
					
				} else {
					
					packer.packInt(remainder);
					packer.addPayload(bytes, bytes.length - remainder, remainder);					
				}
				
				packer.close();
				
				if(!send(true,NetDataType.NORMAL, packer.toByteArray())) {
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

	
	public static void main(String[] args) {
		new TcpClient("localhost", 9956);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
