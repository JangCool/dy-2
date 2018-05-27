package kr.co.pionnet.dy.vo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import kr.co.pionnet.dy.type.ErrorType;
import kr.co.pionnet.dy.vo.pack.ProfilePack;

public class TrackerVO {
	
	public String aid;
	public short gid;
	public long txid;
	public long sTime;
	public long eTime;
	public int elapsed;
	
	public AtomicInteger level = new AtomicInteger();
	
	public boolean exclusion;

	public long cpu_t;
	public long threadId;
	public Thread thread;
	public String uid;

    public List<ProfilePack> profile;
	public ProfilePack sql;
	public ProfilePack fetch = new ProfilePack();
    
    public  AtomicInteger profileStep = new AtomicInteger();    
    
    public boolean isSql;

	public int sql_t;
	public int sql_c;
	public int fetch_c;
	

	public String l_ip;
	public String r_ip;
	public String serviceNm;
	public int serv_h;
	public String method;
	public String query_s;
	public int qst_h;
	public String httpContentType;
	public int ref_h;
	public int uagent_h;
	public String uagent;
	public short e_type;
	public int e;
	public int e_cls;
	public byte e_g = ErrorType.ERR_GRP_UNKNOWN;

	
	public TrackerVO() {}
	
	public TrackerVO(List wPorfileCollection) {
		this.profile = wPorfileCollection;
	}
	
	@Override
	public String toString() {
		  StringBuilder result = new StringBuilder();
		  String newLine = System.getProperty("line.separator");

		  result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);

		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		      result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");

		  return result.toString();
		}

}

