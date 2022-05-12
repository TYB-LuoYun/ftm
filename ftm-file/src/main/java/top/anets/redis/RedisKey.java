/**
 * 
 */
package top.anets.redis;

/**
 * @author Administrator
 *
 */
public enum RedisKey {
	USER("user:",0),
	ITEM("item:",30),
	XUANJI_TOKEN("xuanJi_token",60),
	Business("business",0),
	Invoicing("invoicing",0),
	InvoicingTrue("InvoicingTrue",0),
	AutoInvoicing("AutoInvoicing",0),
	Invaliding("invaliding",0),
	Channel_log("logs",0),
	Channel_autoInvoice_log("autoInvoice",0),
	Storj_log("storj_log",60*60*24),
	Path("path",60*60*24) ;

    public static final String AutoInvocieOrgs = "AutoInvocieOrgs";
    public static final String AutoInvocieOrgMachines = "AutoInvocieOrgMachines";
    public static final String Block = "block";
	public static final String Txs ="Txs" ;
	public static final String PendingTxns = "PendingTxns";
	public static String  Invoice_Wait ="DocStatus_Wait";
	public static String Invoice_Error ="Invoice_Error";
	public static String Invoice_Invalid ="Invoice_Invalid";

	private String prefix;
	private int seconds;
	
	
	RedisKey(String prefix, int seconds) {
		this.prefix=prefix;
		this.seconds=seconds;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getKey() {
		return prefix;
	}
	public void setKey(String key) {
		this.prefix = key;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	
}
