package dev.marisol.helpdesk_software.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "topics")

public class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // evita valores nulos, valores repetidos y longitud maxima
    @Column(nullable = false, unique = true, length = 120)
    private String name;
    // evita nulos y permite hacer borrado lógico (desactivar en vez de borrar)
    @Column(nullable = false)
    private boolean active = true;

    public TopicEntity() {
    }

    public long getId(){
        return id;
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName(){
        return name;
    }

    public void setName(){
        this.name = name;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }
    
}
