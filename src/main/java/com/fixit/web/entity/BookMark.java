package com.fixit.web.entity;

import com.fixit.web.audit.Auditable;

import javax.persistence.*;

@Entity
@Table(name = "bookmarks")
public class BookMark extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmarker")
    private Profile bookmarker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artisan")
    private Profile artisan;

    public BookMark() {
    }

    public BookMark(Profile bookmarker, Profile artisan) {
        this.bookmarker = bookmarker;
        this.artisan = artisan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Profile getBookmarker() {
        return bookmarker;
    }

    public void setBookmarker(Profile bookmarker) {
        this.bookmarker = bookmarker;
    }

    public Profile getArtisan() {
        return artisan;
    }

    public void setArtisan(Profile artisan) {
        this.artisan = artisan;
    }

    @Override
    public String toString() {
        return "BookMark{" +
                "id=" + id +
                '}';
    }
}
