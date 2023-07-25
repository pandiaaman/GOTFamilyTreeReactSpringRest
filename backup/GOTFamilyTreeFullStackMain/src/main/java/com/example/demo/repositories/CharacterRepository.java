package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Characters;

public interface CharacterRepository extends JpaRepository<Characters, String>{

	public List<Characters> findByHouseName(String houseName);
	
	public Characters findByCharacterName(String characterName);
}
