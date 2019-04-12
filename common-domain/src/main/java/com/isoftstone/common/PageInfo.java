package com.isoftstone.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.github.pagehelper.Page;
@SuppressWarnings({"rawtypes", "unchecked"})
public class PageInfo<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3928691272412041580L;
	 //当前页
    private int pageNum;
    //每页的数量
    private int pageSize;
    //总记录数
    private long total;
    //总记录数
    private String visuaOutput;
    //总记录数
    private String visuaInput;
    //总页数
    private int pages;
    //结果集
    private List<T> list;
    //是否为第一页
    private boolean isFirstPage = false;
    //是否为最后一页
    private boolean isLastPage = false;
    //增删改
    private String supportToDo;
    //自定义操作    
    private String udfToDo;
    
    public String getSupportToDo() {
		return supportToDo;
	}

	public void setSupportToDo(String supportToDo) {
		this.supportToDo = supportToDo;
	}

	public String getUdfToDo() {
		return udfToDo;
	}

	public void setUdfToDo(String udfToDo) {
		this.udfToDo = udfToDo;
	}

	public PageInfo() {
    }

    /**
     * 包装Page对象
     *
     * @param list
     */
    public PageInfo(List<T> list) {
        if (list instanceof Page) {
            Page page = (Page) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();

            this.pages = page.getPages();
            this.list = page;
            this.total = page.getTotal();
        } else if (list instanceof Collection) {
            this.pageNum = 1;
            this.pageSize = list.size();

            this.pages = 1;
            this.list = list;
            this.total = list.size();
        }
        if (list instanceof Collection) {
            //判断页面边界
            judgePageBoudary();
        }
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = pageNum == 1;
        isLastPage = pageNum == pages;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public String getVisuaOutput() {
		return visuaOutput;
	}

	public void setVisuaOutput(String visuaOutput) {
		this.visuaOutput = visuaOutput;
	}

	public String getVisuaInput() {
		return visuaInput;
	}

	public void setVisuaInput(String visuaInput) {
		this.visuaInput = visuaInput;
	}

	@Override
	public String toString() {
		return "PageInfo [supportToDo=" + supportToDo + ", udfToDo=" + udfToDo + "]";
	}

}
