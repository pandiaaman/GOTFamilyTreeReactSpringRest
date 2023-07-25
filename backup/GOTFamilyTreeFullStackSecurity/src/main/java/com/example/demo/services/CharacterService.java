package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.CharacterRequest;
import com.example.demo.dto.CharacterResponse;
import com.example.demo.models.Characters;

public interface CharacterService {

	public List<String> getAllUniqueHouses();
	
	public List<CharacterResponse> getAllCharacters();
	
	public CharacterResponse getCharacterById(String characterId);
	
	public List<CharacterResponse> getCharactersByHouseName(String houseName);
	
	public CharacterResponse addOrRemoveFavourite(String characterId);
	
	public CharacterResponse addCharacter(CharacterRequest character);
	
	public CharacterResponse deleteCharacterById(String characterId);
	
	public boolean deleteAll();
	
	public boolean addCharactersUsingJSONdata(List<CharacterRequest> characters);
	
	public List<CharacterResponse> findAllSiblingsByCharId(String characterId);
	
	public List<CharacterResponse> findAllChildrenByCharId(String characterId);
	
	public List<CharacterResponse> findAllParentsByCharId(String characterId);
	
	public List<CharacterResponse> findAllMarriedByCharId(String characterId);
}
