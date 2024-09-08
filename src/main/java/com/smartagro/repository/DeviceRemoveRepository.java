package com.smartagro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartagro.model.DeviceRequestRemoval;

public interface DeviceRemoveRepository extends JpaRepository<DeviceRequestRemoval, Integer>{
	
	

}
