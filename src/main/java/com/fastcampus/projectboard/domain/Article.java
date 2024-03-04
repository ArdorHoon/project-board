package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title; // 제목
    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문

    @Setter
    private String hashtag; // 해시태그


    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    //순환 참조 마기 위한 코드
    @ToString.Exclude
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성일시
    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy; //생성자
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; //수정자

//    public void setArticleComments(Set<ArticleComment> articleComments) {
//        this.articleComments = articleComments;
//    }

    protected Article(){

    }
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    //Factory Method로 제공
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }


    //id는  not null 인 경우 isEquals 사용
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        /*
        *영속화되지 않았다고 하면 동등성 검사 자체가 의미 없는 걸로 보고 다 다른 것으로
        *간주하거나 혹은 처리하지 않겠다는 의미 (id != null의 의미)
         */
        return id != null & id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
