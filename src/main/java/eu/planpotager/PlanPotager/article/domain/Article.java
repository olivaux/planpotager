package eu.planpotager.PlanPotager.article.domain;

import eu.planpotager.PlanPotager.registry.domain.Variety;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article")
    private Long id;
    private String title;

    @Column(name = "name_content")
    private String content;

    @Column(name = "link")
    private String url;

    @ManyToOne
    @JoinColumn(name = "name_variety")
    private Variety variety;

    protected Article() {
    }

    public Article(String title, String content, String url, Variety variety) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.variety = variety;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public Variety getVariety() {
        return variety;
    }
}
