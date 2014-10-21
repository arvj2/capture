package com.claro.cfcmobile.dto;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/25/14.
 */
public class BudgetSpinnerItem {
    private String desc;
    private  int count;

    public  BudgetSpinnerItem( String desc,int count ){
        this.desc = desc;
        this.count = count;
    }

    public String getDesc() {
        return desc;
    }

    public int getCount() {
        return count;
    }
}
