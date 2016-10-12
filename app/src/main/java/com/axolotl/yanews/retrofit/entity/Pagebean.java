
package com.axolotl.yanews.retrofit.entity;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Pagebean {

    @SerializedName("allPages")
    @Expose
    private Integer allPages;
    @SerializedName("contentlist")
    @Expose
    private List<News> news = new ArrayList<News>();
    @SerializedName("currentPage")
    @Expose
    private Integer currentPage;
    @SerializedName("allNum")
    @Expose
    private Integer allNum;
    @SerializedName("maxResult")
    @Expose
    private Integer maxResult;

    /**
     * 
     * @return
     *     The allPages
     */
    public Integer getAllPages() {
        return allPages;
    }

    /**
     * 
     * @param allPages
     *     The allPages
     */
    public void setAllPages(Integer allPages) {
        this.allPages = allPages;
    }

    /**
     * 
     * @return
     *     The news
     */
    public List<News> getNews() {
        return news;
    }

    /**
     * 
     * @param news
     *     The news
     */
    public void setNews(List<News> news) {
        this.news = news;
    }

    /**
     * 
     * @return
     *     The currentPage
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * 
     * @param currentPage
     *     The currentPage
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 
     * @return
     *     The allNum
     */
    public Integer getAllNum() {
        return allNum;
    }

    /**
     * 
     * @param allNum
     *     The allNum
     */
    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    /**
     * 
     * @return
     *     The maxResult
     */
    public Integer getMaxResult() {
        return maxResult;
    }

    /**
     * 
     * @param maxResult
     *     The maxResult
     */
    public void setMaxResult(Integer maxResult) {
        this.maxResult = maxResult;
    }

}
