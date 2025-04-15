package com.example.sveltespringboot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Table(name="DV_NOTI")
public class NotiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Override
    public String toString(){
        return "[" + id + "] title = " + title;
    }

    public Long getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }
}
