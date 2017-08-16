package projectbase.utils;

import java.io.Serializable;

public class Pager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int itemCount = 0;

	protected int pageSize = 15;

	protected int pageIndex = 0;

	public Pager() {
	}

	public Pager(int pageSize) {
		this.setPageSize(pageSize);
	}

	public Pager(int pageSize, int pageNum) {
		this.setPageSize(pageSize);
		this.setPageNum(pageNum);
	}

	/**
	 * if the currently required page is the last page
	 */
	protected boolean lastPage = false;

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int value) {
		itemCount = value;
		// adjust the pageIndex to the actual number of the last page
		if (lastPage == true || pageIndex > getPageCount() - 1) {
			pageIndex = getPageCount() - 1;
		}
		if (getPageCount() >= 0 && pageIndex < 0) {
			pageIndex = 0;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int value) {
		pageSize = value;
	}

	public int getPageIndex()

	{
		return pageIndex;
	}

	public void setPageIndex(int value) {
		pageIndex = value;
	}

	public void setLastPage(boolean value)

	{
		lastPage = value;
	}

	public int getPageNum() {
		return pageIndex + 1;
	}

	public void setPageNum(int value) {
		pageIndex = value - 1;
	}

	public int getPageCount() {
		if (itemCount == 0)
			return 0;
		return pageSize == 0 ? 1 : ((int) Math.ceil((double) itemCount
				/ pageSize));
	}

	public int getFromRowNum() {
		return pageSize == 0 ? 1 : (pageIndex * pageSize + 1);
	}

	public int getToRowNum() {
		return pageSize == 0 ? getItemCount() : ((pageIndex + 1) * pageSize);
	}

	public int getFromRowIndex()

	{
		return pageSize == 0 ? 0 : (pageIndex * pageSize);
	}

	public int getToRowIndex()

	{
		return (pageSize == 0 ? getItemCount() : (pageIndex + 1) * pageSize) - 1;
	}

}
