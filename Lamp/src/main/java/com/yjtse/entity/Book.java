package com.yjtse.entity;

/**
 * Created by yjtse on 2017/4/5.
 */
public class Book {

    private long bookId;// 图书ID

    private String name;// 图书名称

    private int numbe;// 馆藏数量

    public Book() {
    }

    public Book(long bookId, String name, int numbe) {
        this.bookId = bookId;
        this.name = name;
        this.numbe = numbe;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumbe() {
        return numbe;
    }

    public void setNumbe(int numbe) {
        this.numbe = numbe;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", numbe=" + numbe +
                '}';
    }
}
