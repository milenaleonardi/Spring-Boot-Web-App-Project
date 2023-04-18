package com.example.application.data.repository;

import com.example.application.data.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    //usar 'searchTerm' como um parametro em um query selecionando primeiro nome e ultimo nome
    @Query("select c from Contact c " +
    "where lower(c.firstName) like lower(concat('%', :searchTerm, '%'))" +
    "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Contact> search(@Param("searchTerm")String searchTerm);
}
