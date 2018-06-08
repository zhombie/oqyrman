package com.example.zhomart.oqyrman.Models;

public class Book {
    private int id;
    private String title;
    private String title_latin;
    private String author;
    private String author_latin;
    private String image;

    public String getImage_latin() {
        return image_latin;
    }

    public void setImage_latin(String image_latin) {
        this.image_latin = image_latin;
    }

    private String image_latin;
    private String description;
    private String description_latin;
    private String epub_cyrillic;
    private String epub_latin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEpub_cyrillic() {
        return epub_cyrillic;
    }

    public void setEpub_cyrillic(String epub_cyrillic) {
        this.epub_cyrillic = epub_cyrillic;
    }

    public String getEpub_latin() {
        return epub_latin;
    }

    public void setEpub_latin(String epub_latin) {
        this.epub_latin = epub_latin;
    }

    public String getTitle_latin() {
        return title_latin;
    }

    public void setTitle_latin(String title_latin) {
        this.title_latin = title_latin;
    }

    public String getAuthor_latin() {
        return author_latin;
    }

    public void setAuthor_latin(String author_latin) {
        this.author_latin = author_latin;
    }

    public String getDescription_latin() {
        return description_latin;
    }

    public void setDescription_latin(String description_latin) {
        this.description_latin = description_latin;
    }
}
