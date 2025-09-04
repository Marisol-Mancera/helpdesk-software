package dev.marisol.helpdesk_software.entities;

import java.time.LocalDateTime;

import dev.marisol.helpdesk_software.enums.RequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "requests")


public class RequestEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 120)
    private String applicantName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicEntity topic;

    private String attendedBy;
    
    private LocalDateTime attendedAt;

    public RequestEntity(){
    }

    @PrePersist
    private void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
}

    @PreUpdate
    private void onUpdate() {
    this.updatedAt = LocalDateTime.now();
}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getDescription() {
        return description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TopicEntity getTopic() {
        return topic;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

     public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

     public String getAttendedBy() {
        return attendedBy;
    }

    public LocalDateTime getAttendedAt() {
        return attendedAt;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

     public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setTopic(TopicEntity topic) {
        this.topic = topic;
    }

    public void setAttendedBy(String attendedBy) {
        this.attendedBy = attendedBy;
    }

    public void setAttendedAt(LocalDateTime attendedAt) {
        this.attendedAt = attendedAt;
    }

}

//protected para que no se puedan modificar desde fuera
