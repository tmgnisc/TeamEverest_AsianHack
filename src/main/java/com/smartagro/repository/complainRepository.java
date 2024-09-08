package com.smartagro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartagro.model.Complains;

public interface complainRepository extends JpaRepository<Complains, Integer>{

}
