package projectbase.utils;
/**
 * 用于保存数据库中字典表的字典项，供与枚举项对应
 * @author tudoubaby
 *
 */
public class DictItem{
	private int itemId;
	private String itemName;
	public DictItem(int itemid,String itemName){
		itemId=itemid;
		this.itemName=itemName;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int value) {
		this.itemId = value;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String value) {
		this.itemName = value;
	}
}
