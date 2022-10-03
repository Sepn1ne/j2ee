package first.bean;

/**
 * @author Sepnine
 * @version 1.0
 * @description: TODO
 * @date 2022/9/25 13:44
 */
public class Page {
    //总数据量
    private int total;
    //当前页
    private int currentPage;
    //每页记录
    private int count;
    //每页的最后一个记录下标
    private int end;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Page() {
    }

    public Page(int total, int currentPage, int count, int end) {
        this.total = total;
        this.currentPage = currentPage;
        this.count = count;
        this.end = end;
    }
}
