package com.example.demo.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.customExceptions.CharacterForIdNotFoundException;
import com.example.demo.customExceptions.NoCharactersAvailableInDbException;
import com.example.demo.dto.CharacterRequest;
import com.example.demo.dto.CharacterResponse;
import com.example.demo.models.Characters;
import com.example.demo.repositories.CharacterRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CharacterServiceImpl implements CharacterService{

	@Autowired
	private CharacterRepository charRepo;
	
	
	private static CharacterResponse mapCharacterToCharacterResponse(Characters character) {
		return CharacterResponse.builder()
				.characterId(character.getCharacterId())
				.characterName(character.getCharacterName())
				.houseName(character.getHouseName())
				.marriedEngaged(character.getMarriedEngaged())
				.parentOf(character.getParentOf())
				.parents(character.getParents())
				.siblings(character.getSiblings())
				.isFavourite(character.isFavourite())
				.characterImageThumb(character.getCharacterImageThumb())
				.characterImageFull(character.getCharacterImageFull())
				.characterLink(character.getCharacterLink())
				.actorName(character.getActorName())
				.actorLink(character.getActorLink())
				.nickname(character.getNickname())
				.royal(character.isRoyal())
				.killed(character.getKilled())
				.killedBy(character.getKilledBy())
				.build();
	}
	
	private static Characters mapCharacterResponseToCharacter(CharacterResponse character) {
		return Characters.builder()
				.characterId(character.getCharacterId())
				.characterName(character.getCharacterName())
				.houseName(character.getHouseName())
				.marriedEngaged(character.getMarriedEngaged())
				.parentOf(character.getParentOf())
				.parents(character.getParents())
				.siblings(character.getSiblings())
				.isFavourite(character.isFavourite())
				.characterImageThumb(character.getCharacterImageThumb())
				.characterImageFull(character.getCharacterImageFull())
				.characterLink(character.getCharacterLink())
				.actorName(character.getActorName())
				.actorLink(character.getActorLink())
				.nickname(character.getNickname())
				.royal(character.isRoyal())
				.killed(character.getKilled())
				.killedBy(character.getKilledBy())
				.build();
	}
	
	public static Characters mapCharacterRequestToCharacter(CharacterRequest character) {
		return Characters.builder()
				.characterName(character.getCharacterName())
				.houseName(character.getHouseName())
				.isFavourite(character.isFavourite())
				.parentOf(character.getParentOf())
				.marriedEngaged(character.getMarriedEngaged())
				.parents(character.getParents())
				.siblings(character.getSiblings())
				.characterImageThumb(character.getCharacterImageThumb())
				.characterImageFull(character.getCharacterImageFull())
				.characterLink(character.getCharacterLink())
				.actorName(character.getActorName())
				.actorLink(character.getActorLink())
				.nickname(character.getNickname())
				.royal(character.isRoyal())
				.killed(character.getKilled())
				.killedBy(character.getKilledBy())
				.build();
	}
	
	@Override
	public List<String> getAllUniqueHouses() {
		log.info("in service :: getting all unique houses...");
		
		List<CharacterResponse> allCharacters = this.getAllCharacters();
		
		Set<String> allUniqueHouses = new HashSet<String>();
		
		for(CharacterResponse character : allCharacters) {
			
			//in case houseName is a single string
			allUniqueHouses.add(character.getHouseName());
		}
		
		List<String> finalHousesList = new ArrayList<String>();
		try {
			finalHousesList.addAll(allUniqueHouses);
		}catch(Exception e) {
			log.error("Exception occured during fetching the list of house names");
			e.printStackTrace();
		}
		return finalHousesList;
	}

	@Override
	public List<CharacterResponse> getAllCharacters() {
		log.info("in service :: getting all characters...");
		
		List<Characters> allCharacters = this.charRepo.findAll();
		
		if(allCharacters.size()==0) {
			throw new NoCharactersAvailableInDbException("No Characters are available in DB");
		}
		
		List<CharacterResponse> allCharsResult = new ArrayList<>();
		try {
			allCharsResult = allCharacters.stream().map(character -> mapCharacterToCharacterResponse(character)).collect(Collectors.toList());
		}catch(Exception e) {
			log.error("ERROR while mapping character to characterResponse during fetch process");
			e.printStackTrace();
		}
		return allCharsResult;
	}

	@Override
	public CharacterResponse getCharacterById(String characterId) {
		log.info("in service :: fetching character by id... {} ",characterId);
		
		Characters fetchedCharacter = this.charRepo.findById(characterId).orElseThrow(() -> new CharacterForIdNotFoundException("No character found with given id"));
		
		CharacterResponse resultCharacter = mapCharacterToCharacterResponse(fetchedCharacter);
		
		return resultCharacter;
	}

	@Override
	public List<CharacterResponse> getCharactersByHouseName(String houseName) {
		log.info("in service :: fetching character by houseName... {} ",houseName);
		
		List<Characters> fetchedCharsByHouseName = new ArrayList<>();
		fetchedCharsByHouseName = this.charRepo.findByHouseName(houseName);
		
		List<CharacterResponse> allCharsResultbyHouseName = new ArrayList<>();
		
		try {
			allCharsResultbyHouseName = fetchedCharsByHouseName.stream().map(character -> mapCharacterToCharacterResponse(character)).collect(Collectors.toList());
		}catch(Exception e) {
			log.error("ERROR while mapping character to characterResponse during fetch process");
			e.printStackTrace();
		}
		
		return allCharsResultbyHouseName;
	}

	@Override
	public CharacterResponse addOrRemoveFavourite(String characterId) {
		log.info("in service :: posting the selected character as favourite / Removing the favourite if already set as fav");
		
		CharacterResponse characterForId = this.getCharacterById(characterId);
		log.info("current value for isFav of character is : {}", characterForId.isFavourite() );
		
		characterForId.setFavourite(characterForId.isFavourite()?false:true);
		log.info("after updating the fav value : we have : {} ", characterForId.isFavourite());
		
		log.info("now saving the updated character...");
		this.charRepo.save(mapCharacterResponseToCharacter(characterForId));
		
		return characterForId;
	}

	@Override
	public CharacterResponse addCharacter(CharacterRequest character) {
		log.info("in service :: adding the character : {} ", character);
		
		Characters charToBeAdded = mapCharacterRequestToCharacter(character);  
			
		Characters charAdded = this.charRepo.save(charToBeAdded);
		
		
		return mapCharacterToCharacterResponse(charAdded);
	}

	@Override
	public boolean addCharactersUsingJSONdata(List<CharacterRequest> characters) {	
		log.info("in service :: adding all characters using the json data...");
		
//		List<CharacterResponse> addedCharacterList = new ArrayList<CharacterResponse>();
		
		List<Characters> charactersList = new ArrayList<>();
		
//		int counterCharacterRecords = 0;
		try {
			characters.forEach(character ->
				charactersList.add(Characters.builder()
				.characterName(character.getCharacterName())
				.houseName(character.getHouseName())
				.marriedEngaged(character.getMarriedEngaged())
				.parentOf(character.getParentOf())
				.parents(character.getParents())
				.siblings(character.getSiblings())
				.isFavourite(character.isFavourite())
				.characterImageThumb(character.getCharacterImageThumb())
				.characterImageFull(character.getCharacterImageFull())
				.characterLink(character.getCharacterLink())
				.actorName(character.getActorName())
				.actorLink(character.getActorLink())
				.nickname(character.getNickname())
				.royal(character.isRoyal())
				.killed(character.getKilled())
				.killedBy(character.getKilledBy())
				.build()));
			
			this.charRepo.saveAll(charactersList);
			
			return true;
		
		}catch(Exception e) {
			log.error("ERROR while mapping the json data to object...");
			e.printStackTrace();
		}
		
		return false;
	}

	
	@Override
	public CharacterResponse deleteCharacterById(String characterId) {
		log.info("in service :: deleting the character by id...");
		
		CharacterResponse characterToBeDeleted = this.getCharacterById(characterId);
		
		this.charRepo.deleteById(characterId);
		
		return characterToBeDeleted;
	}

	@Override
	public boolean deleteAll() {
		log.info("deleting all characters...");
		
		try {
			this.charRepo.deleteAll();
			return true;
		}catch(Exception e) {
			log.error("error in deleting the characters...");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<CharacterResponse> findAllSiblingsByCharId(String characterId) {

		log.info("finding all siblings of character with id : {} ", characterId);
		
		CharacterResponse characterInQuestion = this.getCharacterById(characterId);
		
		log.info("character's name is : {} ", characterInQuestion.getCharacterName());
		
		List<CharacterResponse> allSiblings = new ArrayList<>();
		
		if(characterInQuestion.getSiblings()!=null) {
			for(String siblingName : characterInQuestion.getSiblings()) {
				
				Characters siblingCharacter = this.charRepo.findByCharacterName(siblingName);
				
				allSiblings.add(mapCharacterToCharacterResponse(siblingCharacter));
			}
		}
		
		return allSiblings;
	}

	@Override
	public List<CharacterResponse> findAllChildrenByCharId(String characterId) {
		log.info("finding all children of character with id : {} ", characterId);
		
		CharacterResponse characterInQuestion = this.getCharacterById(characterId);
		
		log.info("character's name is : {} ", characterInQuestion.getCharacterName());
		
		List<CharacterResponse> allChildren = new ArrayList<>();
		
		if(characterInQuestion.getParentOf()!=null) {
			for(String childName : characterInQuestion.getParentOf()) {
				
				Characters childCharacter = this.charRepo.findByCharacterName(childName);
				
				allChildren.add(mapCharacterToCharacterResponse(childCharacter));
			}
		}
		
		return allChildren;
	}

	@Override
	public List<CharacterResponse> findAllParentsByCharId(String characterId) {
		log.info("finding all parents of character with id : {} ", characterId);
		
		CharacterResponse characterInQuestion = this.getCharacterById(characterId);
		
		log.info("character's name is : {} ", characterInQuestion.getCharacterName());
		
		List<CharacterResponse> allParents = new ArrayList<>();
		
		if(characterInQuestion.getParents()!=null) {
			for(String parentName : characterInQuestion.getParents()) {
				
				Characters parentCharacter = this.charRepo.findByCharacterName(parentName);
				
				allParents.add(mapCharacterToCharacterResponse(parentCharacter));
			}
		}
		
		return allParents;
	}

	@Override
	public List<CharacterResponse> findAllMarriedByCharId(String characterId) {
		log.info("finding all married to character with id : {} ", characterId);
		
		CharacterResponse characterInQuestion = this.getCharacterById(characterId);
		
		log.info("character's name is : {} ", characterInQuestion.getCharacterName());
		
		List<CharacterResponse> allMarriedTo = new ArrayList<>();
		
		if(characterInQuestion.getMarriedEngaged()!=null) {
			for(String partnerName : characterInQuestion.getMarriedEngaged()) {
				
				Characters partnerCharacter = this.charRepo.findByCharacterName(partnerName);
				
				allMarriedTo.add(mapCharacterToCharacterResponse(partnerCharacter));
			}
		}
		
		return allMarriedTo;
	}

	
	
}
