package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CharacterRequest;
import com.example.demo.dto.CharacterResponse;
import com.example.demo.services.CharacterServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/characters")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class CharacterController {

	@Autowired
	private CharacterServiceImpl charService;
	
	@GetMapping(value="/houses")
	public ResponseEntity<List<String>> getAllHouses(){
		log.info("in controller :: to get all the houses...");
		
		try {
			
			List<String> allHouses = this.charService.getAllUniqueHouses();
			
			return ResponseEntity.status(HttpStatus.OK).body(allHouses);
			
		}catch(Exception e) {
			log.error("error in getting all the houses...");
			e.printStackTrace();
		}
		
		log.warn("issue with getting all houses...");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);	
	}
	
	@GetMapping(value="/familytree/{houseName}")
	public ResponseEntity<List<CharacterResponse>> getCharactersForGivenHouse(@PathVariable("") String houseName){
		log.info("in controller :: getting all characters for the given house name : {}", houseName);
		
		try {
			
			List<CharacterResponse> allCharsForGivenHouse = this.charService.getCharactersByHouseName(houseName);
			
			return ResponseEntity.status(HttpStatus.OK).body(allCharsForGivenHouse);
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error("exception at getting characters belonging to given house");
		}
		
		log.warn("some issue in getting chars for given house, data has not been returned!");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@PutMapping(value="/{id}/favourite")
	public ResponseEntity<CharacterResponse> addOrRemoveFavourite(@PathVariable("id") String id){
		log.info("adding or removing favourite value for the character with id {} ", id);
		
		try {
			CharacterResponse characterUpdated = this.charService.addOrRemoveFavourite(id);
			
			return ResponseEntity.status(HttpStatus.OK).body(characterUpdated);
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error("error while adding/removing fav for the character");
		}
		
		log.info("issue while setting the fav, the data HAS NOT been set...");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	//secondary API : SubRequired API
	////////////////////////////////////////////////////////////////////////////////////////
	
	
	@GetMapping(value="/{id}/siblings")
	public ResponseEntity<List<CharacterResponse>> getAllSiblingsOfCharacterById(@PathVariable("id") String id){
		log.info("in controller :: fetching all sibling of character with id : {}", id);
		
		try {
			
			List<CharacterResponse> allSiblings = this.charService.findAllSiblingsByCharId(id);
			
			if(allSiblings.size()>0) {
				log.info("number of siblings : {} ", allSiblings.size());
				
				return ResponseEntity.status(HttpStatus.OK).body(allSiblings);	
			}else {
				log.warn("NO siblings found");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(allSiblings);
			}
			
			
		}catch(Exception e) {
			log.error("Exception occured :: while fetching the siblings of the given character");
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	
	
	@GetMapping(value="/{id}/partners")
	public ResponseEntity<List<CharacterResponse>> getAllPartnersOfCharacterById(@PathVariable("id") String id){
		log.info("in controller :: fetching all Partners of character with id : {}", id);
		
		try {
			
			List<CharacterResponse> allPartners = this.charService.findAllMarriedByCharId(id);
			
			if(allPartners.size()>0) {
				log.info("number of partners : {} ", allPartners.size());
				
				return ResponseEntity.status(HttpStatus.OK).body(allPartners);	
			}else {
				log.warn("NO Partners found");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(allPartners);
			}
			
			
		}catch(Exception e) {
			log.error("Exception occured :: while fetching the Partners of the given character");
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	
	
	@GetMapping(value="/{id}/children")
	public ResponseEntity<List<CharacterResponse>> getAllChildrenOfCharacterById(@PathVariable("id") String id){
		log.info("in controller :: fetching all children of character with id : {}", id);
		
		try {
			
			List<CharacterResponse> allChildren = this.charService.findAllChildrenByCharId(id);
			
			if(allChildren.size()>0) {
				log.info("number of children : {} ", allChildren.size());
				
				return ResponseEntity.status(HttpStatus.OK).body(allChildren);	
			}else {
				log.warn("NO children found");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(allChildren);
			}
			
			
		}catch(Exception e) {
			log.error("Exception occured :: while fetching the Children of the given character");
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	
	@GetMapping(value="/{id}/parents")
	public ResponseEntity<List<CharacterResponse>> getAllParentsOfCharacterById(@PathVariable("id") String id){
		log.info("in controller :: fetching all parents of character with id : {}", id);
		
		try {
			
			List<CharacterResponse> allParents = this.charService.findAllParentsByCharId(id);
			
			if(allParents.size()>0) {
				log.info("number of parents : {} ", allParents.size());
				
				return ResponseEntity.status(HttpStatus.OK).body(allParents);	
			}else {
				log.warn("NO parents found");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(allParents);
			}
			
			
		}catch(Exception e) {
			log.error("Exception occured :: while fetching the Parents of the given character");
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//additional required API endpoints
	/////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping(value="/")
	public ResponseEntity<List<CharacterResponse>> getAllCharacters(){
		log.info("in controller :: getting all characters");
		try {
			
			List<CharacterResponse> allChars = this.charService.getAllCharacters();
			
			return ResponseEntity.status(HttpStatus.OK).body(allChars);
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Error while fetching all the characters...");
		}
		
		log.warn("not able to fetch the characters, data HAS NOT BEEN sent!");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
 	
	
	@GetMapping(value="/{id}")
	public ResponseEntity<CharacterResponse> getCharById(@PathVariable("id") String id){
		log.info("in controller :: getting character by id...");
		
		try {
			CharacterResponse characterFetched = this.charService.getCharacterById(id);
			
			log.info("character fetched :: {} ", characterFetched);
			
			return ResponseEntity.status(HttpStatus.OK).body(characterFetched);
		}catch(Exception e) {
			e.printStackTrace();
			log.error("exception while fetching the character by given id {}", id);
		}
		
		log.warn("data HAS NOT BEEN sent to the UI...");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
	
	@PostMapping(value="/")
	public ResponseEntity<CharacterResponse> addCharacter(@RequestBody CharacterRequest character){
		log.info("in controller :: adding the characcter : {} ", character);
		
		try {
			
			CharacterResponse charactedAdded = this.charService.addCharacter(character);
			
			log.info("character has been added successfully");
			
			return ResponseEntity.status(HttpStatus.CREATED).body(charactedAdded);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Exception while adding a new character to the DB");
		}
		
		log.warn("new object HAS NOT been added in db ");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<CharacterResponse> deleteCharById(@PathVariable("id") String id){
		log.info("in controller :: deleting by id : {} ", id);
		
		try {
			CharacterResponse toBedeleted = this.charService.deleteCharacterById(id);
			log.info("deleted successfully");
			return ResponseEntity.status(HttpStatus.OK).body(toBedeleted);
		}catch(Exception e) {
			log.error("error in deletion");
			e.printStackTrace();
		}
		log.warn("ID has not been deleted, some issue");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
	
	@DeleteMapping(value="/")
	public ResponseEntity<String> deleteAll(){
		log.info("deleting all the data..");
		
		try {
			
			boolean isDeleted = this.charService.deleteAll();
			
			if(isDeleted == true) {
				log.info("all data is deleted successfully");
				return ResponseEntity.status(HttpStatus.OK).body("deleted all");
			}
		}catch(Exception e) {
			log.error("error while deleting all data");
			e.printStackTrace();
		}
		
		log.warn("data is not deleted some issue ");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not deleted");
	}
	
}
