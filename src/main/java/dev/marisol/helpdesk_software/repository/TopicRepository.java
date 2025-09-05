package dev.marisol.helpdesk_software.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.marisol.helpdesk_software.entities.TopicEntity;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long>{

}
 
