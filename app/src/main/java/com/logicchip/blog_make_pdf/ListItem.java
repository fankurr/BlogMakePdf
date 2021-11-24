package com.logicchip.blog_make_pdf;

/**
 * Created by Akhil Ashok
 * akhilashok123@gmail.com
 */
public class ListItem {
    private String name,company,date,amount;
    public ListItem(){}
    public ListItem(String name,String company,String date,String amount){
        this.name=name;this.company=company;this.date=date;this.amount=amount;
    }

    public void setName(String name) { this.name = name; }
    public void setCompany(String company) { this.company = company; }
    public void setDate(String date) { this.date = date; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getName() { return name; }
    public String getCompany() { return company; }
    public String getDate() { return date; }
    public String getAmount() { return amount; }
}
