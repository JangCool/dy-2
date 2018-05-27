package kr.co.pionnet.dy.vo;

public class AppVo {

	public int app_hash;
	public String log_dt;
	public String hh;
	public String mm;
	public String aid;
	public long elapsed;
	public long cpu_time;	
	public int err_cnt;
	public long sql_exec_time;
	public long sql_cnt;
	public int getApp_hash() {
		return app_hash;
	}
	public void setApp_hash(int app_hash) {
		this.app_hash = app_hash;
	}
	public String getLog_dt() {
		return log_dt;
	}
	public void setLog_dt(String log_dt) {
		this.log_dt = log_dt;
	}
	public String getHh() {
		return hh;
	}
	public void setHh(String hh) {
		this.hh = hh;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public long getElapsed() {
		return elapsed;
	}
	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}
	public long getCpu_time() {
		return cpu_time;
	}
	public void setCpu_time(long cpu_time) {
		this.cpu_time = cpu_time;
	}
	
	public int getErr_cnt() {
		return err_cnt;
	}
	public void setErr_cnt(int err_cnt) {
		this.err_cnt = err_cnt;
	}
	public long getSql_exec_time() {
		return sql_exec_time;
	}
	public void setSql_exec_time(long sql_exec_time) {
		this.sql_exec_time = sql_exec_time;
	}
	public long getSql_cnt() {
		return sql_cnt;
	}
	public void setSql_cnt(long sql_cnt) {
		this.sql_cnt = sql_cnt;
	}
	
}
