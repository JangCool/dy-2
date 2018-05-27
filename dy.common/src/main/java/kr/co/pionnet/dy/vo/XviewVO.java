package kr.co.pionnet.dy.vo;

import java.io.Serializable;

public class XviewVO  implements Serializable {
	 
	public String aid;
	public short gid;
	public String txid;
	public long stime;
	public long etime;
	public int elapsed;
	public int serv_h;//service_hash
	public String serv_nm;//service_hash name
    public int uagent_h;
    public int qst_h;    
	public int sql_t;
	public int sql_c;
	public int fetch_c;
	public long cpu_t;
	public int bytes;
	public String l_ip;//local ip
	public String r_ip;//remote ip
	public String method;
	public int ref_h;//referer hash	
	public short e_type;  //error_hashcode
	public int e;  //error_hashcode
	public int e_g;
	public String e_cls;
	public String dummy2;

}
