/*
 * ------------------------------------------------------------------------------
 * @Project       : dy_collector
 * @Source        : ApplData.java
 * @Description   : 
 * @Author        : l22sangdlf
 *------------------------------------------------------------------------------
 *                  蹂�         寃�         �궗         �빆                       
 *------------------------------------------------------------------------------
 *    DATE     AUTHOR                      DESCRIPTION                        
 * ----------  ------  --------------------------------------------------------- 
 * 2017. 6. 20.   �씠�긽�씪    �떊洹쒖깮�꽦                                     
 *------------------------------------------------------------------------------
 */
/**   
* @Title: ApplData.java 
* @Package kr.co.pionnet.dy.collector.was.server.worker.apps 
* @Description: TODO(�뼱�븘由ъ��씠�뀡 summary �뜲�씠�� ) 
* @author l22sangdlf 
* @date 2017. 6. 20. �삤�쟾 11:24:06 
* @version V1.0   
*/ 
package kr.co.pionnet.dy.vo;

import java.io.Serializable;

/** 
* @ClassName: ApplData 
* @Description: TODO
* @author l22sangdlf
* @date 2017. 6. 20. �삤�쟾 11:24:06 
*  
*/
public class AppSummaryVO implements Serializable {

	public int app_hash;
	public String log_dt;
	public String hh;
	public String mm;
	public String aid;
	public long elapsed_sum;
	public double elapsed_avg;
	public long elapsed_min;
	public long elapsed_max;
	public long cpu_time_sum;
	public long cnt;
	public long err_cnt;
	public long sql_exec_time_sum;
	public double sql_cnt_avg;
	public double sql_exec_time_avg;
	public long sql_exec_time_max;
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
	public long getElapsed_sum() {
		return elapsed_sum;
	}
	public void setElapsed_sum(long elapsed_sum) {
		this.elapsed_sum = elapsed_sum;
	}
	public double getElapsed_avg() {
		return elapsed_avg;
	}
	public void setElapsed_avg(double elapsed_avg) {
		this.elapsed_avg = elapsed_avg;
	}
	public long getElapsed_min() {
		return elapsed_min;
	}
	public void setElapsed_min(long elapsed_min) {
		this.elapsed_min = elapsed_min;
	}
	public long getElapsed_max() {
		return elapsed_max;
	}
	public void setElapsed_max(long elapsed_max) {
		this.elapsed_max = elapsed_max;
	}
	public long getCpu_time_sum() {
		return cpu_time_sum;
	}
	public void setCpu_time_sum(long cpu_time_sum) {
		this.cpu_time_sum = cpu_time_sum;
	}
	public long getCnt() {
		return cnt;
	}
	public void setCnt(long cnt) {
		this.cnt = cnt;
	}
	public long getErr_cnt() {
		return err_cnt;
	}
	public void setErr_cnt(long err_cnt) {
		this.err_cnt = err_cnt;
	}
	public long getSql_exec_time_sum() {
		return sql_exec_time_sum;
	}
	public void setSql_exec_time_sum(long sql_exec_time_sum) {
		this.sql_exec_time_sum = sql_exec_time_sum;
	}
	public double getSql_cnt_avg() {
		return sql_cnt_avg;
	}
	public void setSql_cnt_avg(double sql_cnt_avg) {
		this.sql_cnt_avg = sql_cnt_avg;
	}
	public double getSql_exec_time_avg() {
		return sql_exec_time_avg;
	}
	public void setSql_exec_time_avg(double sql_exec_time_avg) {
		this.sql_exec_time_avg = sql_exec_time_avg;
	}
	public long getSql_exec_time_max() {
		return sql_exec_time_max;
	}
	public void setSql_exec_time_max(long sql_exec_time_max) {
		this.sql_exec_time_max = sql_exec_time_max;
	}
}


