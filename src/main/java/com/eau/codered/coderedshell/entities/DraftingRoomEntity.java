package com.eau.codered.coderedshell.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="drafting_room")
public class DraftingRoomEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private int league;

    private int espnRank;

    private double espnAdp;

    private int hashtagRank;

    private String name;

    private String pos;

    private int gp;

    private double mpg;

    private double fgPercent;

    private double ftPercent;

    private double threepm;

    private double pts;

    private double treb;

    private double ast;

    private double stl;

    private double blk;

    private double turnovers;

    private double total;

    @LastModifiedDate
    private Instant lastModified;
}
