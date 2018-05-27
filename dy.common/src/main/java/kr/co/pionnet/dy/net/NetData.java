package kr.co.pionnet.dy.net;

public class NetData {
	
	private byte dataType;

	private boolean isJson;
	
	private boolean isPack;
	
	private boolean isNormal;
	
	private byte[] bytes;
	
	
	public byte getDataType() {
		return dataType;
	}

	public void setDataType(byte dataType) {
		this.dataType = dataType;
	}

	public void setJson(boolean isJson) {
		this.isJson = isJson;
	}

	public boolean isJson() {
		return isJson;
	}
	
	public void setPack(boolean isPack) {
		this.isPack = isPack;
	}
	
	public boolean isPack() {
		return isPack;
	}

	public void setNormal(boolean isNormal) {
		this.isNormal = isNormal;
	}
	
	public boolean isNormal() {
		return isNormal;
	}
	
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("NetData { ");
		sb.append("noraml = ").append(isNormal).append(", ");
		sb.append("pack = ").append(isPack).append(", ");
		sb.append("json = ").append(isJson).append(", ");
		sb.append("bytes = ").append(bytes).append(", ");
		sb.append("bytesLength = ").append((bytes != null) ? bytes.length : 0);
		sb.append(" }");
		
		return sb.toString();
	}
	
	


}
