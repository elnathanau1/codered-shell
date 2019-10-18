package com.eau.codered.coderedshell.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="hashtag_rankings_zscore")
public class HashtagRankingZscoreEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    private double fgPercent;

    private double ftPercent;

    private double threepm;

    private double pts;

    private double treb;

    private double ast;

    private double stl;

    private double blk;

    private double turnovers;

    @Transient
    private double total;

}
