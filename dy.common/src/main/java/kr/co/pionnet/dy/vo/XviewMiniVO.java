package kr.co.pionnet.dy.vo;

import java.io.Serializable;

public class XviewMiniVO  implements Serializable {
	
	public String aid;
	public long etime;
	public int elapsed;
	public int e;
	public int sql_t;
	public int cpu_t;
	public short e_type;
	public int s_h;     //service hash 

	public XviewMiniVO(String aid, long etime, int elapsed,int cpu_t,int sql_t,int err,short e_type,int s_h) {
		this(aid, etime, elapsed,err,e_type);
		this.sql_t = sql_t;
		this.cpu_t = cpu_t;
		this.s_h = s_h;
	}
	
	public XviewMiniVO(String aid, long etime, int elapsed,int err,short e_type,int s_h) {
		this(aid, etime, elapsed);
		this.e = err;
		this.e_type = e_type;
		this.s_h = s_h;
	}
	
	public XviewMiniVO(String aid, long etime, int elapsed,int err,short e_type) {
		this(aid, etime, elapsed);
		this.e = err;
		this.e_type = e_type;
	}
	
	public XviewMiniVO(String aid, long etime, int elapsed) {
		this.aid = aid;
		this.etime = etime;
		this.elapsed = elapsed;
	}
}
