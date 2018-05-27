package kr.co.pionnet.dy.common.dto;

import java.sql.Timestamp;

public class ServerInfo {

	private String server_id;
	private String server_ip;
	private String server_nm;
	private String server_dsc;
	private String lsn_key;
	private String change_port;
	private String port;
	private String org_port;
	private String type;
	private String work_type;
	private String protocol;
	private String use_yn;
	private String server_type;
	private String status;
	private byte display_order;
	private String config_type;
	private String property_key;
	private String alias;
	private Timestamp insert_datetime;
	private Timestamp update_datetime;
	private Timestamp status_update_datetime;
	private String driver_nm;
	private String url;
	private String username;
	private String password;
	private String dbid;
	private String host_nm;
	private String version;
	private String is_valid;
	private String exp_dt;
	private char sms_yn;
	
	
	public char getSms_yn() {
		return sms_yn;
	}
	public void setSms_yn(char sms_yn) {
		this.sms_yn = sms_yn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public byte getDisplay_order() {
		return display_order;
	}
	public void setDisplay_order(byte display_order) {
		this.display_order = display_order;
	}
	public String getServer_id() {
		return server_id;
	}
	public void setServer_id(String server_id) {
		this.server_id = server_id;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getOrg_port() {
		return org_port;
	}
	public void setOrg_port(String org_port) {
		this.org_port = org_port;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getServer_ip() {
		return server_ip;
	}
	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}
	public String getServer_nm() {
		return server_nm;
	}
	public void setServer_nm(String server_nm) {
		this.server_nm = server_nm;
	}
	public String getLsn_key() {
		return lsn_key;
	}
	public void setLsn_key(String lsn_key) {
		this.lsn_key = lsn_key;
	}
	public String getServer_type() {
		return server_type;
	}
	public void setServer_type(String server_type) {
		this.server_type = server_type;
	}
	public String getWork_type() {
		return work_type;
	}
	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}
	public String getServer_dsc() {
		return server_dsc;
	}
	public void setServer_dsc(String server_dsc) {
		this.server_dsc = server_dsc;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Timestamp getInsert_datetime() {
		return insert_datetime;
	}
	public void setInsert_datetime(Timestamp insert_datetime) {
		this.insert_datetime = insert_datetime;
	}
	public Timestamp getUpdate_datetime() {
		return update_datetime;
	}
	public void setUpdate_datetime(Timestamp update_datetime) {
		this.update_datetime = update_datetime;
	}
	public Timestamp getStatus_update_datetime() {
		return status_update_datetime;
	}
	public void setStatus_update_datetime(Timestamp status_update_datetime) {
		this.status_update_datetime = status_update_datetime;
	}
	public String getConfig_type() {
		return config_type;
	}
	public void setConfig_type(String config_type) {
		this.config_type = config_type;
	}
	public String getProperty_key() {
		return property_key;
	}
	public void setProperty_key(String property_key) {
		this.property_key = property_key;
	}
	public String getChange_port() {
		return change_port;
	}
	public void setChange_port(String change_port) {
		this.change_port = change_port;
	}
	public String getDriver_nm() {
		return driver_nm;
	}
	public void setDriver_nm(String driver_nm) {
		this.driver_nm = driver_nm;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDbid() {
		return dbid;
	}
	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	public String getHost_nm() {
		return host_nm;
	}
	public void setHost_nm(String host_nm) {
		this.host_nm = host_nm;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getIs_valid() {
		return is_valid;
	}
	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}
	public String getExp_dt() {
		return exp_dt;
	}
	public void setExp_dt(String exp_dt) {
		this.exp_dt = exp_dt;
	}
	
	
}
