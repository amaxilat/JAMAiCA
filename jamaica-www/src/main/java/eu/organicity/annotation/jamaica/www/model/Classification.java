package eu.organicity.annotation.jamaica.www.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Describes a detected Anomaly Entry.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Classification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entities_id")
    private CEntity entity;
    private String entityAttribute;
    private String attributeValue;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tags_id")
    private CTag tag;
    
    private double score;
    private long classificationConfigId;
    private long startTime;
    private long processingTime;
}
