package dev.marisol.helpdesk_software.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import dev.marisol.helpdesk_software.entities.RequestEntity;

public interface RequestRepository extends JpaRepository <RequestEntity, Long> {


}
