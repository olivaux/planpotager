package eu.planpotager.PlanPotager.garden.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Long id;

    @Column(name = "point_leftUp_x")
    private Double leftUpX;

    @Column(name = "point_leftUp_y")
    private Double leftUpY;

    @Column(name = "point_rightUp_x")
    private Double rightUpX;

    @Column(name = "point_rightUp_y")
    private Double rightUpY;

    @Column(name = "point_rightDown_x")
    private Double rightDownX;

    @Column(name = "point_rightDown_y")
    private Double rightDownY;

    @Column(name = "point_leftDown_x")
    private Double leftDownX;

    @Column(name = "point_leftDown_y")
    private Double leftDownY;

    @ManyToOne
    @JoinColumn(name = "id_garden")
    private Garden garden;

    protected Area() {
    }

    public Area(Garden garden, Double leftUpX, Double leftUpY, Double rightUpX, Double rightUpY,
            Double rightDownX, Double rightDownY, Double leftDownX, Double leftDownY) {
        this.garden = garden;
        this.leftUpX = leftUpX;
        this.leftUpY = leftUpY;
        this.rightUpX = rightUpX;
        this.rightUpY = rightUpY;
        this.rightDownX = rightDownX;
        this.rightDownY = rightDownY;
        this.leftDownX = leftDownX;
        this.leftDownY = leftDownY;
    }

    public Long getId() {
        return id;
    }

    public Garden getGarden() {
        return garden;
    }

    public Double getLeftUpX() {
        return leftUpX;
    }

    public Double getLeftUpY() {
        return leftUpY;
    }

    public Double getRightUpX() {
        return rightUpX;
    }

    public Double getRightUpY() {
        return rightUpY;
    }

    public Double getRightDownX() {
        return rightDownX;
    }

    public Double getRightDownY() {
        return rightDownY;
    }

    public Double getLeftDownX() {
        return leftDownX;
    }

    public Double getLeftDownY() {
        return leftDownY;
    }

    public void setPoints(Double leftUpX, Double leftUpY, Double rightUpX, Double rightUpY,
            Double rightDownX, Double rightDownY, Double leftDownX, Double leftDownY) {
        this.leftUpX = leftUpX;
        this.leftUpY = leftUpY;
        this.rightUpX = rightUpX;
        this.rightUpY = rightUpY;
        this.rightDownX = rightDownX;
        this.rightDownY = rightDownY;
        this.leftDownX = leftDownX;
        this.leftDownY = leftDownY;
    }
}
