package com.example.demo.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.customExceptions.CharacterForIdNotFoundException;
import com.example.demo.customExceptions.NoCharactersAvailableInDbException;
import com.example.demo.dto.CharacterRequest;
import com.example.demo.dto.CharacterResponse;
import com.example.demo.dto.CharacterWithChildrenResponse;
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
	
	public static CharacterWithChildrenResponse mapCharactersToCharacterWithChildrenResponse(Characters character, List<CharacterWithChildrenResponse> allChildren) {
		return CharacterWithChildrenResponse.builder()
				.characterName(character.getCharacterName())
				.parentOf(allChildren)
				.attributes(mapCharacterToCharacterResponse(character))
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
	public List<CharacterWithChildrenResponse> getCharactersByHouseName(String houseName) {
		log.info("in service :: fetching character by houseName... {} ",houseName);
		
		List<Characters> fetchedCharsByHouseName = new ArrayList<>();
	
		fetchedCharsByHouseName = this.charRepo.findByHouseName(houseName);
		
		//logger
		log.info("characters in the selected house : {} ", houseName);
		for(Characters char1 : fetchedCharsByHouseName) {
			log.info(" => {} \n", char1.getCharacterName());
		}
		
		//old code : only getting names of children and not the object
		
//		List<CharacterResponse> allCharsResultbyHouseName = new ArrayList<>();
//		
//		try {
//			allCharsResultbyHouseName = fetchedCharsByHouseName.stream().map(character -> mapCharacterToCharacterResponse(character)).collect(Collectors.toList());
//		}catch(Exception e) {
//			log.error("ERROR while mapping character to characterResponse during fetch process");
//			e.printStackTrace();
//		}
		
		//new idea : since we need to create a tree, we need to have a CharacterResponse within parent CharacterResponse
		
		//we need to fetch each character from the list received from DB
		//check the names present inside its parentOf tag: i.e., its children
		//fetch these children in same way from DB
		//keep a track of the characters who have been inserted, so that they are not inserted twice
		
//		List<CharacterWithChildrenResponse> characterRelationships = new ArrayList<>();
//		
//		for(Characters character : fetchedCharsByHouseName) {
//
//			List<CharacterResponse> allChildren = this.findAllChildrenByCharId(character.getCharacterId());
//			
//			CharacterWithChildrenResponse resultCharacter = mapCharactersToCharacterWithChildrenResponse(character, allChildren);
//			
//			characterRelationships.add(resultCharacter);
//			
//		}
		
		//Recursion :
		
		//why is above code not enough? :: we used ParentOf as List<CharacterResponse>
		//which means that now we can only go till one level of children , not any level further then that
		//to resolve this, we need to use ParrentsOf as List<CharacterWithChildrenResponse>
		//we use RECURSION :
			//we go to first item in list -> get all its children until null
			//for each child in the list -> get all its children until null
		//base case : all children nodes have been reached 
		
		/*
		 * A -> ( D -> ( P, Q), E)
		 * B -> (F, G -> (R -> X, Y -> (z1,z2))
		 * C
		 */
		
		
		//next problem : 
		/*
		 * lets say both A and B belong to the same house
		 * and B is the child of A
		 * now if we simply load the children, firstly in the initial nodes, both A and B will come
		 * then B will come again as the child of A
		 * motive :
		 * we need to check if B is going to occur as a child of someone in the house, if yes, then 
		 * DO NOT show B in the initial list
		 * to do this:
		 * we can say if the parents array list of B is empty: then it has no parents in the house and we will add it
		 * else we will not add it
		 * 
		 */
		List<CharacterWithChildrenResponse> characterRelationshipsFinal = new ArrayList<>();
		
//		CharacterWithChildrenResponse eachCharacterResponse = findEachCharacterChildrenResponse(fetchedCharsByHouseName);
		for(Characters character : fetchedCharsByHouseName) { //A, B, C
			log.info("working on character : {} ", character);
			//converting characters to CharacterWithChildrenResponse that actually has list of CharacterWithChildrenResponse as its children
			//we keep the children (parentOf) as null initially
			if(character.getParents()==null || character.getParents().isEmpty()) {//so that the children dont repeat multiple times
				CharacterWithChildrenResponse thisCharacter = mapCharactersToCharacterWithChildrenResponse(character, null);//empty children list
				//we want the list of children with CharacterWithChildrenResponse to go inside the parentOf part
				List<CharacterWithChildrenResponse> eachCharacterChildrenListResponse = findEachCharacterChildrenResponse(character);
				//we set the children to the given character
				thisCharacter.setParentOf(eachCharacterChildrenListResponse);	
				//we add this character to the final output list to json
				characterRelationshipsFinal.add(thisCharacter);
			}
			
		}
		return characterRelationshipsFinal;
	}


	public List<CharacterResponse> findFavouriteCharacters(){
		log.info("inside service :: findFavouriteCharacters");
		
		List<Characters> allFavCharacters = this.charRepo.findByIsFavourite(true);
		
		if(allFavCharacters.size()==0) {
			log.warn("No fav characters yet");
			throw new NoCharactersAvailableInDbException("No favourite characters yet");
		}
		
		List<CharacterResponse> favCharResponse = new ArrayList<CharacterResponse>();
		
		for(Characters eachChar : allFavCharacters) {
			favCharResponse.add(mapCharacterToCharacterResponse(eachChar));
		}
		
		return favCharResponse;
		
	}
	
	private List<CharacterWithChildrenResponse> findEachCharacterChildrenResponse(Characters character) {
		log.info("recursive method :: {} ", character);
		//recursive method
//		List<CharacterResponse> allChildren = this.findAllChildrenByCharId(character.getCharacterId());
	
		List<String> allChildren = character.getParentOf();
		//base case
		if(allChildren==null || allChildren.isEmpty()) {
			return null;
		}
			
		//else
			List<CharacterWithChildrenResponse> characterWithChildrenList = new ArrayList<>();	
			
//			int noOfChildren = allChildren.size();
		
			for(String child : allChildren) {
				
				Characters childCharacter = this.charRepo.findByCharacterName(child);
				
				CharacterWithChildrenResponse thisChild = mapCharactersToCharacterWithChildrenResponse(childCharacter, null);
				
				thisChild.setParentOf(findEachCharacterChildrenResponse(childCharacter));
				
				characterWithChildrenList.add(thisChild);
				
//				thisChild.getParentOf().add(findEachCharacterChildrenResponse(childCharacter));
			}
		
		return characterWithChildrenList;
		
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
